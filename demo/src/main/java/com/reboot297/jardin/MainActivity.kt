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

package com.reboot297.jardin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.reboot297.jardin.info.Info

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.allInfoButton).setOnClickListener {
            Info(applicationContext)
                .battery()
                .batteryLevel()
                .batteryStatus()
                .isPresent()
                .isBatteryLow()
                .currentAverage()
                .chargeCounter()
                .currentNow()
                .energyCounter()
                .isCharging()
                .chargingStatus()
                .chargingSource()
                .remainingChargeTime()
                .health()
                .technology()
                .temperature()
                .voltage()
                .cycleCount()
                .build()

                .bluetooth()
                .supportFeature()
                .isEnabled()
                .name()
                .address()
                .discoverableTimeout()
                .isDiscovering()
                .state()
                .scanMode()
                .isLeAudioSupported()
                .isLeAudioBroadcastSourceSupported()
                .isLeAudioBroadcastAssistantSupported()
                .maxConnectedAudioDevices()
                .isLe2MPhySupported()
                .isLeCodedPhySupported()
                .isLeExtendedAdvertisingSupported()
                .leMaximumAdvertisingDataLength()
                .isLePeriodicAdvertisingSupported()
                .isMultipleAdvertisementSupported()
                .isOffloadedFilteringSupported()
                .isOffloadedScanBatchingSupported()
                .bondedDevices()
                .build()

                .print()
        }

        findViewById<Button>(R.id.batteryFullButton).setOnClickListener {
            Info(applicationContext).battery().fullInfo().print()
        }

        findViewById<Button>(R.id.batteryLightButton).setOnClickListener {
            Info(applicationContext).battery().briefInfo().print()
        }

        findViewById<Button>(R.id.bluetoothFullButton).setOnClickListener {
            Info(applicationContext).bluetooth().fullInfo().print()
        }

        findViewById<Button>(R.id.bluetoothLightButton).setOnClickListener {
            Info(applicationContext).bluetooth().briefInfo().print()
        }
    }
}
