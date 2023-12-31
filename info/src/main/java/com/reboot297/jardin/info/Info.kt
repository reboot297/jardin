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

import android.app.GrammaticalInflectionManager
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.BatteryManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONObject

private const val TAG = "Info"

/**
 * Get info about the application and device.
 */
class Info(private val applicationContext: Context) {

    private val json = JSONObject()

    private val battery: BatteryInfo by lazy { BatteryInfo(applicationContext, json) }
    private val features: FeaturesInfo by lazy { FeaturesInfo(applicationContext, json) }
    private val locales: LocalesInfo by lazy { LocalesInfo(applicationContext, json) }
    private val sensors: SensorsInfo by lazy { SensorsInfo(applicationContext, json) }

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
     * System audio features.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.audio.low_latency": false,
     *         "android.hardware.audio.output": true,
     *         "android.hardware.audio.pro": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresAudio(): Info {
        features.audio()
        return this
    }

    /**
     * Features related to biometrics.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.fingerprint": true,
     *         "android.hardware.biometrics.face": true,
     *         "android.hardware.biometrics.iris": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresBiometrics(): Info {
        features.biometrics()
        return this
    }

    /**
     * Features related to bluetooth.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.bluetooth": true,
     *         "android.hardware.bluetooth_le": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresBluetooth(): Info {
        features.bluetooth()
        return this
    }

    /**
     * Features related to the camera.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.camera": true,
     *         "android.hardware.camera.any": true,
     *         "android.hardware.camera.autofocus": true,
     *         "android.hardware.camera.capability.manual_post_processing": true,
     *         "android.hardware.camera.capability.manual_sensor": true,
     *         "android.hardware.camera.capability.raw": true,
     *         "android.hardware.camera.external": false,
     *         "android.hardware.camera.flash": true,
     *         "android.hardware.camera.front": true,
     *         "android.hardware.camera.level.full": true,
     *         "android.hardware.camera.ar": false,
     *         "android.hardware.camera.concurrent": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresCamera(): Info {
        features.camera()
        return this
    }

    /**
     * Features related to device type.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.type.automotive": false,
     *         "android.hardware.type.television": false,
     *         "android.hardware.type.watch": false,
     *         "android.software.leanback": false,
     *         "android.software.leanback_only": false,
     *         "android.hardware.type.embedded": false,
     *         "android.hardware.type.pc": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresDeviceType(): Info {
        features.deviceType()
        return this
    }

    /**
     * Features related to openGL, Vulkan.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.opengles.aep": false,
     *         "android.hardware.vulkan.level": true,
     *         "android.hardware.vulkan.version": true,
     *         "android.hardware.vulkan.compute": true,
     *         "android.software.vulkan.deqp.level": true,
     *         "android.software.opengles.deqp.level": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresGraphics(): Info {
        features.graphics()
        return this
    }

    /**
     * Features related to the location.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.location": true,
     *         "android.hardware.location.gps": true,
     *         "android.hardware.location.network": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresLocation(): Info {
        features.location()
        return this
    }

    /**
     * Features related to the NFC.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.nfc": false,
     *         "android.hardware.nfc.hce": false,
     *         "android.hardware.nfc.hcef": false,
     *         "android.sofware.nfc.beam": false,
     *         "android.hardware.nfc.ese": false,
     *         "android.hardware.nfc.uicc": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresNFC(): Info {
        features.nfc()
        return this
    }

    /**
     * Features related to the sensors.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
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
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresSensors(): Info {
        features.sensors()
        return this
    }

    /**
     * Features related to telephony.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.telephony": true,
     *         "android.hardware.telephony.cdma": false,
     *         "android.hardware.telephony.gsm": true,
     *         "android.hardware.telephony.euicc": false,
     *         "android.hardware.telephony.mbms": false,
     *         "android.hardware.telephony.ims": true,
     *         "android.hardware.telephony.calling": true,
     *         "android.hardware.telephony.data": true,
     *         "android.hardware.telephony.euicc.mep": false,
     *         "android.hardware.telephony.messaging": false,
     *         "android.hardware.telephony.radio.access": true,
     *         "android.hardware.telephony.subscription": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresTelephony(): Info {
        features.telephony()
        return this
    }

    /**
     * Features related to touch and faketouch.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.faketouch": true,
     *         "android.hardware.faketouch.multitouch.distinct": false,
     *         "android.hardware.faketouch.multitouch.jazzhand": false,
     *         "android.hardware.touchscreen": true,
     *         "android.hardware.touchscreen.multitouch": true,
     *         "android.hardware.touchscreen.multitouch.distinct": true,
     *         "android.hardware.touchscreen.multitouch.jazzhand": true
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresTouch(): Info {
        features.touch()
        return this
    }

    /**
     * Features related to VR
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.software.vr.mode": false,
     *         "android.hardware.vr.high_performance": false,
     *         "android.hardware.vr.headtracking": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun featuresVR(): Info {
        features.vr()
        return this
    }

    /**
     * Features related to WiIFI.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.wifi": true,
     *         "android.hardware.wifi.direct": true,
     *         "android.hardware.wifi.aware": false,
     *         "android.hardware.wifi.passpoint": true,
     *         "android.hardware.wifi.rtt": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun featuresWifi(): Info {
        features.wifi()
        return this
    }

    /**
     * All features.
     *
     * Example output:
     * ```
     * {
     *   "info-features": {
     *     "packageManager": {
     *       "hasSystemFeature": {
     *         "android.hardware.audio.low_latency": false,
     *         "android.hardware.audio.output": true,
     *         "android.hardware.audio.pro": false,
     *         "android.hardware.fingerprint": true,
     *         "android.hardware.biometrics.face": true,
     *         "android.hardware.biometrics.iris": false,
     *         "android.hardware.bluetooth": true,
     *         "android.hardware.bluetooth_le": true,
     *         "android.hardware.camera": true,
     *         "android.hardware.camera.any": true,
     *         "android.hardware.camera.autofocus": true,
     *         "android.hardware.camera.capability.manual_post_processing": true,
     *         "android.hardware.camera.capability.manual_sensor": true,
     *         "android.hardware.camera.capability.raw": true,
     *         "android.hardware.camera.external": false,
     *         "android.hardware.camera.flash": true,
     *         "android.hardware.camera.front": true,
     *         "android.hardware.camera.level.full": true,
     *         "android.hardware.camera.ar": false,
     *         "android.hardware.camera.concurrent": true,
     *         "android.hardware.opengles.aep": false,
     *         "android.hardware.vulkan.level": true,
     *         "android.hardware.vulkan.version": true,
     *         "android.hardware.vulkan.compute": true,
     *         "android.software.vulkan.deqp.level": true,
     *         "android.software.opengles.deqp.level": true,
     *         "android.hardware.type.automotive": false,
     *         "android.hardware.type.television": false,
     *         "android.hardware.type.watch": false,
     *         "android.software.leanback": false,
     *         "android.software.leanback_only": false,
     *         "android.hardware.type.embedded": false,
     *         "android.hardware.type.pc": false,
     *         "android.hardware.location": true,
     *         "android.hardware.location.gps": true,
     *         "android.hardware.location.network": true,
     *         "android.hardware.nfc": false,
     *         "android.hardware.nfc.hce": false,
     *         "android.hardware.nfc.hcef": false,
     *         "android.sofware.nfc.beam": false,
     *         "android.hardware.nfc.ese": false,
     *         "android.hardware.nfc.uicc": false,
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
     *         "android.hardware.sensor.heading": false,
     *         "android.hardware.telephony": true,
     *         "android.hardware.telephony.cdma": false,
     *         "android.hardware.telephony.gsm": true,
     *         "android.hardware.telephony.euicc": false,
     *         "android.hardware.telephony.mbms": false,
     *         "android.hardware.telephony.ims": true,
     *         "android.hardware.telephony.calling": true,
     *         "android.hardware.telephony.data": true,
     *         "android.hardware.telephony.euicc.mep": false,
     *         "android.hardware.telephony.messaging": false,
     *         "android.hardware.telephony.radio.access": true,
     *         "android.hardware.telephony.subscription": true,
     *         "android.hardware.faketouch": true,
     *         "android.hardware.faketouch.multitouch.distinct": false,
     *         "android.hardware.faketouch.multitouch.jazzhand": false,
     *         "android.hardware.touchscreen": true,
     *         "android.hardware.touchscreen.multitouch": true,
     *         "android.hardware.touchscreen.multitouch.distinct": true,
     *         "android.hardware.touchscreen.multitouch.jazzhand": true,
     *         "android.software.vr.mode": false,
     *         "android.hardware.vr.high_performance": false,
     *         "android.hardware.vr.headtracking": false,
     *         "android.hardware.wifi": true,
     *         "android.hardware.wifi.direct": true,
     *         "android.hardware.wifi.aware": false,
     *         "android.hardware.wifi.passpoint": true,
     *         "android.hardware.wifi.rtt": false,
     *         "android.software.app_widgets": true,
     *         "android.software.backup": true,
     *         "android.software.connectionservice": false,
     *         "android.hardware.consumerir": false,
     *         "android.software.device_admin": true,
     *         "android.hardware.gamepad": false,
     *         "android.hardware.sensor.hifi_sensors": false,
     *         "android.software.home_screen": true,
     *         "android.software.input_methods": true,
     *         "android.software.live_tv": false,
     *         "android.software.live_wallpaper": true,
     *         "android.software.managed_users": true,
     *         "android.hardware.microphone": true,
     *         "android.software.midi": true,
     *         "android.software.print": true,
     *         "android.hardware.screen.landscape": true,
     *         "android.hardware.screen.portrait": true,
     *         "android.software.securely_removes_users": true,
     *         "android.software.sip": false,
     *         "android.software.sip.voip": false,
     *         "android.hardware.usb.accessory": false,
     *         "android.hardware.usb.host": false,
     *         "android.software.verified_boot": true,
     *         "android.software.webview": true,
     *         "android.hardware.ethernet": false,
     *         "android.software.freeform_window_management": false,
     *         "android.software.picture_in_picture": true,
     *         "android.software.activities_on_secondary_displays": true,
     *         "android.software.autofill": true,
     *         "android.software.companion_device_setup": true,
     *         "android.hardware.ram.low": false,
     *         "android.hardware.ram.normal": true,
     *         "android.software.cant_save_state": true,
     *         "android.hardware.strongbox_keystore": false,
     *         "android.software.ipsec_tunnels": true,
     *         "android.software.secure_lock_screen": true,
     *         "android.software.controls": true,
     *         "android.hardware.se.omapi.ese": false,
     *         "android.hardware.se.omapi.sd": false,
     *         "android.hardware.se.omapi.uicc": false,
     *         "android.hardware.hardware_keystore": true,
     *         "android.hardware.identity_credential": true,
     *         "android.hardware.identity_credential_direct_access": false,
     *         "android.hardware.keystore.app_attest_key": true,
     *         "android.hardware.keystore.limited_use_key": false,
     *         "android.hardware.keystore.single_use_key": false,
     *         "android.hardware.security.model.compatible": true,
     *         "android.software.expanded_picture_in_picture": false,
     *         "android.software.window_magnification": true,
     *         "android.software.telecom": true,
     *         "android.software.credentials": true,
     *         "android.software.device_lock": true,
     *         "android.software.ipsec_tunnel_migration": true,
     *         "android.hardware.uwb": false,
     *         "android.software.wallet_location_based_suggestions": false
     *       }
     *     }
     *   }
     * }
     * ```
     * @see [PackageManager.hasSystemFeature]
     * @return instance of [Info] object
     */
    fun features(): Info {
        features.full()
        return this
    }

    /**
     * List of locales available in the application.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "localeManager": {
     *       "applicationLocales": "[es_ES]"
     *     }
     *   }
     * }
     * ```
     * @return reference to [Info] object
     * @see [LocaleManager.getApplicationLocales]
     * @return instance of [Info] object
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun localesApplication(): Info {
        locales.application()
        return this
    }

    /**
     * Grammatical gender.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "grammaticalInflectionManager": {
     *       "applicationGrammaticalGender": "0 (not specified)"
     *     },
     *     "configuration": {
     *       "grammaticalGender": "0 (not specified)"
     *     }
     *   }
     * }
     * ```
     * @return reference to [Info] object
     * @see [Configuration.getGrammaticalGender]
     * @see [GrammaticalInflectionManager.getApplicationGrammaticalGender]
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun localesGrammaticalGender(): Info {
        locales.grammaticalGender()
        return this
    }

    /**
     * System locals.
     *
     * Example output:
     * ```
     * {
     *   "info-locales": {
     *     "configuration": {
     *       "locale": "es_ES",
     *       "locales": "[es_ES,en_US]"
     *     },
     *     "localeManager": {
     *       "systemLocales": "[es_ES,en_US]"
     *     }
     *   }
     * }
     * ```
     * @return reference to [Info] object
     * @see [Configuration.getLocales]
     * @see [Configuration.locale]
     * @see [LocaleManager.getSystemLocales]
     */
    fun localesSystem(): Info {
        locales.system()
        return this
    }

    /**
     * Get All information related to locales.
     *
     * Example output for API 34:
     * ```
     * {
     *   "info-locales": {
     *     "configuration": {
     *       "locale": "es_ES",
     *       "locales": "[es_ES,en_US]",
     *       "grammaticalGender": "0 (not specified)"
     *     },
     *     "localeManager": {
     *       "systemLocales": "[es_ES,en_US]",
     *       "applicationLocales": "[]"
     *     },
     *     "grammaticalInflectionManager": {
     *       "applicationGrammaticalGender": "0 (not specified)"
     *     }
     *   }
     * }
     * ```
     * @return reference to [Info] object
     * @see [Configuration.getLocales]
     * @see [Configuration.locale]
     * @see [LocaleManager.getApplicationLocales]
     * @see [LocaleManager.getSystemLocales]
     * @see [Configuration.getGrammaticalGender]
     * @see [GrammaticalInflectionManager.getApplicationGrammaticalGender]
     */
    fun locales(): Info {
        locales.full()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_ACCELEROMETER]
     * @see [Sensor.TYPE_ACCELEROMETER_UNCALIBRATED]
     * @see [Sensor.TYPE_ACCELEROMETER_LIMITED_AXES]
     * @see [Sensor.TYPE_ACCELEROMETER_LIMITED_AXES_UNCALIBRATED]
     */
    fun sensorsAccelerometer(): Info {
        sensors.accelerometer()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_GRAVITY]
     */
    fun sensorsGravity(): Info {
        sensors.gravity()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_GYROSCOPE]
     * @see [Sensor.TYPE_GYROSCOPE_UNCALIBRATED]
     * @see [Sensor.TYPE_GYROSCOPE_LIMITED_AXES]
     * @see [Sensor.TYPE_GYROSCOPE_LIMITED_AXES_UNCALIBRATED]
     */
    fun sensorsGyroscope(): Info {
        sensors.gyroscope()
        return this
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
     *
     * ```
     * @return reference to [Info] object
     * @see [Sensor.TYPE_HEADING]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sensorsHeading(): Info {
        sensors.heading()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_HEAD_TRACKER]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun sensorsHeadTracker(): Info {
        sensors.headTracker()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_HEART_RATE]
     * @see [Sensor.TYPE_HEART_BEAT]
     */
    // TODO(Viktor) add sample to doc.
    fun sensorsHeart(): Info {
        sensors.heart()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_HINGE_ANGLE]
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun sensorsHingeAngle(): Info {
        sensors.hingeAngle()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_LIGHT]
     */
    fun sensorsLight(): Info {
        sensors.light()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_LINEAR_ACCELERATION]
     */
    fun sensorsLinearAcceleration(): Info {
        sensors.linearAcceleration()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.O)
    fun sensorsLowLatencyOffBodyDetect(): Info {
        sensors.lowLatencyOffBodyDetect()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_MAGNETIC_FIELD]
     * @see [Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED]
     */
    fun sensorsMagneticField(): Info {
        sensors.magneticField()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_MOTION_DETECT]
     */
    // TODO(Viktor) add sample to doc.
    @RequiresApi(Build.VERSION_CODES.N)
    fun sensorsMotionDetect(): Info {
        sensors.motionDetect()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_ORIENTATION]
     */
    fun sensorsOrientation(): Info {
        sensors.orientation()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_POSE_6DOF]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    fun sensorsPOSE6DOF(): Info {
        sensors.pose6Dof()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_PRESSURE]
     */
    fun sensorsPressure(): Info {
        sensors.pressure()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_PROXIMITY]
     */
    fun sensorsProximity(): Info {
        sensors.proximity()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_RELATIVE_HUMIDITY]
     */
    fun sensorsRelativeHumidity(): Info {
        sensors.relativeHumidity()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_GAME_ROTATION_VECTOR]
     * @see [Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR]
     * @see [Sensor.TYPE_ROTATION_VECTOR]
     */
    fun sensorsRotationVector(): Info {
        sensors.rotationVector()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_SIGNIFICANT_MOTION]
     */
    // TODO(Viktor) add sample to doc
    fun sensorsSignificantMotion(): Info {
        sensors.significantMotion()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_STATIONARY_DETECT]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    fun sensorsStationaryDetect(): Info {
        sensors.stationaryDetect()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_STEP_COUNTER]
     * @see [Sensor.TYPE_STEP_DETECTOR]
     */
    // TODO(Viktor) add sample to doc
    fun sensorsSteps(): Info {
        sensors.step()
        return this
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
     * @return reference to [Info] object
     * @see [Sensor.TYPE_AMBIENT_TEMPERATURE]
     * @see [Sensor.TYPE_TEMPERATURE]
     */
    fun sensorsTemperature(): Info {
        sensors.temperature()
        return this
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
     * @return reference to [Info] object
     * @see [SensorManager.getDynamicSensorList]
     */
    // TODO(Viktor) add sample to doc
    @RequiresApi(Build.VERSION_CODES.N)
    fun sensorsDynamic(): Info {
        sensors.dynamicAvailable()
        return this
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
     * @return reference to [Info] object
     * @see [SensorManager.getSensorList]
     */
    fun sensorsAvailable(): Info {
        sensors.available()
        return this
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
     * @return reference to [Info] object
     * @see [SensorManager.getSensorList]
     * @see [SensorManager.getDynamicSensorList]
     */
    fun sensors(): Info {
        sensors.full()
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
