package com.example.gymbuddy.adapter;

import com.example.gymbuddy.R;
import com.example.gymbuddy.common.DrillEnums;
import com.example.gymbuddy.data.WorkoutExercise;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataItems {
    public static HashMap<String, List<WorkoutExercise>> expandableDetailList = new HashMap<>();
    public static HashMap<String, List<WorkoutExercise>> initialize() {

        List<WorkoutExercise> legdayExercises = new ArrayList<>();

        WorkoutExercise legday1 = new WorkoutExercise("Squads",(float) 100, 8,3, R.drawable.failed, "Legday - 18.05.22", DrillEnums.BIZEPSCURLS);
        legdayExercises.add(legday1);
        expandableDetailList.put("Legday - 18.05.22", legdayExercises);

        List<WorkoutExercise> workoutExercises = new ArrayList<>();

        WorkoutExercise uebung1 = new WorkoutExercise("Bizepscurls", (float)15.0, 8, 3, R.drawable.failed,"Brust - 16.05.2022", DrillEnums.BIZEPSCURLS);
        uebung1.finishedReps++;



        WorkoutExercise uebung2 = new WorkoutExercise("Seitheben", (float)10.0, 7, 1, R.drawable.failed,"Brust - 16.05.2022", DrillEnums.BIZEPSCURLS);
        uebung2.finishedReps = 5;
        workoutExercises.add(uebung1);
        workoutExercises.add(uebung2);

        expandableDetailList.put("Brust - 16.05.2022", workoutExercises);



        return expandableDetailList;
    }



}
