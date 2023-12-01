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

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.format.Formatter
import androidx.core.content.ContextCompat

/**
 * Info about bluetooth.
 */
class Bluetooth internal constructor(
    private val context: Context,
    private val builder: StringBuilder,
    private val info: Info,
) : Base(context) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    init {
        builder.append("--------------------BluetoothInfo-------------------").append("\n")
    }

    /**
     * BriefInfo about bluetooth state.
     */
    fun briefInfo(): Info {
        supportFeature()
        isEnabled()
        address()
        isDiscovering()
        return build()
    }

    /**
     * Full info about bluetooth state.
     */
    fun fullInfo(): Info {
        supportFeature()
        isEnabled()
        name()
        address()

        discoverableTimeout()
        isDiscovering()

        state()
        scanMode()
        isLeAudioSupported()
        isLeAudioBroadcastSourceSupported()
        isLeAudioBroadcastAssistantSupported()
        maxConnectedAudioDevices()

        isLe2MPhySupported()
        isLeCodedPhySupported()
        isLeExtendedAdvertisingSupported()
        leMaximumAdvertisingDataLength()
        isLePeriodicAdvertisingSupported()
        isMultipleAdvertisementSupported()

        isOffloadedFilteringSupported()
        isOffloadedScanBatchingSupported()

        bondedDevices()
        return build()
    }

    /**
     * Capability to communicate with other devices via Bluetooth or vai Bluetooth Low Energy radio.
     */
    fun supportFeature(): Bluetooth {
        builder.append("isBluetoothSupported: ")
            .append(context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            .append("\n")
            .append("isBluetoothLESupported: ")
            .append(context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            .append("\n")
        return this
    }

    /**
     * True if Bluetooth is currently enabled and ready for use.
     * Equivalent to: getBluetoothState() == STATE_ON
     */
    fun isEnabled(): Bluetooth {
        val enabled = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.isEnabled ?: false
            }
        } else {
            bluetoothAdapter?.isEnabled ?: false
        }

        builder.append("adapter.isEnabled: ")
            .append(enabled)
            .append("\n")
        return this
    }


    /**
     * The friendly Bluetooth name of the local Bluetooth adapter.
     * This name is visible to remote Bluetooth devices.
     */

    @SuppressLint("MissingPermission")
    fun name(): Bluetooth {
        val name = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(Manifest.permission.BLUETOOTH_CONNECT) ?: let {
                bluetoothAdapter?.name.toString()
            }
        } else {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.name.toString()
            }
        }

        builder.append("adapter.name: ")
            .append(name)
            .append("\n")
        return this
    }

    /**
     * The hardware address of the local Bluetooth adapter.
     * For example, "00:11:22:AA:BB:CC".
     */
    @SuppressLint("HardwareIds")
    fun address(): Bluetooth {
        val address = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(Manifest.permission.BLUETOOTH_CONNECT) ?: let {
                bluetoothAdapter?.address.toString()
            }
        } else {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.address.toString()
            }
        }

        builder.append("adapter.address: ")
            .append(address)
            .append("\n")

        return this
    }

    /**
     * The timeout duration of the SCAN_MODE_CONNECTABLE_DISCOVERABLE.
     */
    @SuppressLint("MissingPermission")
    fun discoverableTimeout(): Bluetooth {//TODO check this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val timeout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                checkPermission(Manifest.permission.BLUETOOTH_SCAN) ?: let {
                    bluetoothAdapter?.discoverableTimeout?.toString()
                }
            } else {
                bluetoothAdapter?.discoverableTimeout
            }

            builder.append("adapter.discoverableTimeOut: ")
                .append(timeout)
                .append("\n")
        }
        return this
    }

    /**
     * Check if the local Bluetooth adapter is currently in the device discovery process.
     */
    @SuppressLint("MissingPermission")
    fun isDiscovering(): Bluetooth {
        val isDiscovering = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(Manifest.permission.BLUETOOTH_SCAN) ?: let {
                bluetoothAdapter?.isDiscovering.toString()
            }
        } else {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.isDiscovering.toString()
            }
        }

        builder.append("adapter.isDiscovering: ")
            .append(isDiscovering)
            .append("\n")
        return this
    }

    /**
     * The current state of the local Bluetooth adapter.
     */
    fun state(): Bluetooth {
        val state = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.state
            }
        } else {
            bluetoothAdapter?.state
        }

        val details = when (state) {
            is String -> state
            BluetoothAdapter.STATE_ON -> "on"
            BluetoothAdapter.STATE_OFF -> "off"
            BluetoothAdapter.STATE_TURNING_ON -> "turning On"
            BluetoothAdapter.STATE_TURNING_OFF -> "turning Off"
            else -> "Unknown"
        }

        builder.append("adapter.getState: ")
            .append(state)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * Current Bluetooth scan mode of the local Bluetooth adapter.
     * The Bluetooth scan mode determines if the local adapter is connectable and/or discoverable
     * from remote Bluetooth devices.
     */
    @SuppressLint("MissingPermission")
    fun scanMode(): Bluetooth {
        val scanMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkPermission(Manifest.permission.BLUETOOTH_SCAN) ?: let {
                bluetoothAdapter?.scanMode
            }
        } else {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.scanMode
            }
        }

        val details = when (scanMode) {
            is String -> ""
            BluetoothAdapter.SCAN_MODE_NONE -> "none"
            BluetoothAdapter.SCAN_MODE_CONNECTABLE -> "connectable"
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> "connectable-discoverable"
            else -> "unknown"
        }

        builder.append("adapter.scanMode: ")
            .append(scanMode)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * Check if LE Audio supported.
     */
    @SuppressLint("NewApi")
    fun isLeAudioSupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.TIRAMISU) ?: let {
            bluetoothAdapter?.isLeAudioSupported
        }

        val details = when (value) {
            is String -> ""
            BluetoothStatusCodes.FEATURE_SUPPORTED -> "feature supported"
            BluetoothStatusCodes.FEATURE_NOT_SUPPORTED -> "feature not supported"
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> "bluetooth not enabled"
            else -> "unknown"
        }

        builder.append("adapter.isLeAudioSupported: ")
            .append(value)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * Check if the LE audio broadcast assistant feature is supported.
     */
    @SuppressLint("NewApi")
    fun isLeAudioBroadcastAssistantSupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.TIRAMISU) ?: let {
            bluetoothAdapter?.isLeAudioBroadcastAssistantSupported
        }

        val details = when (value) {
            is String -> ""
            BluetoothStatusCodes.FEATURE_SUPPORTED -> "feature supported"
            BluetoothStatusCodes.FEATURE_NOT_SUPPORTED -> "feature not supported"
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> "bluetooth not enabled"
            else -> "unknown"
        }

        builder.append("adapter.isLeAudioBroadcastAssistantSupported: ")
            .append(value)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * Check if the LE audio broadcast source feature is supported.
     */
    @SuppressLint("NewApi")
    fun isLeAudioBroadcastSourceSupported(): Bluetooth {
        val value: Any? = checkMinSdkVersion(Build.VERSION_CODES.TIRAMISU) ?: let {
            bluetoothAdapter?.isLeAudioBroadcastSourceSupported
        }

        val details = when (value) {
            is String? -> ""
            BluetoothStatusCodes.FEATURE_SUPPORTED -> "feature supported"
            BluetoothStatusCodes.FEATURE_NOT_SUPPORTED -> "feature not supported"
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> "bluetooth not enabled"
            else -> "unknown"
        }

        builder.append("adapter.isLeAudioBroadcastSourceSupported: ")
            .append(value)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * The maximum number of connected devices per audio profile for this device.
     */
    @SuppressLint("MissingPermission", "NewApi")
    fun maxConnectedAudioDevices(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.TIRAMISU) ?: let {
            checkPermission(Manifest.permission.BLUETOOTH_CONNECT) ?: let {
                bluetoothAdapter?.maxConnectedAudioDevices
            }
        }

        builder.append("adapter.maxConnectedAudioDevices: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * Check if LE 2M PHY feature is supported
     */
    @SuppressLint("NewApi")
    fun isLe2MPhySupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.O) ?: let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                    bluetoothAdapter?.isLe2MPhySupported
                }
            } else {
                bluetoothAdapter?.isLe2MPhySupported
            }
        }

        builder.append("adapter.isLe2MPhySupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * Check if LE Coded PHY feature is supported.
     */
    @SuppressLint("NewApi")
    fun isLeCodedPhySupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.O) ?: let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                    bluetoothAdapter?.isLeCodedPhySupported
                }
            } else {
                bluetoothAdapter?.isLeCodedPhySupported
            }
        }

        builder.append("adapter.isLeCodedPhySupported: ")
            .append(value)
            .append("\n")
        return this
    }


    /**
     * Check if LE Extended Advertising feature is supported.
     */
    @SuppressLint("NewApi")
    fun isLeExtendedAdvertisingSupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.O) ?: let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                    bluetoothAdapter?.isLeExtendedAdvertisingSupported
                }
            } else {
                bluetoothAdapter?.isLeExtendedAdvertisingSupported
            }
        }

        builder.append("adapter.isLeExtendedAdvertisingSupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * The maximum LE advertising data length in bytes, if LE Extended Advertising feature is supported.
     */
    @SuppressLint("NewApi")
    fun leMaximumAdvertisingDataLength(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.O) ?: let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                    bluetoothAdapter?.leMaximumAdvertisingDataLength
                }
            } else {
                bluetoothAdapter?.leMaximumAdvertisingDataLength
            }
        }

        val details = if (value is Int) {
            Formatter.formatFileSize(context, value.toLong())
        } else {
            ""
        }
        builder.append("adapter.leMaximumAdvertisingDataLength: ")
            .append(value)
            .append("($details)")
            .append("\n")
        return this
    }

    /**
     * Check if LE Periodic Advertising feature is supported.
     */
    @SuppressLint("NewApi")
    fun isLePeriodicAdvertisingSupported(): Bluetooth {
        val value = checkMinSdkVersion(Build.VERSION_CODES.O) ?: let {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                    bluetoothAdapter?.isLePeriodicAdvertisingSupported
                }
            } else {
                bluetoothAdapter?.isLePeriodicAdvertisingSupported
            }
        }

        builder.append("adapter.isLePeriodicAdvertisingSupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * Check if the multi advertisement is supported by the chipset.
     */
    fun isMultipleAdvertisementSupported(): Bluetooth {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.isMultipleAdvertisementSupported
            }
        } else {
            bluetoothAdapter?.isMultipleAdvertisementSupported
        }

        builder.append("adapter.isMultipleAdvertisementSupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * Check if offloaded scan batching is supported.
     */
    fun isOffloadedScanBatchingSupported(): Bluetooth {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.isOffloadedScanBatchingSupported
            }
        } else {
            bluetoothAdapter?.isOffloadedScanBatchingSupported
        }

        builder.append("adapter.isOffloadedScanBatchingSupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * Check if chipset supports on-chip filtering.
     */
    fun isOffloadedFilteringSupported(): Bluetooth {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            checkPermission(Manifest.permission.BLUETOOTH) ?: let {
                bluetoothAdapter?.isOffloadedFilteringSupported
            }
        } else {
            bluetoothAdapter?.isOffloadedFilteringSupported
        }

        builder.append("adapter.isOffloadedFilteringSupported: ")
            .append(value)
            .append("\n")
        return this
    }

    /**
     * The set of BluetoothDevice objects that are bonded (paired) to the local adapter.
     */
    @SuppressLint("MissingPermission")
    fun bondedDevices(): Bluetooth {
        val bondedDevices: Set<BluetoothDevice>? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.bondedDevices
                } else {
                    builder.append("adapter.bonded-devices: Manifest.permission.BLUETOOTH_CONNECT is disabled")
                        .append("\n")
                    return this
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.bondedDevices
                } else {
                    builder.append("adapter.bonded-devices: Manifest.permission.BLUETOOTH is disabled")
                        .append("\n")
                    return this
                }
            }


        builder.append("adapter.bounded-devices: ")
            .append("\n")
            .append("----------")
            .append("\n")
        bondedDevices?.forEach { device ->

            val bondState = device.bondState
            val bondStateDetails = when (bondState) {
                BluetoothDevice.BOND_NONE -> "none"
                BluetoothDevice.BOND_BONDED -> "bonded"
                BluetoothDevice.BOND_BONDING -> "bonding"
                else -> "unknown"
            }
            val type = device.type
            val typeDetails = when (type) {
                BluetoothDevice.DEVICE_TYPE_CLASSIC -> "classic"
                BluetoothDevice.DEVICE_TYPE_LE -> "le"
                BluetoothDevice.DEVICE_TYPE_DUAL -> "dual"
                BluetoothDevice.DEVICE_TYPE_UNKNOWN -> "unknown"
                else -> ""
            }

            val deviceClass = device.bluetoothClass.deviceClass
            builder.append("device.name: ")
                .append(device.name)
                .append("\n")
                .append("device.address: ")
                .append(device.address)
                .append("\n")
//                .append("device.alias: ")
//                .append(device.alias)//TODO(Viktor) API 30
//                .append("\n")
                .append("device.boundState: ")
                .append(bondState)
                .append("($bondStateDetails)")
                .append("\n")
                .append("device.type: ")
                .append(type)
                .append("($typeDetails)")
                .append("\n")
                .append("device.uuids: ")
                .append(device.uuids.toList())
                .append("\n")
                .append("device.deviceClass: ")
                .append(deviceClass)
                .append("(${getDeviceClassName(deviceClass)})")
                .append("\n")
                .append("--------")
                .append("\n")
        }
        return this
    }

    private fun getDeviceClassName(deviceClass: Int): String {
        return when (deviceClass) {
            BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER -> "AUDIO_VIDEO_CAMCORDER"
            BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO -> "AUDIO_VIDEO_CAR_AUDIO"
            BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE -> "AUDIO_VIDEO_HANDSFREE"
            BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES -> "AUDIO_VIDEO_HEADPHONES"
            BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO -> "AUDIO_VIDEO_HIFI_AUDIO"
            BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER -> "AUDIO_VIDEO_LOUDSPEAKER"
            BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE -> "AUDIO_VIDEO_MICROPHONE"
            BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO -> "AUDIO_VIDEO_PORTABLE_AUDIO"
            BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX -> "AUDIO_VIDEO_SET_TOP_BOX"
            BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED -> "AUDIO_VIDEO_UNCATEGORIZED"
            BluetoothClass.Device.AUDIO_VIDEO_VCR -> "AUDIO_VIDEO_VCR"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA -> "AUDIO_VIDEO_VIDEO_CAMERA"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING -> "AUDIO_VIDEO_VIDEO_CONFERENCING"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER -> "AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY -> "AUDIO_VIDEO_VIDEO_GAMING_TOY"
            BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR -> "AUDIO_VIDEO_VIDEO_MONITOR"
            BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET -> "AUDIO_VIDEO_WEARABLE_HEADSET"

            BluetoothClass.Device.COMPUTER_DESKTOP -> "COMPUTER_DESKTOP"
            BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA -> "COMPUTER_HANDHELD_PC_PDA"
            BluetoothClass.Device.COMPUTER_LAPTOP -> "COMPUTER_LAPTOP"
            BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA -> "COMPUTER_PALM_SIZE_PC_PDA"
            BluetoothClass.Device.COMPUTER_SERVER -> "COMPUTER_SERVER"
            BluetoothClass.Device.COMPUTER_UNCATEGORIZED -> "COMPUTER_UNCATEGORIZED"
            BluetoothClass.Device.COMPUTER_WEARABLE -> "COMPUTER_WEARABLE"

            BluetoothClass.Device.HEALTH_BLOOD_PRESSURE -> "HEALTH_BLOOD_PRESSURE"
            BluetoothClass.Device.HEALTH_DATA_DISPLAY -> "HEALTH_DATA_DISPLAY"
            BluetoothClass.Device.HEALTH_GLUCOSE -> "HEALTH_GLUCOSE"
            BluetoothClass.Device.HEALTH_PULSE_OXIMETER -> "HEALTH_PULSE_OXIMETER"
            BluetoothClass.Device.HEALTH_PULSE_RATE -> "HEALTH_PULSE_RATE"
            BluetoothClass.Device.HEALTH_THERMOMETER -> "HEALTH_THERMOMETER"
            BluetoothClass.Device.HEALTH_UNCATEGORIZED -> "HEALTH_UNCATEGORIZED"
            BluetoothClass.Device.HEALTH_WEIGHING -> "HEALTH_WEIGHING"

            //TODO(Victor) API 33 and above
            BluetoothClass.Device.PERIPHERAL_KEYBOARD -> "PERIPHERAL_KEYBOARD"
            BluetoothClass.Device.PERIPHERAL_KEYBOARD_POINTING -> "PERIPHERAL_KEYBOARD_POINTING"
            BluetoothClass.Device.PERIPHERAL_NON_KEYBOARD_NON_POINTING -> "PERIPHERAL_NON_KEYBOARD_NON_POINTING"
            BluetoothClass.Device.PERIPHERAL_POINTING -> "PERIPHERAL_POINTING"

            BluetoothClass.Device.PHONE_CELLULAR -> "PHONE_CELLULAR"
            BluetoothClass.Device.PHONE_CORDLESS -> "PHONE_CORDLESS"
            BluetoothClass.Device.PHONE_ISDN -> "PHONE_ISDN"
            BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY -> "PHONE_MODEM_OR_GATEWAY"
            BluetoothClass.Device.PHONE_SMART -> "PHONE_SMART"
            BluetoothClass.Device.PHONE_UNCATEGORIZED -> "PHONE_UNCATEGORIZED"

            BluetoothClass.Device.TOY_CONTROLLER -> "TOY_CONTROLLER"
            BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE -> "TOY_DOLL_ACTION_FIGURE"
            BluetoothClass.Device.TOY_GAME -> "TOY_GAME"
            BluetoothClass.Device.TOY_ROBOT -> "TOY_ROBOT"
            BluetoothClass.Device.TOY_UNCATEGORIZED -> "TOY_UNCATEGORIZED"
            BluetoothClass.Device.TOY_VEHICLE -> "TOY_VEHICLE"


            BluetoothClass.Device.WEARABLE_GLASSES -> "WEARABLE_GLASSES"
            BluetoothClass.Device.WEARABLE_HELMET -> "WEARABLE_HELMET"
            BluetoothClass.Device.WEARABLE_JACKET -> "WEARABLE_JACKET"
            BluetoothClass.Device.WEARABLE_PAGER -> "WEARABLE_PAGER"
            BluetoothClass.Device.WEARABLE_UNCATEGORIZED -> "WEARABLE_UNCATEGORIZED"
            BluetoothClass.Device.WEARABLE_WRIST_WATCH -> "WEARABLE_WRIST_WATCH"
            else -> "unknown"
        }
    }

    fun build(): Info {
        builder.append("--------------------End BluetoothInfo----------------").append("\n")
        return info
    }
}
