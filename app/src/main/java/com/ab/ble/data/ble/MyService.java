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

import java.util.concurrent.TimeUnit;

import rx.Completable;
import rx.Subscription;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();

    public static final String ACTION_DEVICE_FOUND = "deviceFound";
    public static final String ACTION_SCAN_EXCEPTION = "scanException";
    public static final String ACTION_SCAN_STARTED = "scanStarted";
    public static final String ACTION_SCAN_STOPPED = "scanStopped";

    private RxBleClient mRxBleClient;
    private Subscription mScanSubscription;
    private Subscription timerSubscription;
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
        if (mRxBleClient.getState() == RxBleClient.State.READY) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_SCAN_STARTED));
        }

        mScanSubscription = mRxBleClient.scanBleDevices(new ScanSettings.Builder().build())
                .doOnUnsubscribe(this::clearScanSubscription)
                .subscribe(this::onScanResult, this::onScanFailure);
    }

    /**
     * method for clients to scan for devices for given time and stop
     *
     * @param scanTimeSecs time to scan
     */
    public void startScan(int scanTimeSecs) {
        startScan();
        timerSubscription = Completable.timer(scanTimeSecs, TimeUnit.SECONDS)
                .subscribe(this::stopScan);
    }

    /**
     * Method for clients to stop the scan
     */
    public void stopScan() {
        if (mScanSubscription != null) {
            mScanSubscription.unsubscribe();
        }
        if (timerSubscription != null) {
            timerSubscription.unsubscribe();
        }
    }

    private void onScanFailure(Throwable throwable) {
        if (throwable instanceof BleScanException) {
            Intent intent = new Intent(ACTION_SCAN_EXCEPTION);
            intent.putExtra(ACTION_SCAN_EXCEPTION, ((BleScanException) throwable).getReason());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
        Intent intent = new Intent(ACTION_SCAN_STOPPED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}