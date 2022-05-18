package com.example.gymbuddy.backgroundThread;

import com.example.gymbuddy.common.CharacteristicTypes;
import com.example.gymbuddy.common.DrillEnums;
import com.example.gymbuddy.service.BleConnectivityService;

public class AngularVelocityThread extends Thread{

    private BleConnectivityService service;
    private DrillEnums exercise;

    private int ringbufferCounter = 0;



    public AngularVelocityThread(BleConnectivityService bleService, DrillEnums ex){

        service = bleService;
        exercise = ex;
    }


    public void run(){

        while(true){

            service.readCharacteristicValue(CharacteristicTypes.PITCH);
            service.readCharacteristicValue(CharacteristicTypes.Z);
            service.readCharacteristicValue(CharacteristicTypes.ROLL);




            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }







}

