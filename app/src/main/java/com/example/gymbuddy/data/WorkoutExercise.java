package com.example.gymbuddy.data;

import android.graphics.drawable.Drawable;

import com.example.gymbuddy.common.DrillEnums;

import java.io.Serializable;

public class WorkoutExercise implements Serializable {
    public String name;
    public float weight;
    public int reps;
    public int sets;
    public boolean done = false;
    public int finishedReps = 0;
    public int finishedSets = 0;
    public DrillEnums drillEnums;
    public int progressPercentage = 75;
    public boolean succesfullyFinished = false;
    public int image;
    public String parent;


    public WorkoutExercise(String uebungsname , float gewicht, int wiederholungen, int saetze, int bild, String eltern, DrillEnums enumerate){
        name = uebungsname;
        weight = gewicht;
        reps = wiederholungen;
        sets = saetze;
        image = bild;
        parent = eltern;
        drillEnums = enumerate;
    }
}
