package com.ab.ble.dagger;

import com.ab.ble.MainActivity;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by sunde_000 on 05/10/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity target);
}
