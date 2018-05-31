package com.inkafarma.dispatcher.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.presenter.presenter.SplashScreenPresenter;
import com.inkafarma.dispatcher.presenter.presenterImpl.SplashScreenPresenterImpl;
import com.inkafarma.dispatcher.presenter.util.DispatcherGlobal;
import com.inkafarma.dispatcher.view.base.BaseActivity;

import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_DASHBOARD;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_LOGIN;
import static com.inkafarma.dispatcher.presenter.util.Util.isActivityShow;

public class SplashActivity extends BaseActivity implements SplashScreenPresenterImpl.View {

    private SplashScreenPresenter splashScreenPresenter;
    private boolean isLogin;
    private final int permisionsRequest = 123;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        splashScreenPresenter = new SplashScreenPresenterImpl(this, this);
        splashScreenPresenter.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void startActivity() {
        showPhoneStatePermission(true);
    }

    @Override
    public void startDashboard() {
        showPhoneStatePermission(false);
    }

    private void showPhoneStatePermission(boolean isLogin) {
        this.isLogin = isLogin;
        showPermission();

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            return;

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        DispatcherGlobal.imei = telephonyManager.getDeviceId();
        if (isLogin) {
            DispatcherGlobal.isDashboard = false;
            if (!isActivityShow(this, ACTIVITY_LOGIN)) {
                startActivityForResult(new Intent(this, LoginActivity.class), 0);
                finish();
            }
        } else {
            if (!isActivityShow(this, ACTIVITY_DASHBOARD)) {
                DispatcherGlobal.isDashboard = true;
                startActivityForResult(new Intent(this, DashboardActivity.class), 0);
                finish();
            }
        }
    }

    public void loadLogin() {
        if (isLogin) {
            startActivityForResult(new Intent(this, LoginActivity.class), 0);
            finish();
        } else {
            startActivityForResult(new Intent(this, DashboardActivity.class), 0);
            finish();
        }
    }

    private void showPermission() {
        String[] permisions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA, Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE};
        if (!hasPermissions(getApplicationContext(), permisions)) {
            ActivityCompat.requestPermissions(SplashActivity.this, permisions,
                    permisionsRequest);
        } else
            loadLogin();
    }

    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions)
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case permisionsRequest:
                int totalPerms = 0;
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.INTERNET)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.READ_PHONE_STATE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.ACCESS_WIFI_STATE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    } else if (permission.equals(Manifest.permission.VIBRATE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)
                            totalPerms++;
                    }
                }
                if (totalPerms == 9)
                    loadLogin();
                break;
        }
    }
}
