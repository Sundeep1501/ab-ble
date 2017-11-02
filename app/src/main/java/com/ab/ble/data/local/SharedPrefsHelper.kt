package com.ab.ble.data.local

import android.content.Context
import android.content.SharedPreferences

import android.content.Context.MODE_PRIVATE

class SharedPrefsHelper(context: Context) {

    private val mSharedPreferences: SharedPreferences

    var scanTime: Int
        get() = mSharedPreferences.getInt(SCAN_TIME, 10)
        set(secs) = mSharedPreferences.edit().putInt(SCAN_TIME, secs).apply()

    var isContinuousScan: Boolean
        get() = mSharedPreferences.getBoolean(CONTINUOUS_SCAN, false)
        set(continuousScan) = mSharedPreferences.edit().putBoolean(CONTINUOUS_SCAN, continuousScan).apply()

    init {
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE)
    }

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }

    companion object {

        private val MY_PREFS = "BLE_PREFS"

        private val CONTINUOUS_SCAN = "CONTINUOUS_SCAN"
        private val SCAN_TIME = "SCAN_TIME"
    }

}