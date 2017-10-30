package com.ab.ble.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.ab.ble.R;
import com.ab.ble.veiwmodel.SettingsViewModel;

public class SettingsActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    private SettingsViewModel mViewModel;

    private RadioButton mContinuousRB;
    private RadioButton mManualRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mContinuousRB = findViewById(R.id.continuous_rb);
        mManualRB = findViewById(R.id.manual_rb);

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        mViewModel.getIsContinuousScan().observe(this, this::updateDataAndUI);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continuous_layout:
                mViewModel.onContinuousLayoutClicked();
                break;
            case R.id.manual_layout:
                mViewModel.onManualLayoutClicked();
                break;
        }
    }

    private void updateDataAndUI(boolean isContinuous) {
        mContinuousRB.setChecked(isContinuous);
        mManualRB.setChecked(!isContinuous);
    }
}