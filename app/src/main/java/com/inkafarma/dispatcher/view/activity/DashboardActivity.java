package com.inkafarma.dispatcher.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.inkafarma.dispatcher.BuildConfig;
import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.presenter.presenter.DashboardPresenter;
import com.inkafarma.dispatcher.presenter.presenterImpl.DashboardPresenterImpl;
import com.inkafarma.dispatcher.presenter.util.DialogUtils;
import com.inkafarma.dispatcher.presenter.util.DispatcherGlobal;
import com.inkafarma.dispatcher.presenter.util.Util;
import com.inkafarma.dispatcher.view.base.BaseActivity;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_DISPATCHER;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_LIQUIDATE;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_LOGIN;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_NAME;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_SCAN;
import static com.inkafarma.dispatcher.presenter.util.Util.isActivityShow;

public class DashboardActivity extends BaseActivity implements DashboardPresenterImpl.DashboardView {


    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_version_app)
    TextView tvVersionApp;
    @BindView(R.id.progress_overlay)
    View progressOverlay;
    @BindView(R.id.ll_dispatcher)
    LinearLayout llDispatcher;
    @BindView(R.id.ll_liquidate)
    LinearLayout llLiquidate;

    private TextView textView;
    private TextView textFullView;
    private TextView textPhone;
    private CircleImageView imageView;
    private LinearLayoutManager layoutManager;
    private DashboardPresenter dashboardPresenter;
    private int resultLogin = 0;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        layoutManager = new LinearLayoutManager(this);
        dashboardPresenter = new DashboardPresenterImpl(this, this, resultLogin);
        dashboardPresenter.init();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        tvVersionApp.setText(getString(R.string.version_app, BuildConfig.VERSION_NAME.toString()));
        setupNavigationDrawerContent(navigationView);
    }

    private void setupNavigationDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                switch (item.getItemId()) {
                    case R.id.menu_logout:
                        drawer.closeDrawer(GravityCompat.START);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        showDialog();
                        dashboardPresenter.logout(DashboardActivity.this);
                        break;
                }
                return false;
            }
        });
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
    protected int getLayoutId() {
        return R.layout.activity_dashboard;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return false;
    }

    @Override
    public void dispatcherAlias(String alias, String fullName, String url, String phone) {
        View v = navigationView.getHeaderView(0);

        textFullView = ButterKnife.findById(v, R.id.textView_full_name_motorized);
        textFullView.setText(Html.fromHtml(getString(R.string.nombres, fullName)));

        imageView = ButterKnife.findById(v, R.id.imageView_motorized);

        if (url != null && !url.isEmpty()) {
            Picasso.with(getApplicationContext())
                    .load(url)
                    .into(imageView);
        } else {
            imageView.setBackgroundResource(R.drawable.profile_image);
        }
    }

    @Override
    public void showAlertDialogImei() {

        if (!((Activity) this).isFinishing()) {
            DialogUtils.showDialog(this,
                    R.string.empty,
                    this.getResources().getString(R.string.cannot_display_session),
                    R.string.cancel,
                    this,
                    R.drawable.ic_warrning);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Método propio de la implementacion de la la interface
     * {@Link com.inkafarma.inkapicker.presenter.presenterImpl.LoginPresenterImpl.View}.
     */
    @Override
    public void showDialog() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    /**
     * Método propio de la implementacion de la la interface
     * {@Link com.inkafarma.inkapicker.presenter.presenterImpl.LoginPresenterImpl.View}.
     */
    @Override
    public void dismissDialog() {
        progressOverlay.setVisibility(View.GONE);
    }

    @Override
    public void dismissDialogLogout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        progressOverlay.setVisibility(View.GONE);
    }

    @Override
    public void dismissDialogLogoutError() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        progressOverlay.setVisibility(View.GONE);
        Toast.makeText(this, "Se produjo un error el cerrar sesión, intente nuevamente",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void logoutImei() {
        if (!isActivityShow(this, ACTIVITY_LOGIN)) {
            dashboardPresenter.logoutImei();
            dashboardPresenter.stopListener();
            DispatcherGlobal.logoutDashboard = true;
            if (!isActivityShow(this, ACTIVITY_LOGIN)) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    public void logoutUser(boolean isError) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        dismissDialogLogout();
        finish();
        if (isError)
            Toast.makeText(this, getResources().getString(R.string.get_profits_error_message),
                    Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @OnClick(R.id.ll_dispatcher)
    public void  startDispatcher() {
        if (!isActivityShow(this, ACTIVITY_SCAN)) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ACTIVITY_NAME, ACTIVITY_DISPATCHER);
            startActivityForResult(intent, 0);
        }
    }

    @OnClick(R.id.ll_liquidate)
    public void  startLiquidate() {
        if (!isActivityShow(this, ACTIVITY_SCAN)) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.putExtra(ACTIVITY_NAME, ACTIVITY_LIQUIDATE);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void showMessage(boolean exists) {
//        if (!((Activity) this).isFinishing()) {
//            String message = "";
//            if (exists)
//                message = this.getResources()
//                        .getString(R.string.cannot_display_order_status, orderIdSearch);
//            else
//                message = this.getResources()
//                        .getString(R.string.cannot_display_order_not_found, orderIdSearch);
//
//            DialogUtils.showDialog(this,
//                    R.string.empty,
//                    message,
//                    R.string.cancel,
//                    this,
//                    R.drawable.ic_warrning);
//        }
    }
}
