package com.ab.ble.veiwmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData

/**
 * Created by sundeep on 29/10/17.
 */

class SettingsViewModel(application: Application) : BaseAndroidViewModel(application) {
    val isContinuousScanLD: MutableLiveData<Boolean> = MutableLiveData()
    val scanTimeLD: MutableLiveData<Int> = MutableLiveData()

    init {
        isContinuousScanLD.value = mSPRepository!!.isContinuousScan
        scanTimeLD.value = mSPRepository!!.scanTime
    }

    fun onScanTimeSelected(secs: Int) {
        mSPRepository!!.scanTime = secs
        scanTimeLD.value = secs
    }

    fun onContinuousLayoutClicked() {
        mSPRepository!!.isContinuousScan = true
        isContinuousScanLD.value = true
    }

    fun onManualLayoutClicked() {
        mSPRepository!!.isContinuousScan = false
        isContinuousScanLD.value = false
    }
}