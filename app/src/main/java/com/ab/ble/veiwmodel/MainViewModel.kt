package com.ab.ble.veiwmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.content.*
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.ab.ble.data.ble.MyService
import com.ab.ble.data.ble.model.BleDevice
import java.util.*

/**
 * Created by sunde_000 on 25/10/2017.
 */

class MainViewModel(application: Application) : BaseAndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName

    private var mService: MyService? = null
    private var mBound: Boolean = false
    val mReason: MutableLiveData<Int> = MutableLiveData()
    val mScanning: MutableLiveData<Boolean> = MutableLiveData()

    private val bleDeviceList = ArrayList<BleDevice>()

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val localBinder = iBinder as MyService.LocalBinder
            mService = localBinder.service
            mBound = true
            startScan()
            Log.i(TAG, "Service Connected")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mBound = false
            Log.i(TAG, "Service Disconnected")
        }
    }

    private val deviceFoundReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                MyService.ACTION_DEVICE_FOUND -> {
                    val bleDevice = intent.getParcelableExtra<BleDevice>(intent.action)
                    if (bleDeviceList.contains(bleDevice)) {
                        bleDeviceList.remove(bleDevice)
                    }
                    bleDeviceList.add(bleDevice)
                    Log.i(TAG, "Device found " + bleDevice.macAddress)
                }
                MyService.ACTION_SCAN_EXCEPTION -> {
                    val reason = intent.getIntExtra(MyService.ACTION_SCAN_EXCEPTION, -1)
                    mReason.value = reason
                }
                MyService.ACTION_SCAN_STOPPED -> mScanning.value = false
                MyService.ACTION_SCAN_STARTED -> mScanning.value = true
            }
        }
    }

    init {
        mBound = false
        mReason.value = -1
        mScanning.value = false
    }

    fun bindService(activityContext: Context) {
        // bind to service
        val intent = Intent(activityContext, MyService::class.java)
        activityContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)

        val intentFilter = IntentFilter(MyService.ACTION_DEVICE_FOUND)
        intentFilter.addAction(MyService.ACTION_SCAN_EXCEPTION)
        intentFilter.addAction(MyService.ACTION_SCAN_STOPPED)
        intentFilter.addAction(MyService.ACTION_SCAN_STARTED)
        LocalBroadcastManager.getInstance(activityContext).registerReceiver(deviceFoundReceiver, intentFilter)
    }

    fun unbindService(activityContext: Context) {
        LocalBroadcastManager.getInstance(activityContext).unregisterReceiver(deviceFoundReceiver)

        // Unbind from the service
        if (mBound) {
            activityContext.unbindService(mServiceConnection)
            mBound = false
        }
    }

    private fun startScan() {
        if (mBound) {
            if (mSPRepository!!.isContinuousScan) {
                mService!!.startScan()
            } else {
                mService!!.startScan(mSPRepository!!.scanTime)
            }
        }
    }

    fun stopScan() {
        if (mBound) {
            mService!!.stopScan()
        }
    }

    fun onScanButtonClicked() {
        if (mScanning.value!!) {
            stopScan()
        } else {
            startScan()
        }
    }
}