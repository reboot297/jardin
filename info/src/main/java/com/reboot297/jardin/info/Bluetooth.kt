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
internal class Bluetooth(
    private val context: Context
) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    /**
     * Full info about bluetooth state.
     */
    fun fullInfo(builder: StringBuilder) {
        supportFeature(builder)
        isEnabled(builder)
        name(builder)
        address(builder)

        discoverableTimeout(builder)
        isDiscovering(builder)

        state(builder)
        scanMode(builder)
        isLeAudioSupported(builder)
        isLeAudioBroadcastSourceSupported(builder)
        isLeAudioBroadcastAssistantSupported(builder)
        maxConnectedAudioDevices(builder)

        isLe2MPhySupported(builder)
        isLeCodedPhySupported(builder)
        isLeExtendedAdvertisingSupported(builder)
        leMaximumAdvertisingDataLength(builder)
        isLePeriodicAdvertisingSupported(builder)
        isMultipleAdvertisementSupported(builder)

        isOffloadedFilteringSupported(builder)
        isOffloadedScanBatchingSupported(builder)

        bondedDevices(builder)
    }

    /**
     * BriefInfo about bluetooth state.
     */
    fun lightInfo(builder: StringBuilder) {
        supportFeature(builder)
        isEnabled(builder)
        address(builder)
        isDiscovering(builder)
    }

    /**
     * Capability to communicate with other devices via Bluetooth or vai Bluetooth Low Energy radio.
     */
    private fun supportFeature(builder: StringBuilder) {
        builder.append("isBluetoothSupported: ")
            .append(context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            .append("\n")
            .append("isBluetoothLESupported: ")
            .append(context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            .append("\n")
    }

    /**
     * True if Bluetooth is currently enabled and ready for use.
     * Equivalent to: getBluetoothState() == STATE_ON
     */
    private fun isEnabled(builder: StringBuilder) {
        val enabled = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isEnabled ?: false
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        } else {
            bluetoothAdapter?.isEnabled ?: false
        }

        builder.append("adapter.isEnabled: ")
            .append(enabled)
            .append("\n")
    }


    /**
     * The friendly Bluetooth name of the local Bluetooth adapter.
     * This name is visible to remote Bluetooth devices.
     */

    @SuppressLint("MissingPermission")
    private fun name(builder: StringBuilder) {
        val name = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.name.toString()
            } else {
                "Manifest.permission.BLUETOOTH_CONNECT is disabled"
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.name.toString()
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        }

        builder.append("adapter.name: ")
            .append(name)
            .append("\n")
    }

    /**
     * The hardware address of the local Bluetooth adapter.
     * For example, "00:11:22:AA:BB:CC".
     */
    @SuppressLint("HardwareIds")
    private fun address(builder: StringBuilder) {
        val address = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.address.toString()
            } else {
                "Manifest.permission.BLUETOOTH_CONNECT is disabled"
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.address.toString()
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        }

        builder.append("adapter.address: ")
            .append(address)
            .append("\n")
    }

    /**
     * The timeout duration of the SCAN_MODE_CONNECTABLE_DISCOVERABLE.
     */
    @SuppressLint("MissingPermission")
    private fun discoverableTimeout(builder: StringBuilder) {//TODO check this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val timeout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.discoverableTimeout?.toString()
                } else {
                    "Manifest.permission.BLUETOOTH_SCAN is disabled"
                }
            } else {
                bluetoothAdapter?.discoverableTimeout
            }

            builder.append("adapter.discoverableTimeOut: ")
                .append(timeout)
                .append("\n")
        }
    }

    /**
     * Check if the local Bluetooth adapter is currently in the device discovery process.
     */
    @SuppressLint("MissingPermission")
    private fun isDiscovering(builder: StringBuilder) {
        val isDiscovering = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isDiscovering.toString()
            } else {
                "Manifest.permission.BLUETOOTH_SCAN is disabled"
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isDiscovering.toString()
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        }

        builder.append("adapter.isDiscovering: ")
            .append(isDiscovering)
            .append("\n")
    }

    /**
     * The current state of the local Bluetooth adapter.
     */
    private fun state(builder: StringBuilder) {
        val state = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.state
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
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
    }

    /**
     * Current Bluetooth scan mode of the local Bluetooth adapter.
     * The Bluetooth scan mode determines if the local adapter is connectable and/or discoverable
     * from remote Bluetooth devices.
     */
    @SuppressLint("MissingPermission")
    private fun scanMode(builder: StringBuilder) {
        val scanMode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.scanMode
            } else {
                "Manifest.permission.BLUETOOTH_SCAN is disabled"
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.scanMode
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        }

        val details = when (scanMode) {
            is String -> scanMode
            BluetoothAdapter.SCAN_MODE_NONE -> "none"
            BluetoothAdapter.SCAN_MODE_CONNECTABLE -> "connectable"
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> "connectable-discoverable"
            else -> "unknown"
        }

        builder.append("adapter.scanMode: ")
            .append(scanMode)
            .append("($details)")
            .append("\n")
    }

    /**
     * Check if LE Audio supported.
     */
    private fun isLeAudioSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bluetoothAdapter?.isLeAudioSupported
        } else {
            "the value is available in API 33 and above"
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
    }

    /**
     * Check if the LE audio broadcast assistant feature is supported.
     */
    private fun isLeAudioBroadcastAssistantSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bluetoothAdapter?.isLeAudioBroadcastAssistantSupported
        } else {
            "the value is available in API 33 and above"
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

    }

    /**
     * Check if the LE audio broadcast source feature is supported.
     */
    private fun isLeAudioBroadcastSourceSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bluetoothAdapter?.isLeAudioBroadcastSourceSupported
        } else {
            "the value is available in API 33 and above"
        }

        val details = when (value) {
            is String -> ""
            BluetoothStatusCodes.FEATURE_SUPPORTED -> "feature supported"
            BluetoothStatusCodes.FEATURE_NOT_SUPPORTED -> "feature not supported"
            BluetoothStatusCodes.ERROR_BLUETOOTH_NOT_ENABLED -> "bluetooth not enabled"
            else -> "unknown"
        }

        builder.append("adapter.isLeAudioBroadcastSourceSupported: ")
            .append(value)
            .append("($details)")
            .append("\n")
    }

    /**
     * The maximum number of connected devices per audio profile for this device.
     */
    @SuppressLint("MissingPermission")
    private fun maxConnectedAudioDevices(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.maxConnectedAudioDevices
                } else {
                    "Manifest.permission.BLUETOOTH_CONNECT is disabled"
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.maxConnectedAudioDevices
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            }
        } else {
            "the value is available in API 33 and above"
        }

        builder.append("adapter.maxConnectedAudioDevices: ")
            .append(value)
            .append("\n")
    }

    /**
     * Check if LE 2M PHY feature is supported
     */
    private fun isLe2MPhySupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.isLe2MPhySupported
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            } else {
                bluetoothAdapter?.isLe2MPhySupported
            }
        } else {
            "the value is available in API 26 and above"
        }

        builder.append("adapter.isLe2MPhySupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * Check if LE Coded PHY feature is supported.
     */
    private fun isLeCodedPhySupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.isLeCodedPhySupported
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            } else {
                bluetoothAdapter?.isLeCodedPhySupported
            }
        } else {
            "the value is available in API 26 and above"
        }

        builder.append("adapter.isLeCodedPhySupported: ")
            .append(value)
            .append("\n")
    }


    /**
     * Check if LE Extended Advertising feature is supported.
     */
    private fun isLeExtendedAdvertisingSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.isLeExtendedAdvertisingSupported
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            } else {
                bluetoothAdapter?.isLeExtendedAdvertisingSupported
            }
        } else {
            "the value is available in API 26 and above"
        }

        builder.append("adapter.isLeExtendedAdvertisingSupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * The maximum LE advertising data length in bytes, if LE Extended Advertising feature is supported.
     */
    private fun leMaximumAdvertisingDataLength(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.leMaximumAdvertisingDataLength
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            } else {
                bluetoothAdapter?.leMaximumAdvertisingDataLength
            }
        } else {
            "the value is available in API 26 and above"
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
    }

    /**
     * Check if LE Periodic Advertising feature is supported.
     */
    private fun isLePeriodicAdvertisingSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    bluetoothAdapter?.isLePeriodicAdvertisingSupported
                } else {
                    "Manifest.permission.BLUETOOTH is disabled"
                }
            } else {
                bluetoothAdapter?.isLePeriodicAdvertisingSupported
            }
        } else {
            "the value is available in API 26 and above"
        }

        builder.append("adapter.isLePeriodicAdvertisingSupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * Check if the multi advertisement is supported by the chipset.
     */
    private fun isMultipleAdvertisementSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isMultipleAdvertisementSupported
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        } else {
            bluetoothAdapter?.isMultipleAdvertisementSupported
        }

        builder.append("adapter.isMultipleAdvertisementSupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * Check if offloaded scan batching is supported.
     */
    private fun isOffloadedScanBatchingSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isOffloadedScanBatchingSupported
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        } else {
            bluetoothAdapter?.isOffloadedScanBatchingSupported
        }

        builder.append("adapter.isOffloadedScanBatchingSupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * Check if chipset supports on-chip filtering.
     */
    private fun isOffloadedFilteringSupported(builder: StringBuilder) {
        val value = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                bluetoothAdapter?.isOffloadedFilteringSupported
            } else {
                "Manifest.permission.BLUETOOTH is disabled"
            }
        } else {
            bluetoothAdapter?.isOffloadedFilteringSupported
        }

        builder.append("adapter.isOffloadedFilteringSupported: ")
            .append(value)
            .append("\n")
    }

    /**
     * The set of BluetoothDevice objects that are bonded (paired) to the local adapter.
     */
    @SuppressLint("MissingPermission")
    private fun bondedDevices(builder: StringBuilder) {
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
                    return
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
                    return
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
}
