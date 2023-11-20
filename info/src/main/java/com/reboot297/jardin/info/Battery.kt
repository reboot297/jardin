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
import android.os.Build

/**
 * Info about battery state.
 */
internal class Battery constructor(
    private val context: Context
) {
    private val batteryManager =
        (context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager)


    /**
     * Get full Info about battery.
     */
    fun fullInfo(builder: StringBuilder) {
        lightInfo(builder)
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        isPresent(intent, builder)
        builder.append("-----------\n")
        isBatteryLow(intent, builder)
        currentAverage(builder)
        chargeCounter(builder)
        currentNow(builder)
        energyCounter(builder)
        builder.append("-----------\n")
        isCharging(builder)
        chargingStatus(intent, builder)
        chargingSource(intent, builder)
        remainingChargeTime(builder)
        builder.append("-----------\n")
        health(intent, builder)
        technology(intent, builder)
        temperature(intent, builder)
        voltage(intent, builder)
        cycleCount(intent, builder)
    }

    /**
     * Get brief info about battery.
     */
    fun lightInfo(builder: StringBuilder) {
        batteryLevel(builder)
        batteryStatus(builder)
    }

    /**
     * Battery charging status.
     */
    private fun batteryStatus(builder: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
            val details = when (status) {
                1 -> "unknown"
                2 -> "charging"
                3 -> "discharging"
                4 -> "not charging"
                5 -> "full"
                else -> ""
            }
            builder.append("Status: ")
                .append(status)
                .append(" ($details)")
                .append("\n")
        }
    }

    /**
     * Remaining battery capacity as an integer percentage of total capacity (with no fractional part).
     */
    private fun batteryLevel(builder: StringBuilder) {
        builder.append("Level: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))
            .append(" %")
            .append("\n")
    }


    /**
     * Check if Battery is present.
     */
    private fun isPresent(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val isPresent = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
            builder.append("isBatteryAvailable: ")
                .append(isPresent)
                .append("\n")
        }
    }

    /**
     * Boolean field indicating whether the battery is currently considered to be low.
     */
    private fun isBatteryLow(intent: Intent?, builder: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            intent?.let {
                val isBatteryLow = intent.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
                builder.append("isBatteryLow: ")
                    .append(isBatteryLow)
                    .append("\n")
            }
        }
    }

    /**
     * Average battery current in microamperes.
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     * The time period over which the average is computed may depend on the fuel gauge hardware
     * and its configuration.
     */
    private fun currentAverage(builder: StringBuilder) {
        val value = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE)
        val details = if (value < 0) {
            "discharging"
        } else {
            "charging"
        }
        builder.append("Current average: ")
            .append(value)
            .append("(microamperes, $details)")
            .append("\n")
    }

    /**
     * Battery capacity in microampere-hours.
     */
    private fun chargeCounter(builder: StringBuilder) {
        builder.append("Charge counter: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER))
            .append("(microampere-hours)")
            .append("\n")
    }

    /**
     * Instantaneous battery current in microamperes.
     * Positive values indicate net current entering the battery from a charge source,
     * negative values indicate net current discharging from the battery.
     */
    private fun currentNow(builder: StringBuilder) {
        val value = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
        val details = if (value < 0) {
            "discharging"
        } else {
            "charging"
        }
        builder.append("Current now: ")
            .append(value)
            .append("(in microamperes, $details)")
            .append("\n")
    }

    /**
     * Battery remaining energy.
     */
    private fun energyCounter(builder: StringBuilder) {
        builder.append("Energy counter: ")
            .append(batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER))
            .append("(nanowatt-hours)")
            .append("\n")
    }

    /**
     * Get Info about charging status and write to the [StringBuilder]
     */
    private fun isCharging(builder: StringBuilder) {
        builder.append("IsCharging: ")
            .append(batteryManager.isCharging)
            .append("\n")
    }

    /**
     * Get Info about charging status and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun chargingStatus(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val status = intent.getIntExtra(BatteryManager.EXTRA_CHARGING_STATUS, 0)
                builder.append("chargingStatus: ")
                    .append(status)
                    .append("\n")
            }
        }
    }

    /**
     * Get Info about charging source and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun chargingSource(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            val details = when (plugged) {
                BatteryManager.BATTERY_PLUGGED_AC -> "AC"
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Wireless"
                BatteryManager.BATTERY_PLUGGED_USB -> "USB"
                BatteryManager.BATTERY_PLUGGED_DOCK -> "dock"
                else -> "not charging"
            }
            builder.append("charging source: ")
                .append(plugged)
                .append("($details)")
                .append("\n")
        }
    }

    /**
     * Calculate remaining charge time and write to the [StringBuilder]
     */
    private fun remainingChargeTime(builder: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val value = batteryManager.computeChargeTimeRemaining()
            builder.append("Charge Time Remaining: ")
            if (value == -1L) {
                builder.append(value).append("(computation fails)")
            } else {
                builder.append(value).append(" millis.")
            }
            builder.append("\n")
        }
    }


    /**
     * Get Info about Battery health and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun health(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            val details = when (health) {
                1 -> "unknown"
                2 -> "good"
                3 -> "overheat"
                4 -> "dead"
                5 -> "over-voltage"
                6 -> "unspecified failure"
                7 -> "cold"
                else -> ""
            }
            builder.append("Health: ")
                .append(health)
                .append(" ($details)")
                .append("\n")
        }
    }

    /**
     * Get Info about Battery technology and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun technology(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val tech = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)
            builder.append("Technology: ")
                .append(tech)
                .append("\n")
        }
    }

    /**
     * Get Info about Battery temperature and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun temperature(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val details = "${temperature / 10f} °C"
            builder.append("Temperature: ")
                .append(temperature)
                .append("($details)")
                .append("\n")
        }
    }

    /**
     * Get Info about Battery voltage and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun voltage(intent: Intent?, builder: StringBuilder) {
        intent?.let {
            val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            val details = "${voltage / 1000f} V"
            builder.append("Voltage: ")
                .append(voltage)
                .append("($details)")
                .append("\n")
        }
    }

    /**
     * Get Info about Cycle count and write to the [StringBuilder]
     * The data is extracted from [Intent]
     */
    private fun cycleCount(intent: Intent?, builder: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            intent?.let {
                val count = intent.getIntExtra(BatteryManager.EXTRA_CYCLE_COUNT, 0)
                builder.append("CycleCount: ")
                    .append(count)
                    .append("\n")
            }
        }
    }
}