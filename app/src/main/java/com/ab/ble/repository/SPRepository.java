package com.ab.ble.repository;

import com.ab.ble.data.local.SharedPrefsHelper;

public class SPRepository {

    private static String TAG = SPRepository.class.getSimpleName();

    private SharedPrefsHelper mSharedPrefsHelper;

    public SPRepository(SharedPrefsHelper sharedPrefsHelper) {
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void clear() {
        mSharedPrefsHelper.clear();
    }

    public void setScanTime(int secs) {
        mSharedPrefsHelper.setScanTime(secs);
    }

    public int getScanTime() {
        return mSharedPrefsHelper.getScanTime();
    }

    public void setContinuousScan() {
        mSharedPrefsHelper.setContinuousScan(true);
    }

    public Boolean isContinuousScan() {
        return mSharedPrefsHelper.isContinuousScan();
    }
}