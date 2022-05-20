package com.example.gymbuddy.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymbuddy.EventBusMessages.SwitchToDashboard;
import com.example.gymbuddy.EventBusMessages.SwitchToHome;
import com.example.gymbuddy.R;
import com.example.gymbuddy.adapter.CustomizedExpandableListAdapter;
import com.example.gymbuddy.adapter.ExpandableListDataItems;
import com.example.gymbuddy.common.DrillEnums;
import com.example.gymbuddy.data.WorkoutExercise;
import com.example.gymbuddy.databinding.FragmentDashboardBinding;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private ExpandableListView expandableListViewExample;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableTitleList;
    private HashMap<String, List<WorkoutExercise>> expandableDetailList;
    ArrayAdapter<String> adapter;
    ListView listView;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
         root = binding.getRoot();

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        expandableListViewExample = (ExpandableListView) root.findViewById(R.id.expandableListViewSample);
        expandableDetailList = ExpandableListDataItems.expandableDetailList;
        expandableTitleList = new ArrayList<String>(expandableDetailList.keySet());
        expandableListAdapter = new CustomizedExpandableListAdapter(root.getContext(), expandableTitleList, expandableDetailList);

        expandableListViewExample.setAdapter(expandableListAdapter);

        // This method is called when the group is expanded
        expandableListViewExample.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
             //   Toast.makeText(root.getApplicationContext(), expandableTitleList.get(groupPosition) + " List Expanded.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the group is collapsed
        expandableListViewExample.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
              //  Toast.makeText(getApplicationContext(), expandableTitleList.get(groupPosition) + " List Collapsed.", Toast.LENGTH_SHORT).show();
            }
        });

        // This method is called when the child in any group is clicked
        // via a toast method, it is shown to display the selected child item as a sample
        // we may need to add further steps according to the requirements
        expandableListViewExample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(root.getContext(), expandableTitleList.get(groupPosition) + " -> " + expandableDetailList.get(expandableTitleList.get(groupPosition)).get(childPosition).name, Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        Button accept = root.findViewById(R.id.acceptNewWorkout);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText workoutName = root.findViewById(R.id.editTextTextPersonName);
                String name = workoutName.getText().toString();

                List<WorkoutExercise> workoutExercises = new ArrayList<>();

                CheckBox bizepsCheckbox = root.findViewById(R.id.bizepsadderCheckbox);
                if (bizepsCheckbox.isChecked()){
                    TextView bizepsadder = root.findViewById(R.id.bizepsadder);
                    String bizeps = bizepsadder.getText().toString();

                    EditText bizreps = root.findViewById(R.id.bizcurlsNumber);
                    String curls = bizreps.getText().toString();

                    EditText bizweight = root.findViewById(R.id.bizcurlsNumberWeight);
                    String weight = bizweight.getText().toString();

                    EditText bizsets2 = root.findViewById(R.id.bizcurlsNumbersets);
                    String bizsets = bizsets2.getText().toString();

                    workoutExercises.add(new WorkoutExercise(bizeps, Float.parseFloat(weight),Integer.parseInt(curls),Integer.parseInt(bizsets),R.drawable.failed,name, DrillEnums.BIZEPSCURLS));

                }
                CheckBox trizepsCheckbox = root.findViewById(R.id.trizepsadderCheckbox);
                if (trizepsCheckbox.isChecked()){
                    TextView triepsadder = root.findViewById(R.id.trizeps);
                    String trieps = triepsadder.getText().toString();

                    EditText trireps = root.findViewById(R.id.tripcurlsNumber);
                    String tricurls = trireps.getText().toString();

                    EditText triweight = root.findViewById(R.id.trizepsNumberweight);
                    String traiweight = triweight.getText().toString();

                    EditText trisets2 = root.findViewById(R.id.tripcurlsNumberSet);
                    String trisets = trisets2.getText().toString();

                    workoutExercises.add(new WorkoutExercise(trieps, Float.parseFloat(traiweight),Integer.parseInt(tricurls),Integer.parseInt(trisets),R.drawable.failed,name, DrillEnums.TRIZEPSCURLS));

                }
                CheckBox squadCheckbox = root.findViewById(R.id.squadsadderCheckbox);
                if (squadCheckbox.isChecked()){
                    TextView squadsadder = root.findViewById(R.id.squadsadder);
                    String squads = squadsadder.getText().toString();

                    EditText squadreps = root.findViewById(R.id.squadNumber);
                    String squadcurls = squadreps.getText().toString();

                    EditText squadweight = root.findViewById(R.id.squadsNumberweights);
                    String sweight = squadweight.getText().toString();

                    EditText squadsets2 = root.findViewById(R.id.squadsNumbersets);
                    String squadsets = squadsets2.getText().toString();

                    workoutExercises.add(new WorkoutExercise(squads, Float.parseFloat(sweight),Integer.parseInt(squadcurls),Integer.parseInt(squadsets),R.drawable.failed,name, DrillEnums.SQUADS));

                }
                CheckBox faceCheckbox = root.findViewById(R.id.facepullsadderCheckbox);
                if (faceCheckbox.isChecked()){
                    TextView facesadder = root.findViewById(R.id.bizepsadder);
                    String facep = facesadder.getText().toString();

                    EditText facereps = root.findViewById(R.id.facepNumber);
                    String facecurls = facereps.getText().toString();

                    EditText faceweight = root.findViewById(R.id.facepNumberweights);
                    String fweight = faceweight.getText().toString();

                    EditText fsets2 = root.findViewById(R.id.facepNumbersets);
                    String fsets = fsets2.getText().toString();

                    workoutExercises.add(new WorkoutExercise(facep, Float.parseFloat(fweight),Integer.parseInt(facecurls),Integer.parseInt(fsets),R.drawable.failed,name, DrillEnums.BUTTERFLY));

                    LinearLayout displaylayout = root.findViewById(R.id.displayLayout);
                    displaylayout.setVisibility(View.VISIBLE);
                    LinearLayout adderLayout = root.findViewById(R.id.adderLayout);
                    adderLayout.setVisibility(View.GONE);
                }





              ExpandableListDataItems.expandableDetailList.put(name,workoutExercises);
              refreshAttempt();
                EventBus.getDefault().post(new SwitchToHome());
            }
        });

        Button button = root.findViewById(R.id.button23);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListDataItems.expandableDetailList.put("Krasses Workout - 16.05.2020",new ArrayList<>());
                LinearLayout displaylayout = root.findViewById(R.id.displayLayout);
                displaylayout.setVisibility(View.GONE);
                LinearLayout adderLayout = root.findViewById(R.id.adderLayout);
                adderLayout.setVisibility(View.VISIBLE);
                EventBus.getDefault().post(new SwitchToDashboard());
            }
        });



        return root;
    }



    @Override
    public void onResume(){
        super.onResume();

        refreshAttempt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public List<String> getExpandableTitleList() {
        return expandableTitleList;
    }

    public HashMap<String, List<WorkoutExercise>> getExpandableDetailList() {
        return expandableDetailList;
    }

    public void refreshAttempt(){
        expandableListAdapter = null;
        expandableListAdapter = new CustomizedExpandableListAdapter(root.getContext(), expandableTitleList, expandableDetailList);
        expandableListViewExample.setAdapter(expandableListAdapter);


    }
}