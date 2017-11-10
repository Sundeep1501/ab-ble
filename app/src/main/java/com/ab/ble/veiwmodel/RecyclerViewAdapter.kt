package com.ab.ble.veiwmodel

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.ab.ble.R
import com.ab.ble.data.ble.model.BleDevice

class RecyclerViewAdapter(private val mDeviceList: MutableList<BleDevice>) : RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_ble_device, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val borrowModel = mDeviceList[position]
        holder.name.text = if (TextUtils.isEmpty(borrowModel.name)) "n/a" else borrowModel.name
        holder.mac.text = borrowModel.macAddress
        holder.rssi.text = borrowModel.rssi.toString()
    }

    override fun getItemCount(): Int {
        return mDeviceList.size
    }

    fun addItems(newList: List<BleDevice>) {
        val diffResult = DiffUtil.calculateDiff(BleDeviceDiffCallback(mDeviceList, newList))
        mDeviceList.clear()
        mDeviceList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val mac: TextView = view.findViewById(R.id.mac)
        val rssi: TextView = view.findViewById(R.id.rssi)

    }
}