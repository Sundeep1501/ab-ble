package com.ab.ble.veiwmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ab.ble.BleApplication;
import com.ab.ble.data.ble.MyService;
import com.ab.ble.data.ble.model.BleDevice;
import com.ab.ble.repository.SPRepository;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.exceptions.BleScanException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunde_000 on 25/10/2017.
 */

public class MainViewModel extends AndroidViewModel {

    private final String TAG = MainViewModel.class.getSimpleName();
    private SPRepository mSPRepository;

    private MyService mService;
    private MutableLiveData<Boolean> mBound;

    private List<BleDevice> bleDeviceList = new ArrayList<>();

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder localBinder = (MyService.LocalBinder) iBinder;
            mService = localBinder.getService();
            mBound.setValue(true);
            Log.i(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound.setValue(false);
            Log.i(TAG, "Service Disconnected");
        }
    };

    private BroadcastReceiver deviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MyService.ACTION_DEVICE_FOUND:
                    BleDevice bleDevice = intent.getParcelableExtra(intent.getAction());
                    if (bleDeviceList.contains(bleDevice)) {
                        bleDeviceList.remove(bleDevice);
                    }
                    bleDeviceList.add(bleDevice);
                    Log.i(TAG, "Device found " + bleDevice.getMacAddress());
                    break;
                case MyService.ACTION_SCAN_EXCEPTION:
                    int reason = intent.getIntExtra(MyService.ACTION_SCAN_EXCEPTION, -1);
                    handleException(reason);
                    break;
            }
        }
    };

    public MainViewModel(Application application) {
        super(application);
        this.mSPRepository = ((BleApplication) application).getSPRepository();
        mBound = new MutableLiveData<>();
        mBound.setValue(false);
    }

    public void bindService(Context activityContext) {
        // bind to service
        Intent intent = new Intent(activityContext, MyService.class);
        activityContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter(MyService.ACTION_DEVICE_FOUND);
        intentFilter.addAction(MyService.ACTION_SCAN_EXCEPTION);
        LocalBroadcastManager.getInstance(activityContext).registerReceiver(deviceFoundReceiver, intentFilter);
    }

    public void unbindService(Context activityContext) {
        LocalBroadcastManager.getInstance(activityContext).unregisterReceiver(deviceFoundReceiver);

        // Unbind from the service
        if (mBound.getValue()) {
            activityContext.unbindService(mServiceConnection);
            mBound.setValue(false);
        }
    }

    public void startScan() {
        if (mBound.getValue()) {
            mService.startScan();
        }
    }

    public void stopScan() {
        if (mBound.getValue()) {
            mService.stopScan();
        }
    }

    public RxBleClient.State getBleState() {
        if (mBound.getValue()) {
            return mService.getBleState();
        }
        throw new RuntimeException(TAG + " Service not bounded with ViewModel");
    }

    public boolean isContinuousScan() {
        return mSPRepository.isContinuousScan();
    }

    int getScanTime() {
        return mSPRepository.getScanTime();
    }

    private void handleException(int reason) {
        final String text;
        switch (reason) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                text = "Bluetooth is not available";
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                text = "Enable bluetooth and try again";
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                text = "On Android 6.0 location permission is required. Implement Runtime Permissions";
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                text = "Location services needs to be enabled on Android 6.0";
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                text = "Scan with the same filters is already started";
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                text = "Failed to register application for bluetooth scan";
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                text = "Scan with specified parameters is not supported";
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                text = "Scan failed due to internal error";
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                text = "Scan cannot start due to limited hardware resources";
                break;
/*
case BleScanException.UNDOCUMENTED_SCAN_THROTTLE:
    text = String.format(
            Locale.getDefault(),
            "Android 7+ does not allow more scans. Try in %d seconds",
            secondsTill(bleScanException.getRetryDateSuggestion())
    );
    break;
*/
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                text = "Unable to start scanning";
                break;
        }
        Log.w(TAG, text);
    }

    private long secondsTill(Date retryDateSuggestion) {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.getTime() - System.currentTimeMillis());
    }

    public LiveData<Boolean> getBound() {
        return mBound;
    }
}