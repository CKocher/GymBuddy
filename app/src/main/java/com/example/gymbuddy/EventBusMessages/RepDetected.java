package com.example.gymbuddy.EventBusMessages;

import com.example.gymbuddy.service.RepDetector;

public class RepDetected {

    private int number;

    public RepDetected(int repcount){
        number = repcount;
    }

    public int getNumber() {
        return number;
    }
}
