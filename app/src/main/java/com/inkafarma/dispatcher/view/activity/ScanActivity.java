package com.inkafarma.dispatcher.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.presenter.presenter.ScanPresenter;
import com.inkafarma.dispatcher.presenter.presenterImpl.ScanPresenterImpl;
import com.inkafarma.dispatcher.presenter.util.DialogUtils;
import com.inkafarma.dispatcher.view.base.BaseActivity;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_DASHBOARD;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_DISPATCHER;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_LIQUIDATE;
import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_NAME;
import static com.inkafarma.dispatcher.presenter.util.Constants.JSON_MOTORIZED_ORDER;
import static com.inkafarma.dispatcher.presenter.util.Util.isActivityShow;

public class ScanActivity extends BaseActivity implements ScanPresenterImpl.ScanView {

    @BindView(R.id.progress_overlay)
    View progressOverlay;
    @BindView(R.id.editText_dni)
    EditText editTextDni;
    @BindView(R.id.accept_dni)
    ImageButton acceptDni;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private String nameClass;
    private ScanPresenter scanPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        scanPresenter = new ScanPresenterImpl(this, this);
        beepManager = new BeepManager(this);
        this.nameClass = getIntent().getExtras().getString(ACTIVITY_NAME);
        barcodeView.resume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        changeNumberButton(false);

    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            System.out.println("***** lastText --> " + lastText);
            beepManager.playBeepSoundAndVibrate();
            barcodeView.pause();
            showDialog();
            scanPresenter.getDispatcher(lastText);

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void showMessage(){
        DialogUtils.showDialog(this,
                R.string.empty,
                getString(R.string.message_not_found_dni, lastText),
                R.string.accept,
                this,
                R.drawable.ic_warrning);
        barcodeView.resume();
    }

    public void showDialog() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissDialog() {
        progressOverlay.setVisibility(View.GONE);
    }

    @OnClick(R.id.scan_capture_button)
    public void  cancelScan() {
        if (!isActivityShow(this, ACTIVITY_DASHBOARD)) {
            onBackPressed();
        }
    }

    @Override
    public void startActivity(MotorizedOrderModel motorizedOrderModel) {
        String gsonMotorizedOrderModel = new Gson().toJson(motorizedOrderModel);
        if (ACTIVITY_DISPATCHER.equals(nameClass)) {
            if (!isActivityShow(ScanActivity.this, ACTIVITY_DISPATCHER)) {
                Intent intent = new Intent(ScanActivity.this, DispatcherActivity.class);
                intent.putExtra(JSON_MOTORIZED_ORDER, gsonMotorizedOrderModel);
                startActivityForResult(intent, 0);
                finish();
            }
        } else if (ACTIVITY_LIQUIDATE.equals(nameClass)) {
            if (!isActivityShow(ScanActivity.this, ACTIVITY_LIQUIDATE)) {
                Intent intent = new Intent(ScanActivity.this, LiquidateActivity.class);
                intent.putExtra(JSON_MOTORIZED_ORDER, gsonMotorizedOrderModel);
                startActivityForResult(intent, 0);
                finish();
            }
        }
    }

    @OnTextChanged(R.id.editText_dni)
    public void onPasswordTextChanged() {
        checkFields(editTextDni.getText().toString());
    }

    private void checkFields(String dni) {
        if (!dni.isEmpty() && dni.length() >= 8) {
            changeNumberButton(true);
            return;
        }
        changeNumberButton(false);
    }

    private void changeNumberButton(boolean enabled) {
        acceptDni.setImageResource(enabled
                ? R.drawable.group_enter_3
                : R.drawable.group_enter);
        acceptDni.setEnabled(enabled);
    }

    @OnClick(R.id.accept_dni)
    void login() {
        lastText = editTextDni.getText().toString();
        System.out.println("##### lastText --> " + lastText);
        beepManager.playBeepSoundAndVibrate();
        barcodeView.pause();
        showDialog();
        scanPresenter.getDispatcher(lastText);
    }
}
