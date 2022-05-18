package com.example.gymbuddy.contract;

import androidx.lifecycle.LiveData;

import com.example.gymbuddy.data.BleDeviceDataObject;


public interface DataManagerContract {
    void setBleDeviceLiveData(BleDeviceDataObject dataObject);
    LiveData<BleDeviceDataObject> getBleDeviceLiveData();
}
