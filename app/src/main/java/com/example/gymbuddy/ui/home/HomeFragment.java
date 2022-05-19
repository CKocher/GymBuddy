package com.example.gymbuddy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymbuddy.EventBusMessages.IsConnectedRequest;
import com.example.gymbuddy.EventBusMessages.IsConnectedResponse;
import com.example.gymbuddy.EventBusMessages.SwitchToBluetoothFragment;
import com.example.gymbuddy.R;
import com.example.gymbuddy.databinding.FragmentHomeBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    View rootMember;
    EventBus event = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

            EventBus.getDefault().register(this);



        Button button = root.findViewById(R.id.switch_to_bluetooth);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SwitchToBluetoothFragment());
            }
        });

        rootMember = root;
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
            rootMember.findViewById(R.id.disconnectedLayout).setVisibility(View.GONE);
        }

    };

    @Override
    public void onResume(){
        super.onResume();
        EventBus.getDefault().post(new IsConnectedRequest());
    }
}