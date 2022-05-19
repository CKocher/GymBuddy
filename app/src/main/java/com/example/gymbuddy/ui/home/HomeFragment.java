package com.example.gymbuddy.ui.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymbuddy.EventBusMessages.ActivateDeactivate;
import com.example.gymbuddy.EventBusMessages.NewExercise;
import com.example.gymbuddy.EventBusMessages.IsConnectedRequest;
import com.example.gymbuddy.EventBusMessages.IsConnectedResponse;
import com.example.gymbuddy.EventBusMessages.RepDetected;
import com.example.gymbuddy.EventBusMessages.SetComplete;
import com.example.gymbuddy.EventBusMessages.SwitchToBluetoothFragment;
import com.example.gymbuddy.EventBusMessages.SwitchToDashboard;
import com.example.gymbuddy.R;
import com.example.gymbuddy.adapter.ExpandableListDataItems;
import com.example.gymbuddy.common.DrillEnums;
import com.example.gymbuddy.data.WorkoutExercise;
import com.example.gymbuddy.databinding.FragmentHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    LinearLayout noConnectionLayout;
    View rootmember;
    List<WorkoutExercise> uebungen;
    private int uebungscounter = 0;
    Button continueWorkout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EventBus.getDefault().register(this);

        noConnectionLayout = root.findViewById(R.id.disconnectedLayout);
        noConnectionLayout.setVisibility(View.GONE);

        Button button = root.findViewById(R.id.switch_to_bluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SwitchToBluetoothFragment());

            }
        });

        rootmember = root;
        root.findViewById(R.id.activeWorkout).setVisibility(View.GONE);
        List<String> dings = new ArrayList<>();
        ExpandableListDataItems.getData();

        HashMap<String,List<WorkoutExercise>> datamap = ExpandableListDataItems.getData();

        for(String key : datamap.keySet()){
            dings.add(key);
        }


        ArrayAdapter ad = new ArrayAdapter(root.getContext(),R.layout.support_simple_spinner_dropdown_item, dings);

        Spinner spinner = root.findViewById(R.id.spinner);

        spinner.setAdapter(ad);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // Notify the selected item text


                Toast.makeText
                        (root.getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                        .show();


                uebungen = ExpandableListDataItems.expandableDetailList.get(selectedItemText);
                TextView workoutName = root.findViewById(R.id.currentWorkoutName);
                workoutName.setText(selectedItemText);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button workoutstarter = root.findViewById(R.id.startWorkoutButton);
        workoutstarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.findViewById(R.id.disconnectedLayout).setVisibility(View.GONE);
                root.findViewById(R.id.startactivity_layout).setVisibility(View.GONE);
                root.findViewById(R.id.activeWorkout).setVisibility(View.VISIBLE);
                startNextExercise();
            }
        });

        Button abort = root.findViewById(R.id.abort);
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uebungen.get(uebungscounter).succesfullyFinished = false;
            }
        });

        continueWorkout = rootmember.findViewById(R.id.continueWorkout);

        continueWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = continueWorkout.getText().toString();
                if (buttonText.equals("Nächste Übung")){
                    continueWorkout.setText("Satz pausieren");
                    startNextExercise();
                }

                if(buttonText.equals("Nächster Satz")){
                    EventBus.getDefault().post(new ActivateDeactivate(true));
                    continueWorkout.setText("Satz pausieren");
                    continueWorkout.setBackgroundColor(getResources().getColor(R.color.purple_700));
                }

                if (buttonText.equals("Satz pausieren")){
                    EventBus.getDefault().post(new ActivateDeactivate(false));
                    continueWorkout.setText("weitermachen");
                }

                if (buttonText.equals("weitermachen")){
                    EventBus.getDefault().post(new ActivateDeactivate(true));
                    continueWorkout.setText("Satz pausieren");
                }

                if (buttonText.equals("WORKOUT BEENDET")){
                    EventBus.getDefault().post(new ActivateDeactivate(false));
                    EventBus.getDefault().post(new SwitchToDashboard());
                }



            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(IsConnectedResponse event)
    {
        if (event.isConnected){
            rootmember.findViewById(R.id.disconnectedLayout).setVisibility(View.GONE);
            rootmember.findViewById(R.id.startactivity_layout).setVisibility(View.VISIBLE);
        }else{
            rootmember.findViewById(R.id.disconnectedLayout).setVisibility(View.VISIBLE);
            rootmember.findViewById(R.id.startactivity_layout).setVisibility(View.GONE);
        }

    };

    @Override
    public void onResume(){
        super.onResume();
        EventBus.getDefault().post(new IsConnectedRequest());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RepDetected event)
    {
        TextView repcount = rootmember.findViewById(R.id.repCounterDisplay);
        TextView setcount = rootmember.findViewById(R.id.setcounterDisplay);



        if ((event.getNumber() < uebungen.get(uebungscounter).reps)){
            repcount.setText(String.valueOf(event.getNumber()));
            uebungen.get(uebungscounter).finishedReps++;
        }else{
            repcount.setText(String.valueOf(0));
            int sets = Integer.parseInt(setcount.getText().toString());
            sets++;
            setcount.setText(String.valueOf(sets));
            uebungen.get(uebungscounter).finishedSets++;
            EventBus.getDefault().post(new SetComplete());
            EventBus.getDefault().post(new ActivateDeactivate(false));


            if (uebungen.get(uebungscounter).finishedSets == uebungen.get(uebungscounter).sets){
                uebungen.get(uebungscounter).succesfullyFinished = true;

                uebungscounter++;
                continueWorkout.setText("Nächste Übung");
                continueWorkout.setBackgroundColor(Color.GREEN);
                setcount.setText("0");
                repcount.setText("0");

                if (uebungscounter >= uebungen.size()){
                    continueWorkout.setText("WORKOUT BEENDET");
                    continueWorkout.setBackgroundColor(Color.GREEN);
                }


            }else{
                continueWorkout.setText("Nächster Satz");
                continueWorkout.setBackgroundColor(getResources().getColor(R.color.purple_700));
                uebungen.get(uebungscounter).finishedReps = 0;
                repcount.setText("0");
            }

        }



    };

    public void startNextExercise(){
        if(uebungscounter < uebungen.size()){
            TextView exerciseNameLabel = rootmember.findViewById(R.id.exerciseNameLabel);
            if (uebungen.get(uebungscounter).drillEnums.equals(DrillEnums.BIZEPSCURLS)){
                System.out.println("Bizepscurls");

                exerciseNameLabel.setText(uebungen.get(uebungscounter).name + " - " + uebungen.get(uebungscounter).weight + "kg");
                NewExercise newExercise = new NewExercise(DrillEnums.BIZEPSCURLS);
                EventBus.getDefault().post(newExercise);
            }
            if (uebungen.get(uebungscounter).drillEnums.equals(DrillEnums.SEITHEBEN)){

                System.out.println("Seitheben");
                exerciseNameLabel.setText(uebungen.get(uebungscounter).name + " - " + uebungen.get(uebungscounter).weight + "kg");
                NewExercise newExercise = new NewExercise(DrillEnums.SEITHEBEN);
                EventBus.getDefault().post(newExercise);
            }

        }else{
            continueWorkout.setText("WORKOUT BEENDET");
            continueWorkout.setClickable(false);
            continueWorkout.setBackgroundColor(Color.GREEN);
        }
    }
}