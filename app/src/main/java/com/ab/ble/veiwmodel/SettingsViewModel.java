package com.ab.ble.veiwmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

/**
 * Created by sundeep on 29/10/17.
 */

public class SettingsViewModel extends BaseAndroidViewModel {
    private MutableLiveData<Boolean> isContinuousScan;

    public SettingsViewModel(Application application) {
        super(application);
        isContinuousScan = new MutableLiveData<>();
        isContinuousScan.setValue(isContinuousScan());
    }

    private boolean isContinuousScan() {
        return mSPRepository.isContinuousScan();
    }

    public int getScanTime() {
        return mSPRepository.getScanTime();
    }

    public void setScanTime(int secs) {
        mSPRepository.setScanTime(secs);
    }

    public void setContinuousScan(boolean isContinuousScan) {
        mSPRepository.setContinuousScan(isContinuousScan);
    }

    public void onContinuousLayoutClicked() {
        setContinuousScan(true);
        isContinuousScan.setValue(true);
    }

    public void onManualLayoutClicked() {
        setContinuousScan(false);
        isContinuousScan.setValue(false);
    }

    public MutableLiveData<Boolean> getIsContinuousScan() {
        return isContinuousScan;
    }
}