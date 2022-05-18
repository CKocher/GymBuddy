package com.example.gymbuddy.data;

import android.bluetooth.BluetoothDevice;

import com.example.gymbuddy.common.ConnectionStates;


public class BleDeviceDataObject {
    private ConnectionStates mCurrentConnectionState;
    private BluetoothDevice mBluetoothDevice;

    // Constructor
    public BleDeviceDataObject(ConnectionStates connectionState, BluetoothDevice bluetoothDevice) {
        this.mCurrentConnectionState = connectionState;
        this.mBluetoothDevice = bluetoothDevice;
    }

    // Getters
    public ConnectionStates getCurrentConnectionState() {
        return mCurrentConnectionState;
    }

    public BluetoothDevice getBluetoothDevice() {
        return mBluetoothDevice;
    }
}
