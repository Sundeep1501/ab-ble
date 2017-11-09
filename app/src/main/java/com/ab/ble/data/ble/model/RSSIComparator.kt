package com.ab.ble.data.ble.model

/**
 * Created by sunde_000 on 08/11/2017.
 */
class RSSIComparator : Comparator<BleDevice> {
    override fun compare(p0: BleDevice?, p1: BleDevice?): Int {
        if (p0!!.rssi == p1!!.rssi) {
            return 0
        }
        return p0.rssi - p1.rssi
    }

}