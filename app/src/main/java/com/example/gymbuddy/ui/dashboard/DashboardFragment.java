package com.example.gymbuddy.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymbuddy.R;
import com.example.gymbuddy.adapter.CustomizedExpandableListAdapter;
import com.example.gymbuddy.adapter.ExpandableListDataItems;
import com.example.gymbuddy.data.WorkoutExercise;
import com.example.gymbuddy.databinding.FragmentDashboardBinding;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        expandableListViewExample = (ExpandableListView) root.findViewById(R.id.expandableListViewSample);
        expandableDetailList = ExpandableListDataItems.getData();
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



        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
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
}