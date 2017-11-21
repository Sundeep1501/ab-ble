package com.ab.ble.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

/**
 * Created by sunde_000 on 21/11/2017.
 */
open class BaseHomeUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
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
}