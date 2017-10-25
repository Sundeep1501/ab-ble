package com.ab.ble;

import android.app.Application;
import android.content.Context;

import com.ab.ble.data.local.SharedPrefsHelper;
import com.ab.ble.repository.SPRepository;

/**
 * Created by sunde_000 on 05/10/2017.
 */

public class BleApplication extends Application {
    private SPRepository SPRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        SPRepository = new SPRepository(new SharedPrefsHelper(context));
    }

    public SPRepository getSPRepository() {
        return SPRepository;
    }

}