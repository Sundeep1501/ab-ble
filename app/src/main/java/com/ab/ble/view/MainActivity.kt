package com.ab.ble.view

import android.Manifest
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ab.ble.R
import com.ab.ble.veiwmodel.MainViewModel
import com.polidea.rxandroidble.exceptions.BleScanException
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LifecycleRegistryOwner, NavigationView.OnNavigationItemSelectedListener {

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var mViewModel: MainViewModel? = null

    private var rxPermissions: RxPermissions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // rx permissions init
        rxPermissions = RxPermissions(this)
        rxPermissions!!.setLogging(true)

        setContentView(R.layout.activity_main)

        // toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { }

        // drawer
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        //init ViewModel
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mViewModel!!.mReason.observe(this, Observer<Int> { reason -> this.onScanFailure(reason!!) })
        mViewModel!!.mScanning.observe(this, Observer<Boolean> { status -> this.onScanStatusChanged(status!!) })
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        mViewModel!!.bindService(this)
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        mViewModel!!.stopScan()
        mViewModel!!.unbindService(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_scan) {
            mViewModel!!.onScanButtonClicked()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun onPermissionChanged(granted: Boolean?) {
        Log.i(PERMISSION_TAG, "Permissions" + (if (granted!!) "" else " not") + " Granted")
        if (granted) {
            // All requested permissions are granted
        } else {
            // At least one permission is denied
        }
    }

    override fun getLifecycle(): LifecycleRegistry {
        return lifecycleRegistry
    }

    private fun onScanStatusChanged(isScanning: Boolean) {
        Toast.makeText(this, if (isScanning) "Scanning" else "Stopped", Toast.LENGTH_SHORT).show()
    }

    private fun onScanFailure(reason: Int) {
        val text: String
        when (reason) {
            BleScanException.BLUETOOTH_NOT_AVAILABLE -> {
                text = "Bluetooth is not available"
                Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_SHORT).show()
                finish()
            }
            BleScanException.BLUETOOTH_DISABLED -> {
                text = "Enable bluetooth and try again"
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(enableBtIntent)
            }
            BleScanException.LOCATION_PERMISSION_MISSING -> {
                text = "On Android 6.0 location permission is required. Implement Runtime Permissions"
                rxPermissions!!.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(Consumer<Boolean> { this.onPermissionChanged(it) })
            }
            BleScanException.LOCATION_SERVICES_DISABLED -> {
                text = "Location services needs to be enabled on Android 6.0"
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            BleScanException.SCAN_FAILED_ALREADY_STARTED -> text = "Scan with the same filters is already started"
            BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> text = "Failed to register application for bluetooth scan"
            BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED -> text = "Scan with specified parameters is not supported"
            BleScanException.SCAN_FAILED_INTERNAL_ERROR -> text = "Scan failed due to internal error"
            BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES -> text = "Scan cannot start due to limited hardware resources"
        /*
case BleScanException.UNDOCUMENTED_SCAN_THROTTLE:
    text = String.format(
            Locale.getDefault(),
            "Android 7+ does not allow more scans. Try in %d seconds",
            secondsTill(bleScanException.getRetryDateSuggestion())
    );
    break;
*/
            BleScanException.UNKNOWN_ERROR_CODE, BleScanException.BLUETOOTH_CANNOT_START -> text = "Unable to start scanning"
            else -> text = "Unable to start scanning"
        }
        Log.w(TAG, text)
    }

    private fun secondsTill(retryDateSuggestion: Date): Long {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.time - System.currentTimeMillis())
    }

    companion object {

        private val PERMISSION_TAG = "RxPermissions"
        private val TAG = MainActivity::class.java.simpleName
    }

}