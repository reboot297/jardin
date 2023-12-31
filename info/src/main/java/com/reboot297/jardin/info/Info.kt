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
