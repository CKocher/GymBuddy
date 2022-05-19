package com.example.gymbuddy.EventBusMessages;

import android.view.View;

import com.example.gymbuddy.common.ConnectionStates;

public class Connected {

    public ConnectionStates v;

    public Connected(ConnectionStates connectionstate){
        v = connectionstate;
    }
}
