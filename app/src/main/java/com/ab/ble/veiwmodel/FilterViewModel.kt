package com.ab.ble.veiwmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData

/**
 * Created by sunde_000 on 21/11/2017.
 */

class FilterViewModel(application: Application) : BaseAndroidViewModel(application) {

    val isRssiFilter = MutableLiveData<Boolean>()
    val isNameFilter = MutableLiveData<Boolean>()
    val isMacFilter = MutableLiveData<Boolean>()

    val rssiFilterVal: Int
    val nameFilterVal: String
    val macFilterVal: String


    init {
        isRssiFilter.value = mSPRepository!!.isRssiFilterApplied
        isNameFilter.value = mSPRepository!!.isNameFilterApplied
        isMacFilter.value = mSPRepository!!.isMacFilterApplied

        rssiFilterVal = mSPRepository!!.rssiFilterVal
        nameFilterVal = mSPRepository!!.nameFilterVal
        macFilterVal = mSPRepository!!.macFilterVal
    }

    fun rssiCBClicked(isChecked: Boolean) {
        mSPRepository!!.isRssiFilterApplied = isChecked
        isRssiFilter.value = isChecked
    }

    fun nameCBClicked(isChecked: Boolean) {
        mSPRepository!!.isNameFilterApplied = isChecked
        isNameFilter.value = isChecked
    }

    fun macCBClicked(isChecked: Boolean) {
        mSPRepository!!.isMacFilterApplied = isChecked
        isMacFilter.value = isChecked
    }

    fun onSeekBarChanged(progress: Int) {
        mSPRepository!!.rssiFilterVal = progress
    }
}