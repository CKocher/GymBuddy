package com.example.gymbuddy.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbuddy.EventBusMessages.StartScan;
import com.example.gymbuddy.R;
import com.example.gymbuddy.adapter.BLEDeviceListViewAdapter;
import com.example.gymbuddy.adapter.CourseAdapter;
import com.example.gymbuddy.adapter.DeviceModel;
import com.example.gymbuddy.databinding.FragmentNotificationsBinding;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    BLEDeviceListViewAdapter bleDeviceListViewAdapter;
    private ArrayList<DeviceModel> courseModelArrayList;
    private RecyclerView courseRV;
    private CourseAdapter courseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        EventBus.getDefault().register(this);


        //final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        courseRV = binding.getRoot().findViewById(R.id.idRVCourse);

        // here we have created new array list and added data to it.
        courseModelArrayList = new ArrayList<>();
       /* courseModelArrayList.add(new DeviceModel("DSA in Java", 4, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("Java Course", 3, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("C++ COurse", 4, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("DSA in C++", 4, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("Kotlin for Android", 4, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("Java for Android", 4, R.drawable.ble_connected));
        courseModelArrayList.add(new DeviceModel("HTML and CSS", 4, R.drawable.ble_connected));*/

        // we are initializing our adapter class and passing our arraylist to it.
        courseAdapter = new CourseAdapter(binding.getRoot().getContext(), courseModelArrayList);
        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
        EventBus.getDefault().post(new StartScan());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeviceModel event)
    {
        courseModelArrayList.add(event);
        courseAdapter.notifyDataSetChanged();
    };
}