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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunde_000 on 25/10/2017.
 */

public class MainViewModel extends AndroidViewModel {

    private final String TAG = MainViewModel.class.getSimpleName();
    private SPRepository mSPRepository;

    private MyService mService;
    private boolean mBound;
    private MutableLiveData<Integer> mReason;
    private MutableLiveData<Boolean> mScanning;

    private List<BleDevice> bleDeviceList = new ArrayList<>();

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.LocalBinder localBinder = (MyService.LocalBinder) iBinder;
            mService = localBinder.getService();
            mBound = true;
            startScan();
            Log.i(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
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
                    mReason.setValue(reason);
                    break;
                case MyService.ACTION_SCAN_STOPPED:
                    mScanning.setValue(false);
                    break;
                case MyService.ACTION_SCAN_STARTED:
                    mScanning.setValue(true);
                    break;
            }
        }
    };

    public MainViewModel(Application application) {
        super(application);
        this.mSPRepository = ((BleApplication) application).getSPRepository();
        mReason = new MutableLiveData<>();
        mReason.setValue(-1);

        mBound = false;

        mScanning = new MutableLiveData<>();
        mScanning.setValue(false);
    }

    public void bindService(Context activityContext) {
        // bind to service
        Intent intent = new Intent(activityContext, MyService.class);
        activityContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

        IntentFilter intentFilter = new IntentFilter(MyService.ACTION_DEVICE_FOUND);
        intentFilter.addAction(MyService.ACTION_SCAN_EXCEPTION);
        intentFilter.addAction(MyService.ACTION_SCAN_STOPPED);
        intentFilter.addAction(MyService.ACTION_SCAN_STARTED);
        LocalBroadcastManager.getInstance(activityContext).registerReceiver(deviceFoundReceiver, intentFilter);
    }

    public void unbindService(Context activityContext) {
        LocalBroadcastManager.getInstance(activityContext).unregisterReceiver(deviceFoundReceiver);

        // Unbind from the service
        if (mBound) {
            activityContext.unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private void startScan() {
        if (mBound) {
            if (isContinuousScan()) {
                mService.startScan();
            } else {
                mService.startScan(getScanTime());
            }
        }
    }

    public void stopScan() {
        if (mBound) {
            mService.stopScan();
        }
    }

    public void onScanButtonClicked() {
        if (mScanning.getValue()) {
            stopScan();
        } else {
            startScan();
        }
    }

    private boolean isContinuousScan() {
        return mSPRepository.isContinuousScan();
    }

    private int getScanTime() {
        return mSPRepository.getScanTime();
    }

    public LiveData<Integer> getReason() {
        return mReason;
    }

    public LiveData<Boolean> getScanStatus() {
        return mScanning;
    }
}