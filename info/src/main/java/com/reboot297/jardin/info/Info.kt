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
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import android.os.Build
import android.util.Log

/**
 * Print Info to logcat.
 */
object Info {

    private const val TAG = "Info"

    /**
     * Print all information to logcat.
     *
     * @param context application context.
     */
    fun printAll(context: Context) {
        printBatteryInfo(context)
    }

    /**
     * Print information about device battery.
     * @param context application context
     */
    fun printBatteryInfo(context: Context) {
        Log.i(TAG, "--------------------BatteryInfo-------------------")
        val batteryManager =
            (context.getSystemService(BATTERY_SERVICE) as BatteryManager)

        val builder = StringBuilder()
        builder.append("Capacity: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY))
            .append(" %")
            .append("\n")

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

        builder.append("Current average: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE))
            .append(" microamperes")
            .append("\n")

        builder.append("Charge counter: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER))
            .append(" microampere-hours")
            .append("\n")

        builder.append("Current now: ")
            .append(batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW))
            .append(" microamperes")
            .append("\n")

        builder.append("Energy counter: ")
            .append(batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER))
            .append(" nanowatt-hours")
            .append("\n")

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

        builder.append("IsCharging: ")
            .append(batteryManager.isCharging)
            .append("\n")

        Log.i(TAG, builder.toString())

        Log.i(TAG, "--------------------End BatteryInfo----------------")
    }
}
