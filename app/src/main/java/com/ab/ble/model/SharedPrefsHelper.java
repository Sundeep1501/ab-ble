package com.ab.ble.model;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper {

    private static final String MY_PREFS = "BLE_PREFS";

    private static final String CONTINUOUS_SCAN = "CONTINUOUS_SCAN";
    private static final String MANUAL_SECONDS = "MANUAL_SECONDS";

    private SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    public void putManualSecs(int secs) {
        mSharedPreferences.edit().putInt(MANUAL_SECONDS, secs).apply();
    }

    public int getManualSecs() {
        return mSharedPreferences.getInt(MANUAL_SECONDS, 2);
    }

    public boolean isContinuousScan() {
        return mSharedPreferences.getBoolean(CONTINUOUS_SCAN, true);
    }

    public void setContinuousScan(boolean continuousScan) {
        mSharedPreferences.edit().putBoolean(CONTINUOUS_SCAN, continuousScan).apply();
    }

}