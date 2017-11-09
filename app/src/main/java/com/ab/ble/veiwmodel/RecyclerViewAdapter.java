package com.ab.ble.veiwmodel;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.ble.R;
import com.ab.ble.data.ble.model.BleDevice;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<BleDevice> mDeviceList;

    public RecyclerViewAdapter(List<BleDevice> bleDevice) {
        mDeviceList = bleDevice;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_ble_device, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        BleDevice borrowModel = mDeviceList.get(position);
        holder.name.setText(TextUtils.isEmpty(borrowModel.name) ? "n/a" : borrowModel.name);
        holder.mac.setText(borrowModel.macAddress);
        holder.rssi.setText(String.valueOf(borrowModel.rssi));
    }

    @Override
    public int getItemCount() {
        return mDeviceList.size();
    }

    public void addItems(List<BleDevice> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BleDeviceDiffCallback(mDeviceList, newList));
        mDeviceList.clear();
        mDeviceList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView mac;
        private TextView rssi;

        RecyclerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            mac = view.findViewById(R.id.mac);
            rssi = view.findViewById(R.id.rssi);
        }
    }
}