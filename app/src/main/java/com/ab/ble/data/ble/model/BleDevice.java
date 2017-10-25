package com.ab.ble.data.ble.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;

/**
 * Created by sunde_000 on 25/10/2017.
 */

public class BleDevice implements Parcelable {

    private final String macAddress;
    private final String name;
    private final int rssi;

    public BleDevice(String macAddress, String name, int rssi) {
        this.macAddress = macAddress;
        this.name = name;
        this.rssi = rssi;
    }

    private BleDevice(Parcel in) {
        macAddress = in.readString();
        name = in.readString();
        rssi = in.readInt();
    }

    public static final Creator<BleDevice> CREATOR = new Creator<BleDevice>() {
        @Override
        public BleDevice createFromParcel(Parcel in) {
            return new BleDevice(in);
        }

        @Override
        public BleDevice[] newArray(int size) {
            return new BleDevice[size];
        }
    };

    public static BleDevice getInstance(ScanResult scanResult) {
        RxBleDevice bleDevice = scanResult.getBleDevice();
        return new BleDevice(bleDevice.getMacAddress(), bleDevice.getName(), scanResult.getRssi());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(macAddress);
        parcel.writeString(name);
        parcel.writeInt(rssi);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getName() {
        return name;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof BleDevice
                && ((BleDevice) obj).getMacAddress().equalsIgnoreCase(getMacAddress());
    }
}
