/*
 * Copyright (c) 2023. Viktor Pop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reboot297.jardin.info

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_HEALTH_COLD
import android.os.BatteryManager.BATTERY_HEALTH_DEAD
import android.os.BatteryManager.BATTERY_HEALTH_GOOD
import android.os.BatteryManager.BATTERY_HEALTH_OVERHEAT
import android.os.BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE
import android.os.BatteryManager.BATTERY_HEALTH_UNKNOWN
import android.os.BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE
import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.BatteryManager.BATTERY_STATUS_DISCHARGING
import android.os.BatteryManager.BATTERY_STATUS_FULL
import android.os.BatteryManager.BATTERY_STATUS_NOT_CHARGING
import android.os.BatteryManager.BATTERY_STATUS_UNKNOWN
import android.os.Build
import org.json.JSONObject

/**
 * Info about battery state.
 */
internal class BatteryInfo internal constructor(
    context: Context,
    json: JSONObject,
) : Base(context) {

    private val batteryManager =
        (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager)

    private val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    private val root = json.getOrCreate("info-battery")

    /**
     * Get battery characteristics.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-battery": {
     *     "ACTION_BATTERY_CHANGED": {
     *       "present": true,
     *       "health": "2 (good)",
     *       "technology": "Li-ion",
     *       "temperature": "250 (25°C)",
     *       "voltage": "5000 (5.0V)",
     *       "cycleCount": 10
     *     }
     *   }
     * }
     *
     * ```
     * @see [Intent.ACTION_BATTERY_CHANGED]
     * @see [BatteryManager.EXTRA_PRESENT]
     * @see [BatteryManager.EXTRA_HEALTH]
     * @see [BatteryManager.EXTRA_TECHNOLOGY]
     * @see [BatteryManager.EXTRA_TEMPERATURE]
     * @see [BatteryManager.EXTRA_VOLTAGE]
     * @see [BatteryManager.EXTRA_CYCLE_COUNT]
     */
    internal fun characteristics() {
        val isPresent = intent
            ?.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
            ?: "intent is null"

        val health = intent
            ?.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            ?: "intent is null"

        val technology = intent
            ?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            ?: "intent is null"

        val temperature = intent
            ?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            ?: "intent is null"

        val temperatureDetails = when (temperature) {
            is Int -> "(${temperature / 10}°C)"
            else -> ""
        }

        val voltage = intent
            ?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            ?: "intent is null"

        val voltageDetails = when (voltage) {
            is Int -> "(${voltage / 1000f}V)"
            else -> ""
        }

        root.getOrCreate("ACTION_BATTERY_CHANGED")
            .put("present", isPresent)
            .put("health", "$health ${healthDetails(health)}")
            .put("technology", technology)
            .put("temperature", "$temperature $temperatureDetails")
            .put("voltage", "$voltage $voltageDetails")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val cycleCount = intent
                ?.getIntExtra(BatteryManager.EXTRA_CYCLE_COUNT, 0)
                ?: "intent is null"
            root.getOrCreate("ACTION_BATTERY_CHANGED")
                .put("cycleCount", cycleCount)
        }
    }

    /**
     * Get battery energy status.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-battery": {
     *     "batteryManager": {
     *       "currentAverage": "900000 (microamperes, charging)",
     *       "chargeCounter": "10000 (microampere-hours)",
     *       "currentNow": "900000 (microamperes, charging)",
     *       "energyCounter": "-9223372036854775808 (nanowatt-hours)"
     *     }
     *   }
     * }
     * ```
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE]
     * @see [BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER]
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_NOW]
     * @see [BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER]
     */
    internal fun energy() {
        val currentAverage =
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)

        val chargeCounter =
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER)

        val currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)

        // TODO(Viktor) review the output and maybe add some details, update docs
        val energyCounter =
            batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER)

        root.getOrCreate("batteryManager")
            .put(
                "currentAverage",
                "$currentAverage (microamperes, ${chargingDetails(currentAverage)})",
            )
            .put("chargeCounter", "$chargeCounter (microampere-hours)")
            .put("currentNow", "$currentNow (microamperes, ${chargingDetails(currentNow)})")
            .put("energyCounter", "$energyCounter (nanowatt-hours)")
    }

    /**
     * Get information related to the battery status.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-battery": {
     *     "batteryManager": {
     *       "capacity": "100 %",
     *       "isCharging": false,
     *       "status": "4 (not charging)",
     *       "computeChargeTimeRemaining": "-1 (computation fails)"
     *     },
     *     "ACTION_BATTERY_CHANGED": {
     *       "plugged": "0 (not charging)",
     *       "batteryLow": false,
     *       "chargingStatus": 0
     *     }
     *   }
     * }
     * ```
     * @see [BatteryManager.BATTERY_PROPERTY_CAPACITY]
     * @see [BatteryManager.BATTERY_PROPERTY_STATUS]
     * @see [BatteryManager.isCharging]
     * @see [BatteryManager.computeChargeTimeRemaining]
     * @see [Intent.ACTION_BATTERY_CHANGED]
     * @see [BatteryManager.EXTRA_BATTERY_LOW]
     * @see [BatteryManager.EXTRA_CHARGING_STATUS]
     * @see [BatteryManager.EXTRA_PLUGGED]
     */
    internal fun status() {
        root.getOrCreate("batteryManager")
            .put(
                "capacity",
                "${batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)} %",
            )
            .put("isCharging", batteryManager.isCharging)

        val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            ?: "intent is null"

        root.getOrCreate("ACTION_BATTERY_CHANGED")
            .put("plugged", "$plugged ${pluggedDetails(plugged)}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
            root.getOrCreate("batteryManager")
                .put("status", "$status ${batteryStatusDetails(status)}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val batteryLow = intent
                    ?.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
                    ?: "intent is null"

                root.getOrCreate("ACTION_BATTERY_CHANGED")
                    .put("batteryLow", batteryLow)

                val remaining = batteryManager.computeChargeTimeRemaining()
                // TODO(Viktor) check validation and update docs
                val remainingDetails = if (remaining == -1L) {
                    "(computation fails)"
                } else {
                    "(millis)"
                }

                root.getOrCreate("batteryManager")
                    .put("computeChargeTimeRemaining", "$remaining $remainingDetails")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    // TODO(Viktor) research more about this status, maybe add some details
                    val chargingStatus = intent
                        ?.getIntExtra(BatteryManager.EXTRA_CHARGING_STATUS, 0)
                        ?: "intent is null"

                    root.getOrCreate("ACTION_BATTERY_CHANGED")
                        .put("chargingStatus", chargingStatus)
                }
            }
        }
    }

    /**
     * Get All information related to the battery.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-battery": {
     *     "ACTION_BATTERY_CHANGED": {
     *       "present": true,
     *       "health": "2 (good)",
     *       "technology": "Li-ion",
     *       "temperature": "250 (25°C)",
     *       "voltage": "5000 (5.0V)",
     *       "cycleCount": 10,
     *       "plugged": "0 (not charging)",
     *       "batteryLow": false,
     *       "chargingStatus": 0
     *     },
     *     "batteryManager": {
     *       "capacity": "100 %",
     *       "isCharging": false,
     *       "status": "4 (not charging)",
     *       "computeChargeTimeRemaining": "-1 (computation fails)",
     *       "currentAverage": "900000 (microamperes, charging)",
     *       "chargeCounter": "10000 (microampere-hours)",
     *       "currentNow": "900000 (microamperes, charging)",
     *       "energyCounter": "-9223372036854775808 (nanowatt-hours)"
     *     }
     *   }
     * }
     * ```
     * @see [BatteryManager.BATTERY_PROPERTY_CAPACITY]
     * @see [BatteryManager.BATTERY_PROPERTY_STATUS]
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE]
     * @see [BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER]
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_NOW]
     * @see [BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER]
     * @see [BatteryManager.isCharging]
     * @see [BatteryManager.computeChargeTimeRemaining]
     * @see [Intent.ACTION_BATTERY_CHANGED]
     */
    internal fun full() {
        characteristics()
        status()
        energy()
    }

    private fun chargingDetails(value: Int): String {
        return if (value < 0) {
            "discharging"
        } else {
            "charging"
        }
    }

    private fun batteryStatusDetails(value: Int): String {
        return when (value) {
            BATTERY_STATUS_UNKNOWN -> "(unknown)"
            BATTERY_STATUS_CHARGING -> "(charging)"
            BATTERY_STATUS_DISCHARGING -> "(discharging)"
            BATTERY_STATUS_NOT_CHARGING -> "(not charging)"
            BATTERY_STATUS_FULL -> "(full)"
            else -> ""
        }
    }

    private fun healthDetails(value: Comparable<*>): String {
        return when (value) {
            BATTERY_HEALTH_UNKNOWN -> "(unknown)"
            BATTERY_HEALTH_GOOD -> "(good)"
            BATTERY_HEALTH_OVERHEAT -> "(overheat)"
            BATTERY_HEALTH_DEAD -> "(dead)"
            BATTERY_HEALTH_OVER_VOLTAGE -> "(over-voltage)"
            BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "(unspecified failure)"
            BATTERY_HEALTH_COLD -> "(cold)"
            else -> ""
        }
    }

    private fun pluggedDetails(value: Comparable<*>): String {
        return when (value) {
            BatteryManager.BATTERY_PLUGGED_AC -> "(AC)"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "(Wireless)"
            BatteryManager.BATTERY_PLUGGED_USB -> "(USB)"
            BatteryManager.BATTERY_PLUGGED_DOCK -> "(dock)"
            else -> "(not charging)"
        }
    }
}
