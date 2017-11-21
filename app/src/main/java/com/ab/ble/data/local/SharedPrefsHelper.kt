package com.ab.ble.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPrefsHelper(context: Context) {

    private val mSharedPreferences: SharedPreferences

    var scanTime: Int
        get() = mSharedPreferences.getInt(SCAN_TIME, 10)
        set(secs) = mSharedPreferences.edit().putInt(SCAN_TIME, secs).apply()

    var isContinuousScan: Boolean
        get() = mSharedPreferences.getBoolean(CONTINUOUS_SCAN, false)
        set(continuousScan) = mSharedPreferences.edit().putBoolean(CONTINUOUS_SCAN, continuousScan).apply()


    var rssiFilterVal: Int
        get() = mSharedPreferences.getInt(FILTER_RSSI_VAL, -100)
        set(rssi) = mSharedPreferences.edit().putInt(FILTER_RSSI_VAL, rssi).apply()

    var nameFilterVal: String
        get() = mSharedPreferences.getString(FILTER_NAME_VAL, "")
        set(name) = mSharedPreferences.edit().putString(FILTER_NAME_VAL, name).apply()

    var macFilterVal: String
        get() = mSharedPreferences.getString(FILTER_MAC_VAL, "")
        set(mac) = mSharedPreferences.edit().putString(FILTER_MAC_VAL, mac).apply()


    var isRssiFilterApplied: Boolean
        get() = mSharedPreferences.getBoolean(FILTER_RSSI, true)
        set(isRssi) = mSharedPreferences.edit().putBoolean(FILTER_RSSI, isRssi).apply()

    var isNameFilterApplied: Boolean
        get() = mSharedPreferences.getBoolean(FILTER_NAME, false)
        set(isName) = mSharedPreferences.edit().putBoolean(FILTER_NAME, isName).apply()

    var isMacFilterApplied: Boolean
        get() = mSharedPreferences.getBoolean(FILTER_MAC, true)
        set(isMac) = mSharedPreferences.edit().putBoolean(FILTER_MAC, isMac).apply()

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
        private val FILTER_RSSI = "FILTER_RSSI"
        private val FILTER_NAME = "FILTER_NAME"
        private val FILTER_MAC = "FILTER_MAC"

        private val FILTER_RSSI_VAL = "FILTER_RSSI_VAL"
        private val FILTER_NAME_VAL = "FILTER_NAME_VAL"
        private val FILTER_MAC_VAL = "FILTER_MAC_VAL"
    }

}