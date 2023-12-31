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
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.Sensor.REPORTING_MODE_CONTINUOUS
import android.hardware.Sensor.REPORTING_MODE_ONE_SHOT
import android.hardware.Sensor.REPORTING_MODE_ON_CHANGE
import android.hardware.Sensor.REPORTING_MODE_SPECIAL_TRIGGER
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_ACCELEROMETER_LIMITED_AXES
import android.hardware.Sensor.TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED
import android.hardware.Sensor.TYPE_ACCELEROMETER_UNCALIBRATED
import android.hardware.Sensor.TYPE_ALL
import android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE
import android.hardware.Sensor.TYPE_GAME_ROTATION_VECTOR
import android.hardware.Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR
import android.hardware.Sensor.TYPE_GRAVITY
import android.hardware.Sensor.TYPE_GYROSCOPE
import android.hardware.Sensor.TYPE_GYROSCOPE_LIMITED_AXES
import android.hardware.Sensor.TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED
import android.hardware.Sensor.TYPE_GYROSCOPE_UNCALIBRATED
import android.hardware.Sensor.TYPE_HEADING
import android.hardware.Sensor.TYPE_HEAD_TRACKER
import android.hardware.Sensor.TYPE_HEART_BEAT
import android.hardware.Sensor.TYPE_HEART_RATE
import android.hardware.Sensor.TYPE_HINGE_ANGLE
import android.hardware.Sensor.TYPE_LIGHT
import android.hardware.Sensor.TYPE_LINEAR_ACCELERATION
import android.hardware.Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED
import android.hardware.Sensor.TYPE_MOTION_DETECT
import android.hardware.Sensor.TYPE_ORIENTATION
import android.hardware.Sensor.TYPE_POSE_6DOF
import android.hardware.Sensor.TYPE_PRESSURE
import android.hardware.Sensor.TYPE_PROXIMITY
import android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY
import android.hardware.Sensor.TYPE_ROTATION_VECTOR
import android.hardware.Sensor.TYPE_SIGNIFICANT_MOTION
import android.hardware.Sensor.TYPE_STATIONARY_DETECT
import android.hardware.Sensor.TYPE_STEP_COUNTER
import android.hardware.Sensor.TYPE_STEP_DETECTOR
import android.hardware.Sensor.TYPE_TEMPERATURE
import android.hardware.SensorDirectChannel.RATE_FAST
import android.hardware.SensorDirectChannel.RATE_NORMAL
import android.hardware.SensorDirectChannel.RATE_STOP
import android.hardware.SensorDirectChannel.RATE_VERY_FAST
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject

/**
 * Sensors info.
 */
internal class SensorsInfo internal constructor(
    context: Context,
    json: JSONObject,
) : Base(context) {

    private val root = json.getOrCreate("info-sensors")
    private val sensorManager =
        (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
    private val packageManager = context.packageManager
    private val featuresJson: JSONObject by lazy {
        root.getOrCreate("packageManager")
            .getOrCreate("hasSystemFeature")
    }

    /**
     * Accelerometer sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.accelerometer": true,
     *         "android.hardware.sensor.accelerometer_limited_axes": false,
     *         "android.hardware.sensor.accelerometer_limited_axes_uncalibrated": false
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_ACCELEROMETER)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Accelerometer",
     *           "stringType": "android.sensor.accelerometer",
     *           "type": 1,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 39.300102,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "3.0 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 2.480159E-4,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": true,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       },
     *       "getSensorList(TYPE_ACCELEROMETER_UNCALIBRATED)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Accelerometer Uncalibrated",
     *           "stringType": "android.sensor.accelerometer_uncalibrated",
     *           "type": 35,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 39.300102,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "3.0 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 2.480159E-4,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": true,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_ACCELEROMETER]
     * @see [Sensor.TYPE_ACCELEROMETER_UNCALIBRATED]
     * @see [Sensor.TYPE_ACCELEROMETER_LIMITED_AXES]
     * @see [Sensor.TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED]
     */
    internal fun accelerometer() {
        printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
        sensorManager.getSensorList(TYPE_ACCELEROMETER).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_ACCELEROMETER)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sensorManager.getSensorList(TYPE_ACCELEROMETER_UNCALIBRATED)
                .forEachIndexed { index, sensor ->
                    val sensorJson = root.getOrCreate("sensorManager")
                        .getOrCreate("getSensorList(TYPE_ACCELEROMETER_UNCALIBRATED)")
                        .getOrCreate("[$index]")
                    fullSensorInfo(sensorJson, sensor)
                }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES)
                printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED)
                sensorManager.getSensorList(TYPE_ACCELEROMETER_LIMITED_AXES)
                    .forEachIndexed { index, sensor ->
                        val sensorJson = root.getOrCreate("sensorManager")
                            .getOrCreate("getSensorList(TYPE_ACCELEROMETER_LIMITED_AXES)")
                            .getOrCreate("[$index]")
                        fullSensorInfo(sensorJson, sensor)
                    }
                sensorManager.getSensorList(TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED)
                    .forEachIndexed { index, sensor ->
                        val sensorJson = root.getOrCreate("sensorManager")
                            .getOrCreate("getSensorList(TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED)")
                            .getOrCreate("[$index]")
                        fullSensorInfo(sensorJson, sensor)
                    }
            }
        }
    }

    /**
     * Gravity sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_GRAVITY)": {
     *         "[0]": {
     *           "name": "Gravity Sensor",
     *           "stringType": "android.sensor.gravity",
     *           "type": 9,
     *           "vendor": "AOSP",
     *           "version": 3,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 19.6133,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 2.480159E-4,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_GRAVITY]
     */
    internal fun gravity() {
        sensorManager.getSensorList(TYPE_GRAVITY).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_GRAVITY)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Gyroscope sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.gyroscope": true,
     *         "android.hardware.sensor.gyroscope_limited_axes": false,
     *         "android.hardware.sensor.gyroscope_limited_axes_uncalibrated": false
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_GYROSCOPE)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Gyroscope",
     *           "stringType": "android.sensor.gyroscope",
     *           "type": 4,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 16.46,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "3.0 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 0.001,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": true,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       },
     *       "getSensorList(TYPE_GYROSCOPE_UNCALIBRATED)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Gyroscope (uncalibrated)",
     *           "stringType": "android.sensor.gyroscope_uncalibrated",
     *           "type": 16,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 16.46,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "3.0 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 0.001,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_GYROSCOPE]
     * @see [Sensor.TYPE_GYROSCOPE_UNCALIBRATED]
     * @see [Sensor.TYPE_GYROSCOPE_LIMITED_AXES]
     * @see [Sensor.TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED]
     */
    internal fun gyroscope() {
        printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)
        sensorManager.getSensorList(TYPE_GYROSCOPE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_GYROSCOPE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
        sensorManager.getSensorList(TYPE_GYROSCOPE_UNCALIBRATED).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_GYROSCOPE_UNCALIBRATED)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES)
            printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES_UNCALIBRATED)
            sensorManager.getSensorList(TYPE_GYROSCOPE_LIMITED_AXES)
                .forEachIndexed { index, sensor ->
                    val sensorJson = root.getOrCreate("sensorManager")
                        .getOrCreate("getSensorList(TYPE_GYROSCOPE_LIMITED_AXES)")
                        .getOrCreate("[$index]")
                    fullSensorInfo(sensorJson, sensor)
                }
            sensorManager.getSensorList(TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED)
                .forEachIndexed { index, sensor ->
                    val sensorJson = root.getOrCreate("sensorManager")
                        .getOrCreate("getSensorList(TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED)")
                        .getOrCreate("[$index]")
                    fullSensorInfo(sensorJson, sensor)
                }
        }
    }

    /**
     * Heading sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.heading": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_HEADING]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun heading() {
        printFeature(PackageManager.FEATURE_SENSOR_HEADING)
        sensorManager.getSensorList(TYPE_HEADING).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_HEADING)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * HeadTracker sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.dynamic.head_tracker": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_HEAD_TRACKER]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    internal fun headTracker() {
        printFeature(PackageManager.FEATURE_SENSOR_DYNAMIC_HEAD_TRACKER)
        sensorManager.getSensorList(TYPE_HEAD_TRACKER).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_HEAD_TRACKER)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Heart sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.heartrate": false,
     *         "android.hardware.sensor.heartrate.ecg": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_HEART_RATE]
     * @see [Sensor.TYPE_HEART_BEAT]
     */
    // TODO(Viktor) add sample to doc.
    internal fun heart() {
        printFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)
        printFeature(PackageManager.FEATURE_SENSOR_HEART_RATE_ECG)
        sensorManager.getSensorList(TYPE_HEART_RATE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_HEART_RATE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorManager.getSensorList(TYPE_HEART_BEAT).forEachIndexed { index, sensor ->
                val sensorJson = root.getOrCreate("sensorManager")
                    .getOrCreate("getSensorList(TYPE_HEART_BEAT)")
                    .getOrCreate("[$index]")
                fullSensorInfo(sensorJson, sensor)
            }
        }
    }

    /**
     * Hinge Angle sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.hinge_angle": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_HINGE_ANGLE)": {
     *         "[0]": {
     *           "name": "Goldfish hinge sensor0 (in degrees)",
     *           "stringType": "android.sensor.hinge_angle",
     *           "type": 36,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 360,
     *           "minDelay": "0 (0.0 millis.)",
     *           "power": "3.0 (mA)",
     *           "reportingMode": "1 (REPORTING_MODE_ON_CHANGE)",
     *           "resolution": 1,
     *           "isWakeUpSensor": true,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_HINGE_ANGLE]
     */
    @RequiresApi(Build.VERSION_CODES.R)
    internal fun hingeAngle() {
        printFeature(PackageManager.FEATURE_SENSOR_HINGE_ANGLE)
        sensorManager.getSensorList(TYPE_HINGE_ANGLE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_HINGE_ANGLE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Light sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.light": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_LIGHT)": {
     *         "[0]": {
     *           "name": "Goldfish Light sensor",
     *           "stringType": "android.sensor.light",
     *           "type": 5,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 40000,
     *           "minDelay": "0 (0.0 millis.)",
     *           "power": "20.0 (mA)",
     *           "reportingMode": "1 (REPORTING_MODE_ON_CHANGE)",
     *           "resolution": 1,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_LIGHT]
     */
    internal fun light() {
        printFeature(PackageManager.FEATURE_SENSOR_LIGHT)
        sensorManager.getSensorList(TYPE_LIGHT).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_LIGHT)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Linear Acceleration sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_LINEAR_ACCELERATION)": {
     *         "[0]": {
     *           "name": "Linear Acceleration Sensor",
     *           "stringType": "android.sensor.linear_acceleration",
     *           "type": 10,
     *           "vendor": "AOSP",
     *           "version": 3,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 19.6133,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 2.480159E-4,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_LINEAR_ACCELERATION]
     */
    internal fun linearAcceleration() {
        sensorManager.getSensorList(TYPE_LINEAR_ACCELERATION).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_LINEAR_ACCELERATION)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Low Latency Off Body Detect sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {}
     * }
     * ```
     * @see [Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.O)
    internal fun lowLatencyOffBodyDetect() {
        sensorManager.getSensorList(TYPE_LOW_LATENCY_OFFBODY_DETECT)
            .forEachIndexed { index, sensor ->
                val sensorJson = root.getOrCreate("sensorManager")
                    .getOrCreate("getSensorList(TYPE_LOW_LATENCY_OFFBODY_DETECT)")
                    .getOrCreate("[$index]")
                fullSensorInfo(sensorJson, sensor)
            }
    }

    /**
     * Magnetic field sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_MAGNETIC_FIELD)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Magnetic field sensor",
     *           "stringType": "android.sensor.magnetic_field",
     *           "type": 2,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 2000,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "6.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 0.5,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       },
     *       "getSensorList(TYPE_MAGNETIC_FIELD_UNCALIBRATED)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Magnetic field sensor (uncalibrated)",
     *           "stringType": "android.sensor.magnetic_field_uncalibrated",
     *           "type": 14,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 2000,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "6.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 0.5,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_MAGNETIC_FIELD]
     * @see [Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED]
     */
    internal fun magneticField() {
        sensorManager.getSensorList(TYPE_MAGNETIC_FIELD).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_MAGNETIC_FIELD)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
        sensorManager.getSensorList(TYPE_MAGNETIC_FIELD_UNCALIBRATED)
            .forEachIndexed { index, sensor ->
                val sensorJson = root.getOrCreate("sensorManager")
                    .getOrCreate("getSensorList(TYPE_MAGNETIC_FIELD_UNCALIBRATED)")
                    .getOrCreate("[$index]")
                fullSensorInfo(sensorJson, sensor)
            }
    }

    /**
     * Motion detect sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {}
     * }
     * ```
     * @see [Sensor.TYPE_MOTION_DETECT]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.N)
    internal fun motionDetect() {
        sensorManager.getSensorList(TYPE_MOTION_DETECT).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_MOTION_DETECT)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Orientation sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_ORIENTATION)": {
     *         "[0]": {
     *           "name": "Goldfish Orientation sensor",
     *           "stringType": "android.sensor.orientation",
     *           "type": 3,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 360,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "9.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 1,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         },
     *         "[1]": {
     *           "name": "Orientation Sensor",
     *           "stringType": "android.sensor.orientation",
     *           "type": 3,
     *           "vendor": "AOSP",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 360,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 0.00390625,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_ORIENTATION]
     */
    internal fun orientation() {
        @Suppress("DEPRECATION")
        sensorManager.getSensorList(TYPE_ORIENTATION).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_ORIENTATION)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * POS6DOF sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {}
     * }
     * ```
     * @see [Sensor.TYPE_POSE_6DOF]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    internal fun pose6Dof() {
        sensorManager.getSensorList(TYPE_POSE_6DOF).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_POSE_6DOF)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Pressure sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.barometer": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_PRESSURE)": {
     *         "[0]": {
     *           "name": "Goldfish Pressure sensor",
     *           "stringType": "android.sensor.pressure",
     *           "type": 6,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "500000 (500.0 millis.)",
     *           "maximumRange": 800,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "20.0 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 1,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_PRESSURE]
     */
    internal fun pressure() {
        printFeature(PackageManager.FEATURE_SENSOR_BAROMETER)
        sensorManager.getSensorList(TYPE_PRESSURE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_PRESSURE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Proximity sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.proximity": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_PROXIMITY)": {
     *         "[0]": {
     *           "name": "Goldfish Proximity sensor",
     *           "stringType": "android.sensor.proximity",
     *           "type": 8,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 1,
     *           "minDelay": "0 (0.0 millis.)",
     *           "power": "20.0 (mA)",
     *           "reportingMode": "1 (REPORTING_MODE_ON_CHANGE)",
     *           "resolution": 1,
     *           "isWakeUpSensor": true,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_PROXIMITY]
     */
    internal fun proximity() {
        printFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)
        sensorManager.getSensorList(TYPE_PROXIMITY).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_PROXIMITY)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Relative humidity sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.relative_humidity": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_RELATIVE_HUMIDITY)": {
     *         "[0]": {
     *           "name": "Goldfish Humidity sensor",
     *           "stringType": "android.sensor.relative_humidity",
     *           "type": 12,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 100,
     *           "minDelay": "0 (0.0 millis.)",
     *           "power": "20.0 (mA)",
     *           "reportingMode": "1 (REPORTING_MODE_ON_CHANGE)",
     *           "resolution": 1,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_RELATIVE_HUMIDITY]
     */
    internal fun relativeHumidity() {
        printFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)
        sensorManager.getSensorList(TYPE_RELATIVE_HUMIDITY).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_RELATIVE_HUMIDITY)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Rotation vector sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_GAME_ROTATION_VECTOR)": {
     *         "[0]": {
     *           "name": "Game Rotation Vector Sensor",
     *           "stringType": "android.sensor.game_rotation_vector",
     *           "type": 15,
     *           "vendor": "AOSP",
     *           "version": 3,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 1,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 5.9604645E-8,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       },
     *       "getSensorList(TYPE_GEOMAGNETIC_ROTATION_VECTOR)": {
     *         "[0]": {
     *           "name": "GeoMag Rotation Vector Sensor",
     *           "stringType": "android.sensor.geomagnetic_rotation_vector",
     *           "type": 20,
     *           "vendor": "AOSP",
     *           "version": 3,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 1,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 5.9604645E-8,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       },
     *       "getSensorList(TYPE_ROTATION_VECTOR)": {
     *         "[0]": {
     *           "name": "Rotation Vector Sensor",
     *           "stringType": "android.sensor.rotation_vector",
     *           "type": 11,
     *           "vendor": "AOSP",
     *           "version": 3,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 1,
     *           "minDelay": "10000 (10.0 millis.)",
     *           "power": "12.7 (mA)",
     *           "reportingMode": "0 (REPORTING_MODE_CONTINUOUS)",
     *           "resolution": 5.9604645E-8,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_GAME_ROTATION_VECTOR]
     * @see [Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR]
     * @see [Sensor.TYPE_ROTATION_VECTOR]
     */
    internal fun rotationVector() {
        sensorManager.getSensorList(TYPE_GAME_ROTATION_VECTOR).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_GAME_ROTATION_VECTOR)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
        sensorManager.getSensorList(TYPE_GEOMAGNETIC_ROTATION_VECTOR)
            .forEachIndexed { index, sensor ->
                val sensorJson = root.getOrCreate("sensorManager")
                    .getOrCreate("getSensorList(TYPE_GEOMAGNETIC_ROTATION_VECTOR)")
                    .getOrCreate("[$index]")
                fullSensorInfo(sensorJson, sensor)
            }
        sensorManager.getSensorList(TYPE_ROTATION_VECTOR).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_ROTATION_VECTOR)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Significant motion sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {}
     * }
     * ```
     * @see [Sensor.TYPE_SIGNIFICANT_MOTION]
     */
    // TODO(Viktor) add sample to doc
    internal fun significantMotion() {
        sensorManager.getSensorList(TYPE_SIGNIFICANT_MOTION).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_SIGNIFICANT_MOTION)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Stationary detect sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {}
     * }
     * ```
     * @see [Sensor.TYPE_STATIONARY_DETECT]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    internal fun stationaryDetect() {
        sensorManager.getSensorList(TYPE_STATIONARY_DETECT).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_STATIONARY_DETECT)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Steps sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.stepcounter": false,
     *         "android.hardware.sensor.stepdetector": false
     *       }
     *     }
     *   }
     * }
     *
     * ```
     * @see [Sensor.TYPE_STEP_COUNTER]
     * @see [Sensor.TYPE_STEP_DETECTOR]
     */
    // TODO(Viktor) add sample to doc
    internal fun step() {
        printFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
        printFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
        sensorManager.getSensorList(TYPE_STEP_COUNTER).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_STEP_COUNTER)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }

        sensorManager.getSensorList(TYPE_STEP_DETECTOR).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_STEP_DETECTOR)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Temperature sensors info.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.ambient_temperature": true
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_AMBIENT_TEMPERATURE)": {
     *         "[0]": {
     *           "name": "Goldfish Ambient Temperature sensor",
     *           "stringType": "android.sensor.ambient_temperature",
     *           "type": 13,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1,
     *           "fifoMaxEventCount": 0,
     *           "fifoReservedEventCount": 0,
     *           "maxDelay": "0 (0.0 millis.)",
     *           "maximumRange": 80,
     *           "minDelay": "0 (0.0 millis.)",
     *           "power": "0.001 (mA)",
     *           "reportingMode": "1 (REPORTING_MODE_ON_CHANGE)",
     *           "resolution": 1,
     *           "isWakeUpSensor": false,
     *           "id": 0,
     *           "isAdditionalInfoSupported": false,
     *           "isDynamicSensor": false,
     *           "highestDirectReportRateLevel": "0 (RATE_STOP)"
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [Sensor.TYPE_AMBIENT_TEMPERATURE]
     * @see [Sensor.TYPE_TEMPERATURE]
     */
    internal fun temperature() {
        printFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)
        sensorManager.getSensorList(TYPE_AMBIENT_TEMPERATURE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_AMBIENT_TEMPERATURE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
        sensorManager.getSensorList(TYPE_TEMPERATURE).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_TEMPERATURE)")
                .getOrCreate("[$index]")
            fullSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Available dynamic sensors.
     *
     * Example output:
     * ```
     *{
     *   "info-sensors": {
     *     "sensorManager": {
     *       "isDynamicSensorDiscoverySupported": false
     *     }
     *   }
     * }
     * ```
     * @see [SensorManager.getDynamicSensorList]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    internal fun dynamicAvailable() {
        root.getOrCreate("sensorManager")
            .put(
                "isDynamicSensorDiscoverySupported",
                sensorManager.isDynamicSensorDiscoverySupported,
            )
        sensorManager.getDynamicSensorList(TYPE_ALL).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getDynamicSensorList(TYPE_ALL)")
                .getOrCreate("[$index]")
            shortSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Available sensors.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "sensorManager": {
     *       "getSensorList(TYPE_ALL)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Accelerometer",
     *           "stringType": "android.sensor.accelerometer",
     *           "type": 1,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[1]": {
     *           "name": "Goldfish 3-axis Gyroscope",
     *           "stringType": "android.sensor.gyroscope",
     *           "type": 4,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[2]": {
     *           "name": "Goldfish 3-axis Magnetic field sensor",
     *           "stringType": "android.sensor.magnetic_field",
     *           "type": 2,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[3]": {
     *           "name": "Goldfish Orientation sensor",
     *           "stringType": "android.sensor.orientation",
     *           "type": 3,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         }
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [SensorManager.getSensorList]
     */
    internal fun available() {
        sensorManager.getSensorList(TYPE_ALL).forEachIndexed { index, sensor ->
            val sensorJson = root.getOrCreate("sensorManager")
                .getOrCreate("getSensorList(TYPE_ALL)")
                .getOrCreate("[$index]")
            shortSensorInfo(sensorJson, sensor)
        }
    }

    /**
     * Full info about sensors.
     *
     * Example output:
     * ```
     * {
     *   "info-sensors": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.sensor.accelerometer": true,
     *         "android.hardware.sensor.ambient_temperature": true,
     *         "android.hardware.sensor.barometer": true,
     *         "android.hardware.sensor.compass": true,
     *         "android.hardware.sensor.gyroscope": true,
     *         "android.hardware.sensor.heartrate": false,
     *         "android.hardware.sensor.heartrate.ecg": false,
     *         "android.hardware.sensor.light": true,
     *         "android.hardware.sensor.proximity": true,
     *         "android.hardware.sensor.relative_humidity": true,
     *         "android.hardware.sensor.stepcounter": false,
     *         "android.hardware.sensor.stepdetector": false,
     *         "android.hardware.sensor.hinge_angle": true,
     *         "android.hardware.sensor.accelerometer_limited_axes": false,
     *         "android.hardware.sensor.accelerometer_limited_axes_uncalibrated": false,
     *         "android.hardware.sensor.dynamic.head_tracker": false,
     *         "android.hardware.sensor.gyroscope_limited_axes": false,
     *         "android.hardware.sensor.gyroscope_limited_axes_uncalibrated": false,
     *         "android.hardware.sensor.heading": false
     *       }
     *     },
     *     "sensorManager": {
     *       "getSensorList(TYPE_ALL)": {
     *         "[0]": {
     *           "name": "Goldfish 3-axis Accelerometer",
     *           "stringType": "android.sensor.accelerometer",
     *           "type": 1,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[1]": {
     *           "name": "Goldfish 3-axis Gyroscope",
     *           "stringType": "android.sensor.gyroscope",
     *           "type": 4,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[2]": {
     *           "name": "Goldfish 3-axis Magnetic field sensor",
     *           "stringType": "android.sensor.magnetic_field",
     *           "type": 2,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[3]": {
     *           "name": "Goldfish Orientation sensor",
     *           "stringType": "android.sensor.orientation",
     *           "type": 3,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         },
     *         "[4]": {
     *           "name": "Goldfish Ambient Temperature sensor",
     *           "stringType": "android.sensor.ambient_temperature",
     *           "type": 13,
     *           "vendor": "The Android Open Source Project",
     *           "version": 1
     *         }
     *       }
     *       "isDynamicSensorDiscoverySupported": false
     *     }
     *   }
     * }
     * ```
     * @see [SensorManager.getSensorList]
     * @see [SensorManager.getDynamicSensorList]
     */
    internal fun full() {
        printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER)
        printFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE)
        printFeature(PackageManager.FEATURE_SENSOR_BAROMETER)
        printFeature(PackageManager.FEATURE_SENSOR_COMPASS)
        printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)
        printFeature(PackageManager.FEATURE_SENSOR_HEART_RATE)
        printFeature(PackageManager.FEATURE_SENSOR_HEART_RATE_ECG)
        printFeature(PackageManager.FEATURE_SENSOR_LIGHT)
        printFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)
        printFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY)
        printFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)
        printFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            printFeature(PackageManager.FEATURE_SENSOR_HINGE_ANGLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES)
                printFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED)
                printFeature(PackageManager.FEATURE_SENSOR_DYNAMIC_HEAD_TRACKER)
                printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES)
                printFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE_LIMITED_AXES_UNCALIBRATED)
                printFeature(PackageManager.FEATURE_SENSOR_HEADING)
            }
        }
        available()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dynamicAvailable()
        }
    }

    private fun shortSensorInfo(sensorJson: JSONObject, sensor: Sensor) {
        sensorJson.put("name", sensor.name)
            .put("stringType", sensor.stringType)
            .put("type", sensor.type)
            .put("vendor", sensor.vendor)
            .put("version", sensor.version)
    }

    private fun fullSensorInfo(sensorJson: JSONObject, sensor: Sensor) {
        shortSensorInfo(sensorJson, sensor)
        sensorJson.put("fifoMaxEventCount", sensor.fifoMaxEventCount)
            .put("fifoReservedEventCount", sensor.fifoReservedEventCount)
            .put("maxDelay", "${sensor.maxDelay} (${(sensor.maxDelay / 1000f)} millis.)")
            .put("maximumRange", sensor.maximumRange)
            .put("minDelay", "${sensor.minDelay} (${(sensor.minDelay / 1000f)} millis.)")
            .put("power", "${sensor.power} (mA)")
            .put(
                "reportingMode",
                String.format(
                    "%s %s",
                    sensor.reportingMode,
                    reportingModeDetails(sensor.reportingMode),
                ),
            )
            .put("resolution", sensor.resolution)
            .put("isWakeUpSensor", sensor.isWakeUpSensor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sensorJson.put("id", sensor.id)
                .put("isAdditionalInfoSupported", sensor.isAdditionalInfoSupported)
                .put("isDynamicSensor", sensor.isDynamicSensor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sensorJson.put(
                    "highestDirectReportRateLevel",
                    String.format(
                        "%s %s",
                        sensor.highestDirectReportRateLevel,
                        directReportRateLevelDetails(sensor.highestDirectReportRateLevel),
                    ),
                )
            }
        }
    }

    private fun reportingModeDetails(value: Int): String {
        return when (value) {
            REPORTING_MODE_CONTINUOUS -> "(REPORTING_MODE_CONTINUOUS)"
            REPORTING_MODE_ONE_SHOT -> "(REPORTING_MODE_ONE_SHOT)"
            REPORTING_MODE_ON_CHANGE -> "(REPORTING_MODE_ON_CHANGE)"
            REPORTING_MODE_SPECIAL_TRIGGER -> "(REPORTING_MODE_SPECIAL_TRIGGER)"
            else -> ""
        }
    }

    private fun directReportRateLevelDetails(value: Int): String {
        return when (value) {
            RATE_STOP -> "(RATE_STOP)"
            RATE_NORMAL -> "(RATE_NORMAL)"
            RATE_FAST -> "(RATE_FAST)"
            RATE_VERY_FAST -> "(RATE_VERY_FAST)"
            else -> ""
        }
    }

    private fun printFeature(feature: String) {
        featuresJson.put(feature, packageManager.hasSystemFeature(feature))
    }
}
