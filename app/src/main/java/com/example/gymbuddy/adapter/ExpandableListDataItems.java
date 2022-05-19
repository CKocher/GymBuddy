package com.example.gymbuddy.adapter;

import com.example.gymbuddy.common.DrillEnums;
import com.example.gymbuddy.data.WorkoutExercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<WorkoutExercise>> getData() {
        HashMap<String, List<WorkoutExercise>> expandableDetailList = new HashMap<>();
        List<WorkoutExercise> workoutExercises = new ArrayList<>();

        WorkoutExercise uebung1 = new WorkoutExercise("Bizepscurls", (float)150.3, 10, 3);
        uebung1.drillEnums = DrillEnums.BIZEPSCURLS;
        uebung1.finishedReps++;
        WorkoutExercise uebung2 = new WorkoutExercise("Seitheben", (float)10.6, 10, 1);
        uebung2.drillEnums = DrillEnums.SEITHEBEN;
        uebung2.finishedReps = 5;
        workoutExercises.add(uebung1);
        workoutExercises.add(uebung2);

        expandableDetailList.put("Legday - 16.05.2022", workoutExercises);

        return expandableDetailList;
    }
}
