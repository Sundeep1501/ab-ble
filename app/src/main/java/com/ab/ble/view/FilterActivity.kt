package com.ab.ble.view

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
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

        // init value
        val progress = mViewModel!!.rssiFilterVal
        rssiTV.text = (progress * -1).toString()
        seekBar.progress = progress

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                rssiTV.text = (p0!!.progress * -1).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                mViewModel!!.onSeekBarChanged(p0!!.progress)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_save) {
            Toast.makeText(this, "Save", Toast.LENGTH_SHORT).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.rssiCB -> mViewModel!!.rssiCBClicked(rssiCB.isChecked)
            R.id.nameCB -> mViewModel!!.nameCBClicked(nameCB.isChecked)
            R.id.macCB -> mViewModel!!.macCBClicked(macCB.isChecked)
        }
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
