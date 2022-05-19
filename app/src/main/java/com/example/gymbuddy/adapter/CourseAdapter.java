package com.example.gymbuddy.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbuddy.EventBusMessages.ConnectToDevice;
import com.example.gymbuddy.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.Viewholder> {

    private Context context;
    private ArrayList<DeviceModel> courseModelArrayList;

    // Constructor
    public CourseAdapter(Context context, ArrayList<DeviceModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @NonNull
    @Override
    public CourseAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        DeviceModel model = courseModelArrayList.get(position);
        holder.courseNameTV.setText(model.getDevice_name());
        holder.courseRatingTV.setText("BT-Adresse: "+model.getDevice_address());
        holder.courseIV.setImageResource(model.getDevice_image());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Nummer: "+position + " gedrückt! Listenlänge: " + courseModelArrayList.size());
                v.setBackgroundColor(Color.LTGRAY);
                EventBus.getDefault().post(new ConnectToDevice(courseModelArrayList.get(position).getDevice_name(), courseModelArrayList.get(position).getDevice_address(),v));


            }
        });
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return courseModelArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView courseIV;
        private TextView courseNameTV, courseRatingTV;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            courseIV = itemView.findViewById(R.id.idIVCourseImage);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseRatingTV = itemView.findViewById(R.id.idTVCourseRating);
        }
    }
}
