package com.example.gymbuddy.service;


import android.media.metrics.Event;

import com.example.gymbuddy.EventBusMessages.ActivateDeactivate;
import com.example.gymbuddy.EventBusMessages.NewExercise;
import com.example.gymbuddy.EventBusMessages.RepDetected;
import com.example.gymbuddy.common.DrillEnums;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RepDetector {

    private boolean low1 = false;
    private boolean low2 = false;

    private boolean middle = false;

    private boolean up1 = false;
    private boolean up2 = false;
    private DrillEnums activeEnum = DrillEnums.BIZEPSCURLS;
    public boolean isActive = false;



    public RepDetector(){
        EventBus.getDefault().register(this);
    }

    public boolean detectRep( int pitch, int roll, int yaw){

        if (activeEnum.equals(DrillEnums.BIZEPSCURLS)){
            System.out.println("detect pitch:"+pitch);
            return checkBooleans(70,0,-70,pitch);
        }

        if (activeEnum.equals(DrillEnums.SEITHEBEN)){
            return checkBooleans(-20, -45,-70,pitch);
        }
        return false;
    }

    private boolean checkBooleans(int high, int mid, int low, int angle){

        if (!isActive){
            return false;
        }

        if (angle > high){

            if (middle && low1 && up1){
                up2 = true;
            }else{
                up1 = true;
            }
        }

        if (angle < low){

            if (middle && up1 && low2){
                low2 = true;
            }else{
                low1 = true;
            }
        }

        if ((angle < mid+10 && angle > mid-10) && (up1 || low1)){
            middle = true;
        }

        if ((middle && low1 && up1 && up2) || (middle && up1 && low1 && low2)){
            resetBooleans();

            return true;
        }
        return false;
    }

    public void resetBooleans(){
        low1 = false;
        low2 = false;
        middle = false;
        up1 = false;
        up2 = false;
    }

    public void setActiveEnum(DrillEnums activeEnum) {
        this.activeEnum = activeEnum;
    }

    public DrillEnums getActiveEnum() {
        return activeEnum;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ActivateDeactivate event)
    {
        if (event.isOnoff()){
            isActive = true;
        }else{
            isActive = false;
        }

    };
}

