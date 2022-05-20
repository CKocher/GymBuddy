package com.example.gymbuddy.EventBusMessages;

import com.example.gymbuddy.data.WorkoutExercise;

import java.util.HashMap;
import java.util.List;

public class EndOfWorkout {

    public HashMap<String, List<WorkoutExercise>> expandableDetailList;

    public EndOfWorkout(HashMap<String, List<WorkoutExercise>> liste){
     expandableDetailList = liste;
    }
}
