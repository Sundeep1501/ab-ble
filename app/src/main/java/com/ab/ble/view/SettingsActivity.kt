package com.ab.ble.view

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.ab.ble.R
import com.ab.ble.veiwmodel.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseHomeUpActivity(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var mViewModel: SettingsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        mViewModel!!.isContinuousScanLD.observe(this, Observer<Boolean> { isContinuous -> updateDataAndUI(isContinuous!!) })
        mViewModel!!.scanTimeLD.observe(this, Observer<Int> { it -> onScanTimeChange(it!!) })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.continuous_layout -> mViewModel!!.onContinuousLayoutClicked()
            R.id.manual_layout -> {
                mViewModel!!.onManualLayoutClicked()
                NumberPickerDialogFragment.showFragment(supportFragmentManager)
            }
        }
    }

    private fun updateDataAndUI(isContinuous: Boolean) {
        continuous_rb!!.isChecked = isContinuous
        manual_rb!!.isChecked = !isContinuous
    }

    private fun onScanTimeChange(integer: Int) {
        scan_time!!.text = getString(R.string.scan_time_label, integer)
    }

    companion object {
        private val TAG = SettingsActivity::class.java.simpleName
    }
}