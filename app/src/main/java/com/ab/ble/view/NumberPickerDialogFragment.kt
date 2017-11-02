package com.ab.ble.view

import android.app.Dialog
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.widget.NumberPicker
import com.ab.ble.R
import com.ab.ble.veiwmodel.SettingsViewModel

/**
 * Created by sunde_000 on 30/10/2017.
 */

class NumberPickerDialogFragment : DialogFragment(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var mNumberPicker: NumberPicker? = null
    private var mViewModel: SettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(activity).get(SettingsViewModel::class.java)
        mViewModel!!.scanTimeLD.observe(this, Observer<Int> { it -> this.onScanTimeChanged(it!!) })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.fragment_number_picker, null)
        mNumberPicker = view!!.findViewById(R.id.number_picker)
        mNumberPicker!!.minValue = resources.getInteger(R.integer.scan_time_min)
        mNumberPicker!!.maxValue = resources.getInteger(R.integer.scan_time_max)

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(R.string.dialog_title_scan_time_picker)
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialogInterface, i -> this.onPositiveButtonClick(dialogInterface, i) }
        builder.setNegativeButton(android.R.string.cancel, null)

        return builder.create()
    }

    private fun onScanTimeChanged(secs: Int) {
        mNumberPicker!!.value = secs
    }

    fun onPositiveButtonClick(dialogInterface: DialogInterface, i: Int) {
        mViewModel!!.onScanTimeSelected(mNumberPicker!!.value)
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    companion object {
        fun showFragment(fragmentManager: FragmentManager) {
            val fragment = NumberPickerDialogFragment()
            fragment.show(fragmentManager, NumberPickerDialogFragment::class.java.simpleName)
        }
    }
}