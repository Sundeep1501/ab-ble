package com.ab.ble.view

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.TextView

import com.ab.ble.R
import com.ab.ble.veiwmodel.SettingsViewModel

class SettingsActivity : AppCompatActivity(), LifecycleRegistryOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var mViewModel: SettingsViewModel? = null

    private var mScanTimeTV: TextView? = null
    private var mContinuousRB: RadioButton? = null
    private var mManualRB: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mContinuousRB = findViewById(R.id.continuous_rb)
        mManualRB = findViewById(R.id.manual_rb)
        mScanTimeTV = findViewById(R.id.scan_time)

        mViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        mViewModel!!.isContinuousScanLD.observe(this, Observer<Boolean> { isContinuous -> updateDataAndUI(isContinuous!!) })
        mViewModel!!.scanTimeLD.observe(this, Observer<Int> { it -> onScanTimeChange(it!!) })
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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
        mContinuousRB!!.isChecked = isContinuous
        mManualRB!!.isChecked = !isContinuous
    }

    private fun onScanTimeChange(integer: Int) {
        mScanTimeTV!!.text = getString(R.string.scan_time_label, integer)
    }

    companion object {
        private val TAG = SettingsActivity::class.java.simpleName
    }
}