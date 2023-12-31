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
import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject

/**
 * Supported features info.
 */
internal class FeaturesInfo internal constructor(
    context: Context,
    json: JSONObject,
) : Base(context) {

    private val root = json.getOrCreate("info-features")
    private val packageManager = context.packageManager
    private val featuresJson = root.getOrCreate("packageManager")
        .getOrCreate("hasSystemFeature")

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
     */
    internal fun audio() {
        printFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY)
        printFeature(PackageManager.FEATURE_AUDIO_OUTPUT)
        printFeature(PackageManager.FEATURE_AUDIO_PRO)
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
     */
    internal fun biometrics() {
        printFeature(PackageManager.FEATURE_FINGERPRINT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            printFeature(PackageManager.FEATURE_FACE)
            printFeature(PackageManager.FEATURE_IRIS)
        }
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
     */
    internal fun bluetooth() {
        printFeature(PackageManager.FEATURE_BLUETOOTH)
        printFeature(PackageManager.FEATURE_BLUETOOTH_LE)
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
     */
    internal fun camera() {
        printFeature(PackageManager.FEATURE_CAMERA)
        printFeature(PackageManager.FEATURE_CAMERA_ANY)
        printFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
        printFeature(PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_POST_PROCESSING)
        printFeature(PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR)
        printFeature(PackageManager.FEATURE_CAMERA_CAPABILITY_RAW)
        printFeature(PackageManager.FEATURE_CAMERA_EXTERNAL)
        printFeature(PackageManager.FEATURE_CAMERA_FLASH)
        printFeature(PackageManager.FEATURE_CAMERA_FRONT)
        printFeature(PackageManager.FEATURE_CAMERA_LEVEL_FULL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            printFeature(PackageManager.FEATURE_CAMERA_AR)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                printFeature(PackageManager.FEATURE_CAMERA_CONCURRENT)
            }
        }
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
     */
    internal fun deviceType() {
        printFeature(PackageManager.FEATURE_AUTOMOTIVE)
        @Suppress("DEPRECATION")
        printFeature(PackageManager.FEATURE_TELEVISION)
        printFeature(PackageManager.FEATURE_WATCH)
        printFeature(PackageManager.FEATURE_LEANBACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            printFeature(PackageManager.FEATURE_LEANBACK_ONLY)
            printFeature(PackageManager.FEATURE_EMBEDDED)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                printFeature(PackageManager.FEATURE_PC)
            }
        }
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
     */
    internal fun graphics() {
        printFeature(PackageManager.FEATURE_OPENGLES_EXTENSION_PACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            printFeature(PackageManager.FEATURE_VULKAN_HARDWARE_LEVEL)
            printFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                printFeature(PackageManager.FEATURE_VULKAN_HARDWARE_COMPUTE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    printFeature(PackageManager.FEATURE_VULKAN_DEQP_LEVEL)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        printFeature(PackageManager.FEATURE_OPENGLES_DEQP_LEVEL)
                    }
                }
            }
        }
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
     */
    internal fun location() {
        printFeature(PackageManager.FEATURE_LOCATION)
        printFeature(PackageManager.FEATURE_LOCATION_GPS)
        printFeature(PackageManager.FEATURE_LOCATION_NETWORK)
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
     */
    internal fun nfc() {
        printFeature(PackageManager.FEATURE_NFC)
        printFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            printFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION_NFCF)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                printFeature(PackageManager.FEATURE_NFC_BEAM)
                printFeature(PackageManager.FEATURE_NFC_OFF_HOST_CARD_EMULATION_ESE)
                printFeature(PackageManager.FEATURE_NFC_OFF_HOST_CARD_EMULATION_UICC)
            }
        }
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
     */
    internal fun sensors() {
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
     */
    internal fun telephony() {
        printFeature(PackageManager.FEATURE_TELEPHONY)
        printFeature(PackageManager.FEATURE_TELEPHONY_CDMA)
        printFeature(PackageManager.FEATURE_TELEPHONY_GSM)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            printFeature(PackageManager.FEATURE_TELEPHONY_EUICC)
            printFeature(PackageManager.FEATURE_TELEPHONY_MBMS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                printFeature(PackageManager.FEATURE_TELEPHONY_IMS)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    printFeature(PackageManager.FEATURE_TELEPHONY_CALLING)
                    printFeature(PackageManager.FEATURE_TELEPHONY_DATA)
                    printFeature(PackageManager.FEATURE_TELEPHONY_EUICC_MEP)
                    printFeature(PackageManager.FEATURE_TELEPHONY_MESSAGING)
                    printFeature(PackageManager.FEATURE_TELEPHONY_RADIO_ACCESS)
                    printFeature(PackageManager.FEATURE_TELEPHONY_SUBSCRIPTION)
                }
            }
        }
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
     */
    internal fun touch() {
        printFeature(PackageManager.FEATURE_FAKETOUCH)
        printFeature(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_DISTINCT)
        printFeature(PackageManager.FEATURE_FAKETOUCH_MULTITOUCH_JAZZHAND)
        printFeature(PackageManager.FEATURE_TOUCHSCREEN)
        printFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)
        printFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)
        printFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_JAZZHAND)
    }

    /**
     * Features related to VR.
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
     */
    @RequiresApi(Build.VERSION_CODES.N)
    internal fun vr() {
        @Suppress("DEPRECATION")
        printFeature(PackageManager.FEATURE_VR_MODE)
        printFeature(PackageManager.FEATURE_VR_MODE_HIGH_PERFORMANCE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            printFeature(PackageManager.FEATURE_VR_HEADTRACKING)
        }
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
     */
    internal fun wifi() {
        printFeature(PackageManager.FEATURE_WIFI)
        printFeature(PackageManager.FEATURE_WIFI_DIRECT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            printFeature(PackageManager.FEATURE_WIFI_AWARE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                printFeature(PackageManager.FEATURE_WIFI_PASSPOINT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    printFeature(PackageManager.FEATURE_WIFI_RTT)
                }
            }
        }
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
     */
    internal fun full() {
        audio()
        biometrics()
        bluetooth()
        camera()
        graphics()
        deviceType()
        location()
        nfc()
        sensors()
        telephony()
        touch()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            vr()
        }
        wifi()

        printFeature(PackageManager.FEATURE_APP_WIDGETS)
        printFeature(PackageManager.FEATURE_BACKUP)
        @Suppress("DEPRECATION")
        printFeature(PackageManager.FEATURE_CONNECTION_SERVICE)
        printFeature(PackageManager.FEATURE_CONSUMER_IR)
        printFeature(PackageManager.FEATURE_DEVICE_ADMIN)
        printFeature(PackageManager.FEATURE_GAMEPAD)
        printFeature(PackageManager.FEATURE_HIFI_SENSORS)
        printFeature(PackageManager.FEATURE_HOME_SCREEN)
        printFeature(PackageManager.FEATURE_INPUT_METHODS)
        printFeature(PackageManager.FEATURE_LIVE_TV)
        printFeature(PackageManager.FEATURE_LIVE_WALLPAPER)
        printFeature(PackageManager.FEATURE_MANAGED_USERS)
        printFeature(PackageManager.FEATURE_MICROPHONE)
        printFeature(PackageManager.FEATURE_MIDI)
        printFeature(PackageManager.FEATURE_PRINTING)
        printFeature(PackageManager.FEATURE_SCREEN_LANDSCAPE)
        printFeature(PackageManager.FEATURE_SCREEN_PORTRAIT)
        printFeature(PackageManager.FEATURE_SECURELY_REMOVES_USERS)
        printFeature(PackageManager.FEATURE_SIP)
        printFeature(PackageManager.FEATURE_SIP_VOIP)
        printFeature(PackageManager.FEATURE_USB_ACCESSORY)
        printFeature(PackageManager.FEATURE_USB_HOST)
        printFeature(PackageManager.FEATURE_VERIFIED_BOOT)
        printFeature(PackageManager.FEATURE_WEBVIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            printFeature(PackageManager.FEATURE_ETHERNET)
            printFeature(PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT)
            printFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                printFeature(PackageManager.FEATURE_ACTIVITIES_ON_SECONDARY_DISPLAYS)
                printFeature(PackageManager.FEATURE_AUTOFILL)
                printFeature(PackageManager.FEATURE_COMPANION_DEVICE_SETUP)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    printFeature(PackageManager.FEATURE_RAM_LOW)
                    printFeature(PackageManager.FEATURE_RAM_NORMAL)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        printFeature(PackageManager.FEATURE_CANT_SAVE_STATE)
                        printFeature(PackageManager.FEATURE_STRONGBOX_KEYSTORE)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            printFeature(PackageManager.FEATURE_IPSEC_TUNNELS)
                            printFeature(PackageManager.FEATURE_SECURE_LOCK_SCREEN)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                printFeature(PackageManager.FEATURE_CONTROLS)
                                printFeature(PackageManager.FEATURE_SE_OMAPI_ESE)
                                printFeature(PackageManager.FEATURE_SE_OMAPI_SD)
                                printFeature(PackageManager.FEATURE_SE_OMAPI_UICC)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    printFeature(PackageManager.FEATURE_HARDWARE_KEYSTORE)
                                    printFeature(PackageManager.FEATURE_IDENTITY_CREDENTIAL_HARDWARE)
                                    printFeature(PackageManager.FEATURE_IDENTITY_CREDENTIAL_HARDWARE_DIRECT_ACCESS)
                                    printFeature(PackageManager.FEATURE_KEYSTORE_APP_ATTEST_KEY)
                                    printFeature(PackageManager.FEATURE_KEYSTORE_LIMITED_USE_KEY)
                                    printFeature(PackageManager.FEATURE_KEYSTORE_SINGLE_USE_KEY)
                                    printFeature(PackageManager.FEATURE_SECURITY_MODEL_COMPATIBLE)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        printFeature(PackageManager.FEATURE_EXPANDED_PICTURE_IN_PICTURE)
                                        printFeature(PackageManager.FEATURE_WINDOW_MAGNIFICATION)
                                        printFeature(PackageManager.FEATURE_TELECOM)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                            printFeature(PackageManager.FEATURE_CREDENTIALS)
                                            printFeature(PackageManager.FEATURE_DEVICE_LOCK)
                                            printFeature(PackageManager.FEATURE_IPSEC_TUNNEL_MIGRATION)
                                            printFeature(PackageManager.FEATURE_UWB)
                                            printFeature(PackageManager.FEATURE_WALLET_LOCATION_BASED_SUGGESTIONS)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun printFeature(feature: String) {
        featuresJson.put(feature, packageManager.hasSystemFeature(feature))
    }
}
