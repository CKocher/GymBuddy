package com.example.gymbuddy.data;

import com.example.gymbuddy.common.DrillEnums;

public class WorkoutExercise {
    public String name;
    public float weight;
    public int reps;
    public int sets;
    public boolean done = false;
    public int finishedReps = 0;
    public DrillEnums drillEnums;
    public int progressPercentage = 75;


    public WorkoutExercise(String uebungsname , float gewicht, int wiederholungen, int saetze){
        name = uebungsname;
        weight = gewicht;
        reps = wiederholungen;
        sets = saetze;
    }
}
