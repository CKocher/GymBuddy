package com.example.gymbuddy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymbuddy.R;
import com.example.gymbuddy.data.WorkoutExercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomizedExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableTitleList;
    private HashMap<String, List<WorkoutExercise>> expandableDetailList;
    private List<String> checkboxList = new ArrayList<>();



    // constructor
    public CustomizedExpandableListAdapter(Context context, List<String> expandableListTitle,
                                           HashMap<String, List<WorkoutExercise>> expandableListDetail) {
        this.context = context;
        this.expandableTitleList = expandableListTitle;
        this.expandableDetailList = expandableListDetail;
    }

    @Override
    // Gets the data associated with the given child within the given group.
    public Object getChild(int lstPosn, int expanded_ListPosition) {

        return this.expandableDetailList.get(this.expandableTitleList.get(lstPosn)).get(expanded_ListPosition);
    }

    @Override
    // Gets the ID for the given child within the given group.
    // This ID must be unique across all children within the group. Hence we can pick the child uniquely
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    // Gets a View that displays the data for the given child within the given group.
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final WorkoutExercise expandedListText = (WorkoutExercise) getChild(lstPosn, expanded_ListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.exercise_item, null);

        }
        TextView exerciseName = (TextView) convertView.findViewById(R.id.idTVCourseName);
        exerciseName.setText(expandedListText.name);

        TextView exerciseProperties = (TextView) convertView.findViewById(R.id.idTVCourseRating);
        exerciseProperties.setText(String.valueOf(expandedListText.weight)+"kg x "+String.valueOf(expandedListText.reps)+ " reps x " + String.valueOf(expandedListText.sets)+ " sets");



            for (String name : ExpandableListDataItems.expandableDetailList.keySet()){
                List<WorkoutExercise> liste = ExpandableListDataItems.expandableDetailList.get(name);
                for (WorkoutExercise workoutExercise : liste){

                    if (workoutExercise.succesfullyFinished){
                        ImageView img= convertView.findViewById(R.id.imageView4);
                       // Drawable myDrawable = context.getResources().getDrawable(workoutExercise.image);
                        img.setImageResource(R.drawable.check);
                    }
                }
            }





        return convertView;
    }

    @Override
    // Gets the number of children in a specified group.
    public int getChildrenCount(int listPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(listPosition)).size();
    }

    @Override
    // Gets the data associated with the given group.
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    @Override
    // Gets the number of groups.
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    @Override
    // Gets the ID for the group at the given position. This group ID must be unique across groups.
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    // Gets a View that displays the given group.
    // This View is only for the group--the Views for the group's children
    // will be fetched using getChildView()
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

       /* CheckBox deleteBox = convertView.findViewById(R.id.deleteCheckbox);
        deleteBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteBox.isChecked()){
                    checkboxList.add(listTitle);
                    Toast.makeText(v.getContext(),  deleteBox.isChecked()+" " + listTitle, Toast.LENGTH_SHORT).show();
                }else{
                    checkboxList.remove(listTitle);
                }
            }
        });*/


        return convertView;
    }

    @Override
    // Indicates whether the child and group IDs are stable across changes to the underlying data.
    public boolean hasStableIds() {
        return false;
    }

    @Override
    // Whether the child at the specified position is selectable.
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return false;
    }
}

