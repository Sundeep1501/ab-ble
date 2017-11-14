package com.ab.ble.data.ble.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;

import java.util.Comparator;

/**
 * Created by sunde_000 on 25/10/2017.
 */

public class BleDevice implements Parcelable {

    public final String macAddress;
    public final String name;
    public final int rssi;

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

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && obj instanceof BleDevice
                && ((BleDevice) obj).macAddress.equalsIgnoreCase(macAddress);
    }

    public static class NameComparator implements Comparator<BleDevice> {

        @Override
        public int compare(BleDevice p0, BleDevice p1) {
            if (TextUtils.isEmpty(p0.name)) {
                return 1;
            }
            if (TextUtils.isEmpty(p1.name)) {
                return -1;
            }
            return p0.name.compareTo(p1.name);
        }
    }

    public static class MacComparator implements Comparator<BleDevice> {

        @Override
        public int compare(BleDevice p0, BleDevice p1) {
            return p0.macAddress.compareTo(p1.macAddress);
        }
    }

    public static class RSSIComparator implements Comparator<BleDevice> {

        @Override
        public int compare(BleDevice p0, BleDevice p1) {
            if (p0.rssi == p1.rssi) {
                return 0;
            }
            return p1.rssi - p0.rssi;
        }
    }
}