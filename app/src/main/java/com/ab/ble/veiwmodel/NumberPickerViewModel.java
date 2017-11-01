package com.ab.ble.veiwmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

/**
 * Created by sundeep on 29/10/17.
 */

public class NumberPickerViewModel extends BaseAndroidViewModel {
    private MutableLiveData<Integer> scanTime;

    public NumberPickerViewModel(Application application) {
        super(application);
        scanTime = new MutableLiveData<>();
        scanTime.setValue(getScanTime());
    }

    public int getScanTime() {
        return mSPRepository.getScanTime();
    }

    public void setScanTime(int secs) {
        mSPRepository.setScanTime(secs);
        scanTime.setValue(secs);
    }

    public MutableLiveData<Integer> getScanTimeLiveData() {
        return scanTime;
    }
}