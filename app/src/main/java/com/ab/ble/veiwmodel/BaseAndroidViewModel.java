package com.ab.ble.veiwmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.ab.ble.BleApplication;
import com.ab.ble.repository.SPRepository;

/**
 * Created by sundeep on 29/10/17.
 */

public class BaseAndroidViewModel extends AndroidViewModel {
    SPRepository mSPRepository;

    public BaseAndroidViewModel(Application application) {
        super(application);
        this.mSPRepository = ((BleApplication) application).getSPRepository();
    }


}
