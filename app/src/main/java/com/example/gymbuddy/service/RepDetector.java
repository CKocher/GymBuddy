package com.example.gymbuddy.service;


import com.example.gymbuddy.common.DrillEnums;

public class RepDetector {

    private boolean low1 = false;
    private boolean low2 = false;

    private boolean middle = false;

    private boolean up1 = false;
    private boolean up2 = false;
    private DrillEnums activeEnum = DrillEnums.BIZEPSCURLS;



    public RepDetector(){

    }




    public boolean detectRep( int pitch, int roll, int yaw){
        System.out.println("detectRep:"+pitch);
        if (activeEnum.equals(DrillEnums.BIZEPSCURLS)){
            return checkBooleans(90,0,-90,pitch);
        }

        if (activeEnum.equals(DrillEnums.SEITHEBEN)){
            return checkBooleans(-10, -45,-90,pitch);
        }
        return false;
    }

    private boolean checkBooleans(int high, int mid, int low, int angle){
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
}

