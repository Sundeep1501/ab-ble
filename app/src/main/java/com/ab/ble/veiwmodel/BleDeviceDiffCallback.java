package com.ab.ble.veiwmodel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.ab.ble.data.ble.model.BleDevice;

import java.util.List;
import java.util.Objects;

/**
 * Created by sunde_000 on 09/11/2017.
 */

public class BleDeviceDiffCallback extends DiffUtil.Callback {

    private List<BleDevice> oldDevices;
    private List<BleDevice> newDevices;

    public BleDeviceDiffCallback(List<BleDevice> oldList, List<BleDevice> newList) {
        this.oldDevices = oldList;
        this.newDevices = newList;
    }

    @Override
    public int getOldListSize() {
        return oldDevices == null ? 0 : oldDevices.size();
    }

    @Override
    public int getNewListSize() {
        return newDevices == null ? 0 : newDevices.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDevices.get(oldItemPosition).macAddress.equals(newDevices.get(newItemPosition).macAddress);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BleDevice newProduct = newDevices.get(newItemPosition);
        BleDevice oldProduct = oldDevices.get(oldItemPosition);
        return Objects.equals(newProduct.macAddress, oldProduct.macAddress)
                && Objects.equals(newProduct.name, oldProduct.name)
                && newProduct.rssi == oldProduct.rssi;
    }

}