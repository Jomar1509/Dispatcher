package com.inkafarma.dispatcher.presenter.presenterImpl;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.data.fireBase.FireBaseInteractor;
import com.inkafarma.dispatcher.data.fireBase.FireBaseListener;
import com.inkafarma.dispatcher.data.model.FireBaseDispatcherModel;
import com.inkafarma.dispatcher.data.model.FirebaseDispatcherDeviceModel;
import com.inkafarma.dispatcher.data.model.OnlineStatusModel;
import com.inkafarma.dispatcher.data.repository.InkaFarmaApi;
import com.inkafarma.dispatcher.data.repository.ws.CallbackCustom;
import com.inkafarma.dispatcher.data.repository.ws.WSConnection;
import com.inkafarma.dispatcher.presenter.presenter.LoginPresenter;
import com.inkafarma.dispatcher.presenter.util.DispatcherGlobal;
import com.inkafarma.dispatcher.presenter.util.Util;
import com.inkafarma.dispatcher.presenter.util.ui.UiComponent;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.imei;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isLoadHttp;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isNegativeButton;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isShowAlertChangeSesion;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.dispatcher;
import static com.inkafarma.dispatcher.presenter.util.UtilPreferences.getCurrentUserUid;
import static com.inkafarma.dispatcher.presenter.util.UtilPreferences.saveDispatcherUid;

/**
 * <p>
 * LoginPresenterImpl contiene interfaces implementadas
 * {@Link com.inkafarma.dispatcher.presenter.presenterImpl.LoginPresenter}
 * {@Link com.inkafarma.dispatcher.data.fireBase.FireBaseListener.FireBaseView }.
 * Aqui se maneja la logica para la coneccion con FireBase
 * y el consumo del primer Webservice en el metodo  loginFireBaseOk.
 * </p>
 */

public class LoginPresenterImpl implements LoginPresenter, FireBaseListener.FireBaseLoginView,
        DashboardPresenterImpl.ViewLogin {
    private Loginview loginview;
    private FireBaseInteractor fireBaseInteractor;
    private Context context;
    private String token;

    public LoginPresenterImpl(Loginview loginview, Context context) {
        this.loginview = loginview;
        this.context = context;
        fireBaseInteractor = new FireBaseListener(this);
    }

    @Override
    public void initLogin(String email, String pass) {
        loginview.showDialog();
        fireBaseInteractor.loginFireBase(email, pass);
    }

    /**
     * @param token
     * @param uid   Parametros de llegada, retornadas del
     */
    @Override
    public void loginFireBaseOk(String token, final String uid) {
        this.token = token;
        isShowAlertChangeSesion = false;
        System.out.println("****** uid --> " + uid);
        fireBaseInteractor.getDispatcherFromServer(uid);
    }

    @Override
    public void loginFireBaseImeiOk() {
        if (isShowAlertChangeSesion)
            return;
        InkaFarmaApi inkaFarmaApi = WSConnection.getAPI(context, token);
        OnlineStatusModel onlineStatus = new OnlineStatusModel();
        onlineStatus.setImei(imei);
        isLoadHttp = true;
        inkaFarmaApi.updateUserStatus(onlineStatus).enqueue(new CallbackCustom<Void>() {
            @Override
            public void onResponseHttpOK(Call<Void> call, Void object, int code) {
                if (code == 200)
                    loginview.loginOK();
                else
                    loginview.loadDialogTelephone();
            }

            @Override
            public void onResponseHttpError(int statuCode, Call<Void> call, Response<Void> response) throws IOException {
                Log.e("ERROR: statuCode", String.valueOf(statuCode));
                loginview.dismissDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR: onFailure", t.getMessage());
                loginview.dismissDialog();
            }
        });
    }

    @Override
    public void loginDevice(FirebaseDispatcherDeviceModel firebaseDispatcherDeviceModel) {
        InkaFarmaApi inkaFarmaApi = WSConnection.getAPI(context, token);
        inkaFarmaApi.updateUserDevice(firebaseDispatcherDeviceModel).enqueue(new CallbackCustom<Void>() {
            @Override
            public void onResponseHttpOK(Call<Void> call, Void object, int code) {
                if (code == 200)
                    loginview.loginOK();
            }

            @Override
            public void onResponseHttpError(int statuCode, Call<Void> call, Response<Void> response) throws IOException {
                loginview.dismissDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loginview.dismissDialog();
            }
        });
    }

    @Override
    public void onFailureFireBaseToken(String error) {
        UiComponent.showToastMessage(R.string.error_invalid_credentials, context);
        loginview.dismissDialog();
    }

    @Override
    public void userId(String userId) {
        saveDispatcherUid(userId, context);
    }

    @Override
    public void fireBaseDataSnapshotDispatcher(DataSnapshot dataSnapshot) {
        dispatcher = dataSnapshot.getValue(FireBaseDispatcherModel.class);
        if (dispatcher != null
                && dispatcher.getDevice() != null) {
            if (dispatcher.getDevice().getImei() != null) {
                if (!dispatcher.getDevice().getImei().equals(imei)
                        && !isNegativeButton
                        && !isShowAlertChangeSesion) {
                    loginview.showAlertDialogImei();
                } else {
                    if (!isLoadHttp) {
                        loginFireBaseImeiOk();
                    }
                }
            } else {
                if (!isLoadHttp)
                    loginFireBaseImeiOk();
            }
            dispatcher.setUid(getCurrentUserUid(context));
            if (dispatcher != null
                    && dispatcher.getDevice().getImei() != null) {
                dispatcher.getDevice().setImei(DispatcherGlobal.imei);
            }
            Util.saveDispatcher(dispatcher, context);
            loginview.dismissDialog();
        } else {
            if (!isLoadHttp) {
                loginFireBaseImeiOk();
            }
        }
    }

    @Override
    public void dismissDialog() {
        loginview.dismissDialog();
    }

    public interface Loginview {

        void loginOK();

        void loadDialogTelephone();

        void showDialog();

        void dismissDialog();

        void showAlertDialogImei();

        void showAlertDialogLogout();

    }
}
