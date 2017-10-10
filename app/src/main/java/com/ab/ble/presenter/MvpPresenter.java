package com.ab.ble.presenter;

import com.ab.ble.view.MvpView;

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

}