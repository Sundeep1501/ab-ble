package com.ab.ble.data.ble;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ab.ble.data.ble.model.BleDevice;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    public static final String ACTION_DEVICE_FOUND = "deviceFound";
    public static final String ACTION_SCAN_EXCEPTION = "scanException";

    private RxBleClient mRxBleClient;
    private Subscription mScanSubscription;

    private IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MyService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MyService.this;
        }
    }

    public MyService() {
    }

    /**
     * The system invokes this method to perform one-time setup procedures when the service is initially created
     * (before it calls either onStartCommand() or onBind()).
     * <p>
     * If the service is already running, this method is not called.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Created");
        mRxBleClient = RxBleClient.create(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service Destroyed");
    }

    /**
     * Method for clients to start the scan
     */
    public void startScan() {
        mScanSubscription = mRxBleClient.scanBleDevices(new ScanSettings.Builder().build())
                .doOnUnsubscribe(this::clearScanSubscription)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onScanResult, this::onScanFailure);
    }

    /**
     * Method for clients to stop the scan
     */
    public void stopScan() {
        if (mScanSubscription != null) {
            mScanSubscription.unsubscribe();
        }
    }

    /**
     * Method to tell whether service is scanning
     *
     * @return true is the service is scanning
     */
    public boolean isScanning() {
        return mScanSubscription != null;
    }

    /**
     * get the state of the ble
     *
     * @return {@link com.polidea.rxandroidble.RxBleClient.State}
     */
    public RxBleClient.State getBleState() {
        return mRxBleClient.getState();
    }

    private void onScanFailure(Throwable throwable) {
        if (throwable instanceof BleScanException) {
            handleBleScanException((BleScanException) throwable);
        }
    }

    private void onScanResult(ScanResult scanResult) {
        Intent intent = new Intent(ACTION_DEVICE_FOUND);
        intent.putExtra(ACTION_DEVICE_FOUND, BleDevice.getInstance(scanResult));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void clearScanSubscription() {
        mScanSubscription = null;
        Log.i(TAG, "Service Scan unsubscribed");
    }

    private void handleBleScanException(BleScanException bleScanException) {
        Intent intent = new Intent(ACTION_SCAN_EXCEPTION);
        intent.putExtra(ACTION_SCAN_EXCEPTION, bleScanException.getReason());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}