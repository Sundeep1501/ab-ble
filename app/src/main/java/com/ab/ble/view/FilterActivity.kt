package com.ab.ble.view

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.ab.ble.R
import com.ab.ble.veiwmodel.FilterViewModel
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : BaseHomeUpActivity(), LifecycleRegistryOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private var mViewModel: FilterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        mViewModel = ViewModelProviders.of(this).get(FilterViewModel::class.java)
        mViewModel!!.isMacFilter.observe(this, Observer<Boolean> { isMacFilter -> onMacChecked(isMacFilter!!) })
        mViewModel!!.isNameFilter.observe(this, Observer<Boolean> { isNameFilter -> onNameChecked(isNameFilter!!) })
        mViewModel!!.isRssiFilter.observe(this, Observer<Boolean> { isRssiFilter -> onRssiChecked(isRssiFilter!!) })

    }

    private fun onRssiChecked(rssiFilter: Boolean) {
        rssiCB.isChecked = rssiFilter
    }

    private fun onNameChecked(nameFilter: Boolean) {
        nameCB.isChecked = nameFilter
    }

    private fun onMacChecked(isMacFilter: Boolean) {
        macCB.isChecked = isMacFilter
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }
}
