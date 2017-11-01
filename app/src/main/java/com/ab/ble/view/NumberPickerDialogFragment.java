package com.ab.ble.view;

import android.app.Dialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import com.ab.ble.R;
import com.ab.ble.veiwmodel.SettingsViewModel;

/**
 * Created by sunde_000 on 30/10/2017.
 */

public class NumberPickerDialogFragment extends DialogFragment implements LifecycleRegistryOwner {

    public static void showFragment(FragmentManager fragmentManager) {
        DialogFragment fragment = new NumberPickerDialogFragment();
        fragment.show(fragmentManager, NumberPickerDialogFragment.class.getSimpleName());
    }

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    private NumberPicker mNumberPicker;
    private SettingsViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
        mViewModel.scanTime.observe(this, this::onScanTimeChanged);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_number_picker, null);
        mNumberPicker = view.findViewById(R.id.number_picker);
        mNumberPicker.setMinValue(getResources().getInteger(R.integer.scan_time_min));
        mNumberPicker.setMaxValue(getResources().getInteger(R.integer.scan_time_max));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.dialog_title_scan_time_picker);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, this::onPositiveButtonClick);
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }

    private void onScanTimeChanged(int secs) {
        mNumberPicker.setValue(secs);
    }

    public void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        mViewModel.setScanTime(mNumberPicker.getValue());
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}