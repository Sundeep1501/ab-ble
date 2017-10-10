package com.ab.ble.presenter;

import com.ab.ble.model.datamngr.DataManager;
import com.ab.ble.view.MainMvpView;

/**
 * Created by sunde_000 on 09/10/2017.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {
    public MainPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void decideAutoScan() {
        if (getDataManager().isContinuousScan()) {
            getMvpView().scanForBle();
        }
    }
}
