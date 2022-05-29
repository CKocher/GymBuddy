package com.example.gymbuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gymbuddy.EventBusMessages.SwitchToHome;
import com.example.gymbuddy.R;
import com.example.gymbuddy.data.WorkoutExercise;
import com.example.gymbuddy.ui.dashboard.DashboardFragment;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DeleteListviewAdapter extends BaseAdapter {

    private List<String> nameList = new ArrayList<>();
    private View viewContext;
    private List<String> deleteList = new ArrayList<>();
    private DashboardFragment dashboardFragment2;


    public DeleteListviewAdapter(View context, DashboardFragment dashboardFragment){
        viewContext = context;
        nameList.addAll(ExpandableListDataItems.expandableDetailList.keySet());
        dashboardFragment2 = dashboardFragment;

    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(viewContext.getContext()).
                    inflate(R.layout.delete_item, parent, false);
        }

        String currentItem = String.valueOf(getItem(position));

        TextView tv = convertView.findViewById(R.id.deleteText);
        tv.setText(currentItem);
        CheckBox delBox = convertView.findViewById(R.id.deleteCheckbox);
        delBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delBox.isChecked()){
                    deleteList.add(currentItem);

                }else{
                    deleteList.remove(currentItem);
                }
            }
        });

        Button delButton = viewContext.findViewById(R.id.delButton);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String name : deleteList){
                   List<WorkoutExercise> placeholder =  ExpandableListDataItems.expandableDetailList.get(name);
                   ExpandableListDataItems.expandableDetailList.remove(name);
                   System.out.println(ExpandableListDataItems.expandableDetailList.keySet() + " " + name);
                    nameList.remove(name);
                }
                deleteList.clear();
                notifyDataSetChanged();
                dashboardFragment2.refreshAttempt();

                LinearLayout displaylayout = viewContext.findViewById(R.id.deleteLayout);
                displaylayout.setVisibility(View.GONE);
                LinearLayout adderLayout = viewContext.findViewById(R.id.displayLayout);
                adderLayout.setVisibility(View.VISIBLE);


                FileOutputStream fos = null;
                try {
                    fos = viewContext.getContext().openFileOutput("storageFile.ser", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ObjectOutputStream os = null;
                try {
                    os = new ObjectOutputStream(fos);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.writeObject(ExpandableListDataItems.expandableDetailList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(new SwitchToHome());

            }

        });





        return convertView;
    }
}
