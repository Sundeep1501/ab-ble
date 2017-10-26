package com.ab.ble.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {

    private static final String MY_PREFS = "BLE_PREFS";

    private static final String CONTINUOUS_SCAN = "CONTINUOUS_SCAN";
    private static final String SCAN_TIME = "SCAN_TIME";

    private SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public void setScanTime(int secs) {
        mSharedPreferences.edit().putInt(SCAN_TIME, secs).apply();
    }

    public int getScanTime() {
        return mSharedPreferences.getInt(SCAN_TIME, 10);
    }

    public boolean isContinuousScan() {
        return mSharedPreferences.getBoolean(CONTINUOUS_SCAN, false);
    }

    public void setContinuousScan(boolean continuousScan) {
        mSharedPreferences.edit().putBoolean(CONTINUOUS_SCAN, continuousScan).apply();
    }

}