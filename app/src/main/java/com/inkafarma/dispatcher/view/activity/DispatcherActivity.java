package com.inkafarma.dispatcher.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.data.model.MotorizeModel;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.data.model.OrderModel;
import com.inkafarma.dispatcher.data.model.PosModel;
import com.inkafarma.dispatcher.presenter.util.Constants;
import com.inkafarma.dispatcher.presenter.util.Util;
import com.inkafarma.dispatcher.view.adapter.OrderPosAdapter;
import com.inkafarma.dispatcher.view.base.BaseActivity;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_CONFIRMATION;
import static com.inkafarma.dispatcher.presenter.util.Constants.JSON_MOTORIZED_ORDER;
import static com.inkafarma.dispatcher.presenter.util.Util.isActivityShow;

public class DispatcherActivity extends BaseActivity {

    @BindView(R.id.image_back)
    ImageView imageBack;
    @BindView(R.id.textView_state_scan_text)
    TextView textViewStateScanText;
    @BindView(R.id.sv_order_motorized)
    ScrollView svOrderMotorized;
    @BindView(R.id.ll_not_order_motorized)
    LinearLayout llNotOrderMotorized;
    @BindView(R.id.scan_information)
    LinearLayout scanInformation;
    @BindView(R.id.recyclerView_orders)
    RecyclerView recyclerView;
    @BindView(R.id.scan_capture_button)
    LinearLayout scanCaptureButton;
    @BindView(R.id.scan_button)
    TextView scanButton;
    @BindView(R.id.layout_scan)
    RelativeLayout layoutScan;

    private MotorizedOrderModel motorizedOrderModel;
    private OrderPosAdapter adapter = null;
    private LinearLayoutManager layoutManager;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dispatcher;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String gsonMotorizedOrder = getIntent().getExtras().getString(JSON_MOTORIZED_ORDER);
        this.motorizedOrderModel = new Gson().fromJson(gsonMotorizedOrder, MotorizedOrderModel.class);
        setTitleScan();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrderPosAdapter(this, motorizedOrderModel.getOrders(), motorizedOrderModel.getPosList());
        recyclerView.setAdapter(adapter);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
        beepManager = new BeepManager(this);
        barcodeView.pause();
    }

    private void setTitleScan() {
        if (motorizedOrderModel != null && motorizedOrderModel.getMotorized() != null) {
            MotorizeModel motorizeModel = motorizedOrderModel.getMotorized();
            textViewStateScanText.setText(getString(R.string.name_motorized,
                    (motorizeModel.getLastName() == null ? new String() : motorizeModel.getLastName()
                            + motorizeModel.getFirstName() == null ? new String() : motorizeModel.getFirstName())));
        }
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            Toast.makeText(DispatcherActivity.this, lastText, Toast.LENGTH_LONG).show();
            beepManager.playBeepSoundAndVibrate();
            if (Util.searchList(lastText, motorizedOrderModel)) {
                adapter = new OrderPosAdapter(getApplicationContext(), motorizedOrderModel.getOrders(), motorizedOrderModel.getPosList());
                recyclerView.setAdapter(adapter);
                if (isContinue()) {
                    barcodeView.pause();
                    if (!isActivityShow(DispatcherActivity.this, ACTIVITY_CONFIRMATION)) {
                        Intent intent = new Intent(DispatcherActivity.this, ConfirmationActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                    }
                }
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    private boolean isContinue() {
        if (motorizedOrderModel != null) {
            if (motorizedOrderModel.getOrders() != null
                    && !motorizedOrderModel.getOrders().isEmpty()) {
                for (OrderModel orderModel : motorizedOrderModel.getOrders()) {
                    if (!orderModel.isChecked())
                        return false;
                }
            }
            if (motorizedOrderModel.getPosList() != null
                    && !motorizedOrderModel.getPosList().isEmpty()) {
                for (PosModel posModel : motorizedOrderModel.getPosList()) {
                    if (!posModel.isChecked())
                        return false;
                }
            }
        }
        return true;
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.scan_capture_button)
    public void startScan() {
        if (getString(R.string.start_scan).equals(scanButton.getText())) {
            scanCaptureButton.setBackgroundResource(R.drawable.button_scan_red_background);
            scanButton.setText(getString(R.string.cancel_scan));
            barcodeView.resume();
            layoutScan.setVisibility(View.VISIBLE);
            textViewStateScanText.setText(getString(R.string.scan_code_bar));
        } else {
            scanCaptureButton.setBackgroundResource(R.drawable.button_green_background);
            scanButton.setText(getString(R.string.start_scan));
            barcodeView.pause();
            layoutScan.setVisibility(View.GONE);
            setTitleScan();
        }
    }
}
