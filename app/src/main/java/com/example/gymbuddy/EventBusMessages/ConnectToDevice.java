package com.example.gymbuddy.EventBusMessages;

import android.view.View;

public class ConnectToDevice {

    public String devName;
    public String adress;
    public View liste;

    public ConnectToDevice(String add, String position, View v){
        devName = add;
        adress = position;
        liste = v;

    }


}
