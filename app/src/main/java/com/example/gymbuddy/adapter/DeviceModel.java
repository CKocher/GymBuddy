package com.example.gymbuddy.adapter;

public class DeviceModel {

    private String device_name;
    private String device_address;
    private int device_image;

    // Constructor
    public DeviceModel(String device_name, String device_address, int device_image) {
        this.device_name = device_name;
        this.device_address = device_address;
        this.device_image = device_image;
    }

    // Getter and Setter
    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_address() {
        return device_address;
    }

    public void setDevice_address(String device_address) {
        this.device_address = device_address;
    }

    public int getDevice_image() {
        return device_image;
    }

    public void setDevice_image(int device_image) {
        this.device_image = device_image;
    }
}
