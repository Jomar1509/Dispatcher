package com.inkafarma.dispatcher.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkafarma.dispatcher.BuildConfig;
import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.data.model.FirebaseDispatcherDeviceModel;
import com.inkafarma.dispatcher.presenter.presenter.LoginPresenter;
import com.inkafarma.dispatcher.presenter.presenterImpl.LoginPresenterImpl;
import com.inkafarma.dispatcher.presenter.util.DialogUtils;
import com.inkafarma.dispatcher.presenter.util.ui.UiComponent;
import com.inkafarma.dispatcher.view.base.BaseActivity;
import com.inkafarma.dispatcher.view.widget.LinearLayoutThatDetectsSoftKeyboard;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.inkafarma.dispatcher.presenter.util.Constants.ACTIVITY_DASHBOARD;
import static com.inkafarma.dispatcher.presenter.util.Constants.MIN_PASSWORD_LENGTH;
import static com.inkafarma.dispatcher.presenter.util.Constants.URL_PICTURE;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.imei;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isLoadHttp;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isNegativeButton;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isShowAlertChangeSesion;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.logoutDashboard;
import static com.inkafarma.dispatcher.presenter.util.Util.isActivityShow;
import static com.inkafarma.dispatcher.presenter.util.UtilPreferences.saveDispatcherLogin;

public class LoginActivity extends BaseActivity implements LoginPresenterImpl.Loginview, LinearLayoutThatDetectsSoftKeyboard.Listener {

    @BindView(R.id.progress_overlay)
    View progressOverlay;
    @BindView(R.id.login_title)
    ImageView title;
    @BindView(R.id.editText_email)
    EditText emailText;
    @BindView(R.id.editText_password)
    EditText passwordText;
    @BindView(R.id.layout_email)
    TextInputLayout emailErrorText;
    @BindView(R.id.layout_password)
    TextInputLayout passwordErrorText;
    @BindView(R.id.button_sign_in)
    Button signInButton;
    @BindView(R.id.tv_version_app)
    TextView tvVersionApp;

    private LoginPresenter loginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loginPresenter = new LoginPresenterImpl(this, this);
        tvVersionApp.setText(getString(R.string.version_app, BuildConfig.VERSION_NAME.toString()));
        LinearLayoutThatDetectsSoftKeyboard mainLayout = (LinearLayoutThatDetectsSoftKeyboard) findViewById(R.id.login_root);
        mainLayout.setListener(this);
        setPasswordFont();
        if (logoutDashboard) {
            logoutDashboard = false;
            showAlertDialogLogout();
        }
    }

    @Override
    public void loginOK() {
        saveDispatcherLogin(true, this);
        if (!isActivityShow(this, ACTIVITY_DASHBOARD)
                && !isShowAlertChangeSesion) {
            startActivityForResult(new Intent(this, DashboardActivity.class), 1);
            finish();
        }
    }

    @Override
    public void loadDialogTelephone() {
        FirebaseDispatcherDeviceModel firebaseDispatcherDeviceModel = new FirebaseDispatcherDeviceModel();
        firebaseDispatcherDeviceModel.setImei(imei);
        firebaseDispatcherDeviceModel.setPhoneMark(Build.BRAND);
        firebaseDispatcherDeviceModel.setPhoneModel(Build.MODEL);
        loginPresenter.loginDevice(firebaseDispatcherDeviceModel);
    }

    @Override
    public void showDialog() {
        progressOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissDialog() {
        progressOverlay.setVisibility(View.GONE);
    }

    @Override
    public void showAlertDialogImei() {

        if (!((Activity) this).isFinishing()) {
            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isShowAlertChangeSesion = false;
                    loginPresenter.loginFireBaseImeiOk();
                }
            };

            DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isShowAlertChangeSesion = false;
                    isNegativeButton = true;
                }
            };
            DialogUtils.showDialogImei(this,
                    R.string.empty,
                    R.string.mobile_session,
                    R.string.mobile_session_yes,
                    positiveListener,
                    negativeListener,
                    this,
                    0);
        }
    }

    @Override
    public void showAlertDialogLogout() {

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
    public void onSoftKeyboardShown(boolean isShowing) {
        if (isShowing) {
            title.setVisibility(View.GONE);
        } else {
            title.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.button_sign_in)
    void login() {
        clearFieldsFocus();

        boolean validationsOk = isValidEmail(this, emailText.getText().toString())
                && isValidPassword(this, passwordText.getText().toString());
        if (validationsOk) {
            verifyImei();
            hideKeyboard();
            isLoadHttp = false;
            isNegativeButton = false;
            loginPresenter.initLogin(emailText.getText().toString(), passwordText.getText().toString());
        }
    }

    public void clearFieldsFocus() {
        emailText.clearFocus();
        passwordText.clearFocus();
    }

    private boolean isValidEmail(Context context, String email) {
        if (UiComponent.isValidEmail(email)) {
            return true;
        }
        showEmailWarning(context.getString(R.string.error_invalid_email));
        return false;
    }

    private boolean isValidPassword(Context context, String password) {
        if (UiComponent.isValidPassword(password)) {
            return true;
        }
        showPasswordWarning(context.getString(R.string.error_password_length, MIN_PASSWORD_LENGTH));
        return false;
    }

    public void verifyImei() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            return;

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
    }

    public void hideKeyboard() {
        UiComponent.hideKeyboard(this);
    }

    public void showEmailWarning(String message) {
        emailErrorText.setError(message);
    }

    public void showPasswordWarning(String message) {
        passwordErrorText.setError(message);
    }

    private void checkFields(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            changeSignInButton(true);
            return;
        }
        changeSignInButton(false);
    }

    @OnTextChanged(R.id.editText_email)
    public void onEmailTextChanged() {
        emailErrorText.setErrorEnabled(false);
        checkFields(emailText.getText().toString(), passwordText.getText().toString());
    }

    @OnTextChanged(R.id.editText_password)
    public void onPasswordTextChanged() {
        passwordErrorText.setErrorEnabled(false);
        checkFields(emailText.getText().toString(), passwordText.getText().toString());
    }

    private void changeSignInButton(boolean enabled) {
        signInButton.setBackgroundResource(enabled
                ? R.drawable.button_login_orange_background
                : R.drawable.button_orange_disabled_background);
        signInButton.setEnabled(enabled);
    }

    private void setPasswordFont() {
        passwordErrorText.setTypeface(emailErrorText.getTypeface());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            finish();
        }
    }
}
