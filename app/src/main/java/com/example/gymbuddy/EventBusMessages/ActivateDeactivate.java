package com.example.gymbuddy.EventBusMessages;

public class ActivateDeactivate {

    private boolean onoff;

    public ActivateDeactivate(boolean ison){
        onoff = ison;
    }

    public boolean isOnoff() {
        return onoff;
    }

    public void setOnoff(boolean onoff) {
        this.onoff = onoff;
    }
}
