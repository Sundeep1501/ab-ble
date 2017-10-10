package com.ab.ble.model.datamngr;

import com.ab.ble.model.SharedPrefsHelper;

public class DataManager {

    SharedPrefsHelper mSharedPrefsHelper;

    public DataManager(SharedPrefsHelper sharedPrefsHelper) {
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void clear() {
        mSharedPrefsHelper.clear();
    }

    public void saveManual(int secs) {
        mSharedPrefsHelper.putManualSecs(secs);
    }

    public int getManualSecs() {
        return mSharedPrefsHelper.getManualSecs();
    }

    public void setContinuousScan() {
        mSharedPrefsHelper.setContinuousScan(true);
    }

    public Boolean isContinuousScan() {
        return mSharedPrefsHelper.isContinuousScan();
    }
}