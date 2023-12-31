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
import android.os.BatteryManager
import android.util.Log
import org.json.JSONObject

private const val TAG = "Info"

/**
 * Get info about the application and device.
 */
class Info(private val applicationContext: Context) {

    private val json = JSONObject()

    private val battery: BatteryInfo by lazy { BatteryInfo(applicationContext, json) }

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
     * @return instance of [Info] object
     * @see [Intent.ACTION_BATTERY_CHANGED]
     * @see [BatteryManager.EXTRA_PRESENT]
     * @see [BatteryManager.EXTRA_HEALTH]
     * @see [BatteryManager.EXTRA_TECHNOLOGY]
     * @see [BatteryManager.EXTRA_TEMPERATURE]
     * @see [BatteryManager.EXTRA_VOLTAGE]
     * @see [BatteryManager.EXTRA_CYCLE_COUNT]
     */
    fun batteryCharacteristics(): Info {
        battery.characteristics()
        return this
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
     * @return instance of [Info] object
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE]
     * @see [BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER]
     * @see [BatteryManager.BATTERY_PROPERTY_CURRENT_NOW]
     * @see [BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER]
     */
    fun batteryEnergy(): Info {
        battery.energy()
        return this
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
     * @return reference to [Info] object
     * @see [BatteryManager.BATTERY_PROPERTY_CAPACITY]
     * @see [BatteryManager.BATTERY_PROPERTY_STATUS]
     * @see [BatteryManager.isCharging]
     * @see [BatteryManager.computeChargeTimeRemaining]
     * @see [Intent.ACTION_BATTERY_CHANGED]
     * @see [BatteryManager.EXTRA_BATTERY_LOW]
     * @see [BatteryManager.EXTRA_CHARGING_STATUS]
     * @see [BatteryManager.EXTRA_PLUGGED]
     */
    fun batteryStatus(): Info {
        battery.status()
        return this
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
     * @return reference to [Info] object
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
    fun battery(): Info {
        battery.full()
        return this
    }

    /**
     * Get info as Json string.
     *
     * @param prettyPrint true if use pretty print.
     * @return string
     */
    fun getText(prettyPrint: Boolean = false): String {
        return if (prettyPrint) {
            json.toString(2)
        } else {
            json.toString()
        }
    }

    /**
     * Print info in logcat.
     *
     * @param tag custom tag fpr the log
     * @param prettyPrint true if use pretty print.
     */
    fun print(tag: String = TAG, prettyPrint: Boolean = true) {
        Log.i(tag, getText(prettyPrint))
    }
}
