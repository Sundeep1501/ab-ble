package com.ab.ble;

import android.app.Application;

import com.ab.ble.dagger.AppComponent;
import com.ab.ble.dagger.AppModule;
import com.ab.ble.dagger.DaggerAppComponent;

/**
 * Created by sunde_000 on 05/10/2017.
 */

public class BleApplication extends Application {

    public AppComponent appComponent;

    AppComponent getAppComponent() {
        return appComponent;
    }

    protected AppComponent initDagger(BleApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }
}
