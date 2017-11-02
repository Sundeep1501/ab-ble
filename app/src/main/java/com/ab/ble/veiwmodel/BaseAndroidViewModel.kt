package com.ab.ble.veiwmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel

import com.ab.ble.BleApplication
import com.ab.ble.repository.SPRepository

/**
 * Created by sundeep on 29/10/17.
 */

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    var mSPRepository: SPRepository? = null

    init {
        this.mSPRepository = (application as BleApplication).spRepository
    }


}
