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

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.reboot297.jardin.core.Jardin
import com.reboot297.jardin.info.Info
import com.reboot297.jardin.info.InfoAdapter
import com.reboot297.jardin.info.Item

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = InfoAdapter(data)
        val recyclerView = findViewById<RecyclerView>(R.id.itemsListView)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.startRecording).setOnClickListener {
            Jardin.startRecording(applicationContext)
        }
        findViewById<Button>(R.id.stopRecording).setOnClickListener { Jardin.stopRecording() }
    }

    private val data = arrayOf(
        Item("Battery Full") { Info(applicationContext).battery().print() },
        Item("Battery Characteristics") {
            Info(applicationContext).batteryCharacteristics().print()
        },
        Item("Battery Energy") { Info(applicationContext).batteryEnergy().print() },
        Item("Battery Status") { Info(applicationContext).batteryStatus().print() },
        Item("") { },

        Item("Features Full") { Info(applicationContext).features().print() },
        Item("Features Audio") { Info(applicationContext).featuresAudio().print() },
        Item("Features Biometrics") { Info(applicationContext).featuresBiometrics().print() },
        Item("Features Bluetooth") { Info(applicationContext).featuresBluetooth().print() },
        Item("Features Camera") { Info(applicationContext).featuresCamera().print() },
        Item("Features DeviceType") { Info(applicationContext).featuresDeviceType().print() },
        Item("Features Graphics") { Info(applicationContext).featuresGraphics().print() },
        Item("Features Location") { Info(applicationContext).featuresLocation().print() },
        Item("Features NFC") { Info(applicationContext).featuresNFC().print() },
        Item("Features Sensors") { Info(applicationContext).featuresSensors().print() },
        Item("Features Telephony") { Info(applicationContext).featuresTelephony().print() },
        Item("Features Touch") { Info(applicationContext).featuresTouch().print() },
        Item("Features VR") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Info(applicationContext).featuresVR().print()
            } else {
                Toast.makeText(this, "Available since API 24", Toast.LENGTH_LONG).show()
            }
        },
        Item("Features Wifi") { Info(applicationContext).featuresWifi().print() },
        Item("") { },

        Item("Locales Full") { Info(applicationContext).locales().print() },
        Item("Locales Application") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Info(applicationContext).localesApplication().print()
            } else {
                Toast.makeText(this, "Available since API 33", Toast.LENGTH_LONG).show()
            }
        },
        Item("Locales GrammaticalGender") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                Info(applicationContext).localesGrammaticalGender().print()
            } else {
                Toast.makeText(this, "Available since API 34", Toast.LENGTH_LONG).show()
            }
        },
        Item("Locales System") { Info(applicationContext).localesSystem().print() },
        Item("") { },

        Item("Sensors Full") { Info(applicationContext).sensors().print() },
        Item("Sensors Available") { Info(applicationContext).sensorsAvailable().print() },
        Item("Sensors Dynamic") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Info(applicationContext).sensorsDynamic().print()
            } else {
                Toast.makeText(this, "Available since API 24", Toast.LENGTH_LONG).show()
            }
        },
        Item("Sensors Accelerometer") { Info(applicationContext).sensorsAccelerometer().print() },
        Item("Sensors Gravity") { Info(applicationContext).sensorsGravity().print() },
        Item("Sensors Gyroscope") { Info(applicationContext).sensorsGyroscope().print() },
        Item("Sensors Heading") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Info(applicationContext).sensorsHeading().print()
            } else {
                Toast.makeText(this, "Available since API 33", Toast.LENGTH_LONG).show()
            }
        },
        Item("Sensors HeadTracker") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Info(applicationContext).sensorsHeadTracker().print()
            } else {
                Toast.makeText(this, "Available since API 33", Toast.LENGTH_LONG).show()
            }
        },
        Item("Sensors Heart") { Info(applicationContext).sensorsHeart().print() },
        Item("Sensors HingeAngle") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Info(applicationContext).sensorsHingeAngle().print()
            } else {
                Toast.makeText(this, "Available since API 30", Toast.LENGTH_LONG).show()
            }
        },
        Item("Sensors Light") { Info(applicationContext).sensorsLight().print() },
        Item("Sensors LinearAcceleration") {
            Info(applicationContext).sensorsLinearAcceleration().print()
        },

        Item("Sensors LowLatencyOffBodyDetect") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Info(applicationContext).sensorsLowLatencyOffBodyDetect().print()
            } else {
                Toast.makeText(this, "Available since API 26", Toast.LENGTH_LONG).show()
            }
        },
        Item("Sensors Magnetic Field") { Info(applicationContext).sensorsMagneticField().print() },

        Item("Sensors MotionDetect") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Info(applicationContext).sensorsMotionDetect().print()
            } else {
                Toast.makeText(this, "Available since API 24", Toast.LENGTH_LONG).show()
            }
        },

        Item("Sensors Orientation") { Info(applicationContext).sensorsOrientation().print() },

        Item("Sensors POSE6DOF") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Info(applicationContext).sensorsPOSE6DOF().print()
            } else {
                Toast.makeText(this, "Available since API 24", Toast.LENGTH_LONG).show()
            }
        },

        Item("Sensors Pressure") { Info(applicationContext).sensorsPressure().print() },
        Item("Sensors Proximity") { Info(applicationContext).sensorsProximity().print() },
        Item("Sensors Relative Humidity") {
            Info(applicationContext).sensorsRelativeHumidity().print()
        },
        Item("Sensors RotationVector") { Info(applicationContext).sensorsRotationVector().print() },
        Item("Sensors Significant motion") {
            Info(applicationContext).sensorsSignificantMotion().print()
        },

        Item("Sensors StationaryDetect") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Info(applicationContext).sensorsStationaryDetect().print()
            } else {
                Toast.makeText(this, "Available since API 24", Toast.LENGTH_LONG).show()
            }
        },

        Item("Sensors Steps") { Info(applicationContext).sensorsSteps().print() },
        Item("Sensors Temperature") { Info(applicationContext).sensorsTemperature().print() },
    )
}
