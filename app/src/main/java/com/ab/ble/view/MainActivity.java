package com.ab.ble.view;

import android.Manifest;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ab.ble.R;
import com.ab.ble.veiwmodel.MainViewModel;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements LifecycleRegistryOwner, NavigationView.OnNavigationItemSelectedListener {

    private static final String PERMISSION_TAG = "RxPermissions";
    private static final String TAG = MainActivity.class.getSimpleName();

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    private MainViewModel mViewModel;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // rx permissions init
        rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);

        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //init ViewModel
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getReason().observe(this, this::onScanFailure);
        mViewModel.getScanStatus().observe(this, this::onScanStatusChanged);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        mViewModel.bindService(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        mViewModel.stopScan();
        mViewModel.unbindService(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_scan) {
            mViewModel.onScanButtonClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onPermissionChanged(Boolean granted) {
        Log.i(PERMISSION_TAG, "Permissions" + (granted ? "" : " not") + " Granted");
        if (granted) {
            // All requested permissions are granted
        } else {
            // At least one permission is denied
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    private void onScanStatusChanged(boolean isScanning) {

        Toast.makeText(this, isScanning ? "Scanning" : "Stopped", Toast.LENGTH_SHORT).show();
    }

    private void onScanFailure(int reason) {
        final String text;
        switch (reason) {
            case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                text = "Bluetooth is not available";
                Toast.makeText(this, "Bluetooth not available", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case BleScanException.BLUETOOTH_DISABLED:
                text = "Enable bluetooth and try again";
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBtIntent);
                break;
            case BleScanException.LOCATION_PERMISSION_MISSING:
                text = "On Android 6.0 location permission is required. Implement Runtime Permissions";
                rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .subscribe(this::onPermissionChanged);
                break;
            case BleScanException.LOCATION_SERVICES_DISABLED:
                text = "Location services needs to be enabled on Android 6.0";
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                break;
            case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                text = "Scan with the same filters is already started";
                break;
            case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                text = "Failed to register application for bluetooth scan";
                break;
            case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                text = "Scan with specified parameters is not supported";
                break;
            case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                text = "Scan failed due to internal error";
                break;
            case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                text = "Scan cannot start due to limited hardware resources";
                break;
/*
case BleScanException.UNDOCUMENTED_SCAN_THROTTLE:
    text = String.format(
            Locale.getDefault(),
            "Android 7+ does not allow more scans. Try in %d seconds",
            secondsTill(bleScanException.getRetryDateSuggestion())
    );
    break;
*/
            case BleScanException.UNKNOWN_ERROR_CODE:
            case BleScanException.BLUETOOTH_CANNOT_START:
            default:
                text = "Unable to start scanning";
                break;
        }
        Log.w(TAG, text);
    }

    private long secondsTill(Date retryDateSuggestion) {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.getTime() - System.currentTimeMillis());
    }

}