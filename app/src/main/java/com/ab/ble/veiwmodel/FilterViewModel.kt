package com.ab.ble.veiwmodel

import android.app.Application
import android.arch.lifecycle.MutableLiveData

/**
 * Created by sunde_000 on 21/11/2017.
 */

class FilterViewModel(application: Application) : BaseAndroidViewModel(application){

    val isRssiFilter = MutableLiveData<Boolean>()
    val isNameFilter = MutableLiveData<Boolean>()
    val isMacFilter = MutableLiveData<Boolean>()


    init {
        isRssiFilter.value = mSPRepository!!.isRssiFilterApplied
        isNameFilter.value = mSPRepository!!.isNameFilterApplied
        isMacFilter.value = mSPRepository!!.isMacFilterApplied
    }
}