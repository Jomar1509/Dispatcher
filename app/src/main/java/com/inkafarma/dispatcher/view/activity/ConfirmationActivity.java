package com.inkafarma.dispatcher.view.activity;

import android.widget.LinearLayout;

import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.view.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfirmationActivity extends BaseActivity {
    @BindView(R.id.scan_capture_button)
    LinearLayout scanCaptureButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirmation;
    }

    @OnClick(R.id.scan_capture_button)
    public void onClickReset() {
        onBackPressed();
    }
}
