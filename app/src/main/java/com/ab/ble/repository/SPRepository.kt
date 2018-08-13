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

    var isRssiFilterApplied: Boolean
        get() = mSharedPrefsHelper.isRssiFilterApplied
        set(value) {
            mSharedPrefsHelper.isRssiFilterApplied = value
        }

    var isNameFilterApplied: Boolean
        get() = mSharedPrefsHelper.isNameFilterApplied
        set(value) {
            mSharedPrefsHelper.isNameFilterApplied = value
        }

    var isMacFilterApplied: Boolean
        get() = mSharedPrefsHelper.isMacFilterApplied
        set(value) {
            mSharedPrefsHelper.isMacFilterApplied = value
        }

    var rssiFilterVal: Int
        get() = mSharedPrefsHelper.rssiFilterVal
        set(value) {
            mSharedPrefsHelper.rssiFilterVal = value
        }

    var nameFilterVal: String
        get() = mSharedPrefsHelper.nameFilterVal
        set(value) {
            mSharedPrefsHelper.nameFilterVal = value
        }

    var macFilterVal: String
        get() = mSharedPrefsHelper.macFilterVal
        set(value) {
            mSharedPrefsHelper.macFilterVal = value
        }

    fun clear() {
        mSharedPrefsHelper.clear()
    }

    companion object {

        private val TAG = SPRepository::class.java.simpleName
    }
}