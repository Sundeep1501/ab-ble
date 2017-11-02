package com.ab.ble.repository

import com.ab.ble.data.local.SharedPrefsHelper

class SPRepository(private val mSharedPrefsHelper: SharedPrefsHelper) {

    var scanTime: Int
        get() = mSharedPrefsHelper.scanTime
        set(secs) {
            mSharedPrefsHelper.scanTime = secs
        }

    var isContinuousScan: Boolean
        get() = mSharedPrefsHelper.isContinuousScan
        set(value) {
            mSharedPrefsHelper.isContinuousScan = value
        }

    fun clear() {
        mSharedPrefsHelper.clear()
    }

    companion object {

        private val TAG = SPRepository::class.java.simpleName
    }
}