package com.ab.ble.presenter;

import com.ab.ble.view.MainMvpView;
import com.ab.ble.view.MvpView;

/**
 * Created by sunde_000 on 09/10/2017.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void decideAutoScan();
}
