package com.ab.ble

import android.app.Application
import com.ab.ble.data.local.SharedPrefsHelper
import com.ab.ble.repository.SPRepository

/**
 * Created by sunde_000 on 05/10/2017.
 */

class BleApplication : Application() {
    var spRepository: SPRepository? = null
        private set

    override fun onCreate() {
        super.onCreate()
        val context = applicationContext
        spRepository = SPRepository(SharedPrefsHelper(context))
    }

}