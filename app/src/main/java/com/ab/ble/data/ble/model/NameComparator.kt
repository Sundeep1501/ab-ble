package com.ab.ble.data.ble.model

/**
 * Created by sunde_000 on 08/11/2017.
 */
class NameComparator : Comparator<BleDevice> {
    override fun compare(p0: BleDevice?, p1: BleDevice?): Int {
        return p0!!.name.compareTo(p1!!.name)
    }
}