package com.example.gymbuddy;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.example.gymbuddy.backgroundThread.AngularVelocityThread;
import com.example.gymbuddy.common.ConnectionStates;
import com.example.gymbuddy.common.Constants;
import com.example.gymbuddy.contract.MainActivityContract;
import com.example.gymbuddy.data.BleDeviceDataObject;
import com.example.gymbuddy.model.DataManager;
import com.example.gymbuddy.service.BleConnectivityService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbuddy.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    private ActivityMainBinding binding;

    private ConnectionStates mCurrentState = ConnectionStates.DISCONNECTED;

    private final String[] PERMISSIONS = {
            // Note: Only 'ACCESS_FINE_LOCATION' permission is needed from user at run-time
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BleConnectivityService mService;

    private ActivityMainBinding mBinding;


    private View mCustomAlertView;
    private RecyclerView mRecyclerView;
    private LottieAnimationView mScanningLottieView;


    private final List<BluetoothDevice> mBleDeviceList = new ArrayList<>();

    private String mManufacturerName;
    private String mManufacturerModel;
    private Intent mServiceIntent;
    private boolean mIsLedButtonClicked = false;
    AngularVelocityThread thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerToBroadcastReceivers();
        requestPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissionsAtRuntime()) {
            if (!checkBluetoothStatus()) {
                enableBluetoothRequest();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterFromBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearComponents();
    }

    @Override
    public void initializeComponents() {
        Log.d(TAG, "initializeComponents() called");

        // Initialize and Bind to Service
        mServiceIntent = new Intent(this, BleConnectivityService.class);
        bindToService();

        // Set initial state of BLE device in Live-Data as DISCONNECTED and bluetoothDevice as 'null'
        DataManager.getInstance()
                .setBleDeviceLiveData(new BleDeviceDataObject(ConnectionStates.DISCONNECTED, null));

        // Attach observer for live-data
        DataManager.getInstance().getBleDeviceLiveData().observeForever(mDeviceConnectionStateObserver);
    }

    @Override
    public void initializeUi() {

    }

    /**
     * Manage Service life-cycles
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mService = ((BleConnectivityService.LocalBinder) service).getService();
            if (!mService.initializeBluetoothAdapter()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG, "onServiceDisconnected: ");
            mService = null;
        }
    };

    /**
     * Device Manufacturer Name Broadcast Receiver
     */
    private final BroadcastReceiver manufacturerNameBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null) {
                mManufacturerName = intent.getStringExtra(Constants.DATA_MANUFACTURER_NAME);
                Log.d(TAG, "onReceive() called for with: Manufacture Name = [" + mManufacturerName + "]");
            }
        }
    };

    /**
     * Device Manufacturer Model Broadcast Receiver
     */
    private final BroadcastReceiver manufacturerModelBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null) {
                mManufacturerModel = intent.getStringExtra(Constants.DATA_MANUFACTURER_MODEL);
                Log.d(TAG, "onReceive() called for with: Manufacture Model = [" + mManufacturerModel + "]");

                prepareDeviceInfoButton();
            }
        }
    };

    /**
     * Temperature Broadcast Receiver
     */
    private final BroadcastReceiver temperatureBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null) {
                String temperature = intent.getStringExtra(Constants.DATA_Pitch);
                Log.d(TAG, "onReceive() called for with: Temperature = [" + temperature + "]");

             //   mBinding.tvTemperature.setText(temperature);
            }
        }
    };

    /**
     * Humidity Broadcast Receiver
     */
    private final BroadcastReceiver humidityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null) {
                String humidity = intent.getStringExtra(Constants.DATA_HUMIDITY);
                Log.d(TAG, "onReceive() called for with: Humidity = [" + humidity + "]");

              //  mBinding.tvHumidity.setText(humidity);
            }
        }
    };

    /**
     * LED Status Broadcast Receiver
     */
    private final BroadcastReceiver ledStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action != null) {
                String ledState = intent.getStringExtra(Constants.DATA_LED_STATUS);
                Log.d(TAG, "onReceive() called for with: LED Status = [" + ledState + "]");

                onLedBroadcastEventReceived(ledState);
            }
        }
    };

    /**
     * Observer for current-connection-state of BLE device
     */
    private final Observer<BleDeviceDataObject> mDeviceConnectionStateObserver = new Observer<BleDeviceDataObject>() {
        @Override
        public void onChanged(BleDeviceDataObject bleDeviceDataObject) {
            Log.d(TAG, "onChanged() called with: connectionState = [" + bleDeviceDataObject.getCurrentConnectionState() + "]");
            mCurrentState = bleDeviceDataObject.getCurrentConnectionState();

            if (mCurrentState.equals(ConnectionStates.CONNECTED)) {
                if (bleDeviceDataObject.getBluetoothDevice() != null) {
                    onConnectedBroadcastReceived(bleDeviceDataObject.getBluetoothDevice());
                }
            } else if (mCurrentState.equals(ConnectionStates.DISCONNECTED)) {
                onDisconnectedBroadcastReceived();
            }
        }
    };

    @Override
    public void clearComponents() {
        Log.d(TAG, "clearComponents() called");
        DataManager.getInstance().getBleDeviceLiveData().removeObserver(mDeviceConnectionStateObserver);
        unbindFromService();
    }

    @Override
    public void startAnimation(LottieAnimationView animationView, String animationName, boolean loop) {

    }

    @Override
    public void stopAnimation(LottieAnimationView animationView) {

    }

    @Override
    public void displaySnackBar(String message) {
       // Snackbar.make(mBinding.layoutMain, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void changeVisibility(View view, int visibility) {

    }

    @Override
    public void switchButtonText(Button button, String text) {

    }

    @Override
    public void disableButtons() {

    }

    @Override
    public void enableButtons() {

    }

    @Override
    public void prepareConnectButton() {

    }

    @Override
    public void prepareDisconnectButton() {

    }

    @Override
    public void launchDeviceScanDialog() {
        Log.d(TAG, "launchDeviceScanDialog() called");
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(mCustomAlertView)
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_positive_button, null)
                .setNegativeButton(R.string.dialog_negative_button, null)
                .setNeutralButton(R.string.dialog_neutral_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopScanning();
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true)
                .create();

        // Implemented in order to avoid auto-dismiss upon click of a dialog button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Start button clicked");
                        startScanning();
                    }
                });

                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "Stop button clicked");
                        stopScanning();
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    public void showDeviceInfoBottomSheetDialog() {

    }

    @Override
    public void prepareReadTemperatureButton() {

    }

    @Override
    public void prepareNotifyTemperatureButton() {

    }

    @Override
    public void prepareReadHumidityButton() {

    }

    @Override
    public void prepareNotifyHumidityButton() {

    }

    @Override
    public void prepareLedToggleButton() {

    }

    @Override
    public void prepareDeviceInfoButton() {

    }

    @Override
    public void startScanning() {
        Log.d(TAG, "startScanning() called");
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (mScanningLottieView.getVisibility() != View.VISIBLE) {
            changeVisibility(mScanningLottieView, View.VISIBLE);
        }

        showPreviouslyConnectedDevice();

        // Begin Scan
        Log.d(TAG, "Started Scanning for BLE devices");
    }

    @Override
    public void stopScanning() {
        Log.d(TAG, "stopScanning() called");

        if (mScanningLottieView.getVisibility() != View.VISIBLE) {
            changeVisibility(mScanningLottieView, View.VISIBLE);
            changeVisibility(mRecyclerView, View.GONE);
        }
        mBleDeviceList.clear();

    }

    @Override
    public void showPreviouslyConnectedDevice() {
        if (DataManager.getInstance().getBleDeviceLiveData().getValue() != null) {
            if (DataManager.getInstance().getBleDeviceLiveData().getValue()
                    .getBluetoothDevice() != null) {
                BluetoothDevice device = DataManager.getInstance().getBleDeviceLiveData().getValue()
                        .getBluetoothDevice();
                Log.d(TAG, "Already Connected to device = [" + device.getName() + "]");

                mBleDeviceList.add(device);
                mCurrentState = ConnectionStates.CONNECTED;
                updateAdapterConnectionState(0);
            } else {
                Log.e(TAG, "Not connected to any BLE device previously");
                mCurrentState = ConnectionStates.DISCONNECTED;
            }
        }
    }

    @Override
    public void onConnectedBroadcastReceived(BluetoothDevice device) {
        Log.d(TAG, "onConnectedBroadcastReceived() called");

        String deviceName = device.getName();
        String mDeviceAddress = device.getAddress();

        enableButtons();
        prepareDisconnectButton();
        updateAdapterConnectionState(-1);


    }

    @Override
    public void onDisconnectedBroadcastReceived() {
/*
        disableButtons();
        prepareConnectButton();

        mBinding.tvDeviceName.setText(getString(R.string.no_sensor_connected));
        mBinding.tvConnectivityStatus.setText(R.string.no_sensor_connected);
        mBinding.tvTemperature.setText("0");
        mBinding.tvHumidity.setText("0");
        stopAnimation(mBinding.lottieViewTemperature);
        stopAnimation(mBinding.lottieViewHumidity);
        changeVisibility(mBinding.btnDeviceInformation, View.INVISIBLE);
        mBinding.tvConnectivityStatus.setTextColor(getResources().getColor(R.color.red_500));
        switchButtonText(mBinding.btnStartScanning, getResources().getString(R.string.connect));
        */

    }

    @Override
    public void onLedBroadcastEventReceived(String ledState) {

    }

    @Override
    public void bindToService() {
        Log.d(TAG, "bindToService() called");
        bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void unbindFromService() {
        Log.d(TAG, "unbindFromService() called");
        unbindService(mServiceConnection);
    }

    @Override
    public void registerToBroadcastReceivers() {
        Log.d(TAG, "registerToBroadcastReceivers() called");
        registerReceiver(manufacturerNameBroadcastReceiver, new IntentFilter(Constants.ACTION_MANUFACTURER_NAME_AVAILABLE));
        registerReceiver(manufacturerModelBroadcastReceiver, new IntentFilter(Constants.ACTION_MANUFACTURER_MODEL_AVAILABLE));
        registerReceiver(temperatureBroadcastReceiver, new IntentFilter(Constants.ACTION_TEMPERATURE_AVAILABLE));
        registerReceiver(humidityBroadcastReceiver, new IntentFilter(Constants.ACTION_HUMIDITY_AVAILABLE));
        registerReceiver(ledStatusBroadcastReceiver, new IntentFilter(Constants.ACTION_LED_STATUS_AVAILABLE));
    }

    @Override
    public void unregisterFromBroadcastReceiver() {
        Log.d(TAG, "unregisterFromBroadcastReceiver() called");
        unregisterReceiver(manufacturerNameBroadcastReceiver);
        unregisterReceiver(manufacturerModelBroadcastReceiver);
        unregisterReceiver(temperatureBroadcastReceiver);
        unregisterReceiver(humidityBroadcastReceiver);
        unregisterReceiver(ledStatusBroadcastReceiver);
    }

    @Override
    public void connectToDevice(String address) {
        Log.d(TAG, "connectToDevice() called with: address = [" + address + "]");
        mService.connectToBleDevice(address);
    }

    @Override
    public void disconnectFromDevice() {
        Log.d(TAG, "disconnectFromDevice() called");
        mService.disconnectFromBleDevice();
    }

    @Override
    public void requestPermissions() {
        Log.d(TAG, "requestPermissions() called");
        ActivityCompat.requestPermissions(this, PERMISSIONS, Constants.REQUEST_PERMISSION_ALL);
    }

    @Override
    public boolean checkPermissionsAtRuntime() {
        Log.d(TAG, "checkPermissionsAtRuntime() called");
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkBluetoothStatus() {
        Log.d(TAG, "checkBluetoothStatus() called");
        if (mBluetoothAdapter != null) {
            // Return Bluetooth Enable Status
            return mBluetoothAdapter.isEnabled();
        } else {
            displaySnackBar("This device doesn't support Bluetooth");
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isAlertDialogInflated = false;
        if (requestCode == Constants.REQUEST_PERMISSION_ALL) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // Called when user selects 'NEVER ASK AGAIN'


                    } else {
                        // Called when user selects 'DENY'
                        displaySnackBar("Enable permission");
                    }
                }
            }

        }
    }
    private void enableBluetoothRequest() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BLUETOOTH);
    }

    private final ScanSettings bluetoothLeScanSettings = new ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .build();

    /**
     * Initializing 'ScanCallback' parameter for 'BLE device Scanning'
     *
     * NOTE: onScanResult is triggered whenever a BLE device, matching the
     *       ScanFilter and ScanSettings is found.
     *       In this callback, we get access to the BluetoothDevice and RSSI
     *       objects through the ScanResult
     */
    private final ScanCallback bluetoothLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice bluetoothDevice = result.getDevice();

            // Append device to Scanned devices list
            if (bluetoothDevice.getName() != null) {
                if (!mBleDeviceList.contains(bluetoothDevice)) {
                    Log.d(TAG, "onScanResult: Adding "+bluetoothDevice.getName()+" to list");
                    mBleDeviceList.add(bluetoothDevice);

                    changeVisibility(mRecyclerView, View.VISIBLE);
                    changeVisibility(mScanningLottieView, View.GONE);

                   // mRvAdapter.setDeviceList(mBleDeviceList);
                   // mRvAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(TAG, "onScanFailed() called with: errorCode = [" + errorCode + "]");
        }
    };

    /**
     * Prepare Custom Alert-Dialog
     */
    private void prepareAlertDialog() {
        // Inflate custom layout
      /*  mCustomAlertView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_device_scan, null, false);

        mRecyclerView = mCustomAlertView.findViewById(R.id.rvScannedDevices);
        mScanningLottieView = mCustomAlertView.findViewById(R.id.lottieViewScanning);

        displayDataInRecyclerView(mRecyclerView);*/
    }

    private void updateAdapterConnectionState(int position) {
      //  mRvAdapter.setCurrentDeviceState(mCurrentState, position);
      //  mRvAdapter.notifyDataSetChanged();
    }
}