package com.example.gymbuddy.EventBusMessages;

import com.example.gymbuddy.common.DrillEnums;

public class NewExercise {

    private DrillEnums kind;

    public NewExercise(DrillEnums activeEnum){
        kind = activeEnum;
    }

    public DrillEnums getKind() {
        return kind;
    }
}
