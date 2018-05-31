package com.inkafarma.dispatcher.presenter.presenterImpl;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.inkafarma.dispatcher.data.fireBase.FireBaseInteractor;
import com.inkafarma.dispatcher.data.fireBase.FireBaseListener;
import com.inkafarma.dispatcher.data.model.FireBaseDispatcherModel;
import com.inkafarma.dispatcher.data.repository.InkaFarmaApi;
import com.inkafarma.dispatcher.data.repository.ws.CallbackCustom;
import com.inkafarma.dispatcher.data.repository.ws.WSConnection;
import com.inkafarma.dispatcher.presenter.presenter.DashboardPresenter;
import com.inkafarma.dispatcher.presenter.util.UtilPreferences;

import java.io.IOException;
import retrofit2.Call;
import retrofit2.Response;

import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.imei;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.dispatcher;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.dispatcherId;
import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isLoadHttp;
import static com.inkafarma.dispatcher.presenter.util.Util.saveDispatcher;
import static com.inkafarma.dispatcher.presenter.util.UtilPreferences.getCurrentUserUid;
import static com.inkafarma.dispatcher.presenter.util.UtilPreferences.saveDispatcherLogin;

public class DashboardPresenterImpl implements DashboardPresenter, FireBaseListener.FirebaseDashBoardView {
    private DashboardView dashboardView;
    private FireBaseInteractor fireBaseInteractor;
    private Context context;
    private String[] mensaje = new String[2];
    private AppCompatActivity activity;
    private Activity dashboardActivity;
    private String token;

    public DashboardPresenterImpl(Context context, DashboardView dashboardView, int resultLogin) {
        this.dashboardView = dashboardView;
        this.context = context;
        activity = (AppCompatActivity) context;
        fireBaseInteractor = new FireBaseListener(this);
        fireBaseInteractor.getDispatcherFromServer(getCurrentUserUid(context));
    }

    @Override
    public void init() {
        String alias = "";
        String fullName = "";
        String url = "";
        String phone = "";

        if (dispatcher != null) {
            if (dispatcher.getUser() != null) {
                if (dispatcher.getUser().getAlias() != null)
                    alias = dispatcher.getUser().getAlias();

                if (dispatcher.getUser().getLastName() != null)
                    fullName = dispatcher.getUser().getLastName();

                if (dispatcher.getUser().getFirstName() != null)
                    fullName = (!fullName.isEmpty() ? fullName + " " + dispatcher.getUser().getFirstName()
                            : dispatcher.getUser().getFirstName());

                if (dispatcher.getUser().getPhoto() != null)
                    url = dispatcher.getUser().getPhoto();

                if (dispatcher.getUser().getPhone() != null)
                    phone = dispatcher.getUser().getPhone();

            }
            dashboardView.dispatcherAlias(alias, fullName, url, phone);
        }
    }

    @Override
    public void logout(Activity activity) {
        this.dashboardActivity = activity;
        fireBaseInteractor.obtainTokenUser();
    }

    @Override
    public void logoutImei() {
        saveDispatcherLogin(false, context);
        if (dispatcher != null
                && dispatcher.getUid() != null)
            dispatcherId = dispatcher.getUid();
    }

    @Override
    public void stopListener() {
        fireBaseInteractor.stopListeners();
    }

    @Override
    public void fireBaseDataSnapshotDispatcher(DataSnapshot dataSnapshot) {
        if (dataSnapshot == null)
            return;

        dispatcher = new FireBaseDispatcherModel();
        dispatcher = dataSnapshot.getValue(FireBaseDispatcherModel.class);
        String alias = "";
        String fullName = "";
        String url = "";
        String phone = "";
        if (dispatcher != null) {
            dispatcher.setUid(UtilPreferences.getCurrentUserUid(context));
            saveDispatcher(dispatcher, context);
            if (dispatcher.getDevice() != null
                    && dispatcher.getDevice().getImei() != null) {
                if (!dispatcher.getDevice().getImei().equals(imei)) {
                    dashboardView.logoutImei();
                    return;
                }
            }

            if (dispatcher.getUser() != null) {
                if (dispatcher.getUser().getAlias() != null)
                    alias = dispatcher.getUser().getAlias();

                if (dispatcher.getUser().getLastName() != null)
                    fullName = dispatcher.getUser().getLastName();

                if (dispatcher.getUser().getFirstName() != null)
                    fullName = (!fullName.isEmpty() ? fullName + " " + dispatcher.getUser().getFirstName()
                            : dispatcher.getUser().getFirstName());

                if (dispatcher.getUser().getPhoto() != null)
                    url = dispatcher.getUser().getPhoto();

                if (dispatcher.getUser().getPhone() != null)
                    phone = dispatcher.getUser().getPhone();

            }
            dashboardView.dispatcherAlias(alias, fullName, url, phone);
        } else {
            logoutImei();
            fireBaseInteractor.setlogoutPicker();
            dashboardView.logoutUser(true);
        }
    }

    @Override
    public void logoutEndPoint(String token) {
        InkaFarmaApi inkaFarmaApi = WSConnection.getAPI(context, token);
        isLoadHttp = true;
        inkaFarmaApi.updateUserLogout().enqueue(new CallbackCustom<Void>() {
            @Override
            public void onResponseHttpOK(Call<Void> call, Void object, int code) {
                logoutImei();
                fireBaseInteractor.setlogoutPicker();
                dashboardView.logoutUser(false);
            }

            @Override
            public void onResponseHttpError(int statuCode, Call<Void> call, Response<Void> response) throws IOException {
                dashboardView.dismissDialogLogoutError();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dashboardView.dismissDialogLogoutError();
            }
        });
    }

    @Override
    public void showMessage(boolean exists) {
        dashboardView.showMessage(exists);
    }

    public interface DashboardView {

        void dispatcherAlias(String alias, String fullName, String url, String phone);

        void showAlertDialogImei();

        void showDialog();

        void dismissDialog();

        void dismissDialogLogout();

        void dismissDialogLogoutError();

        void logoutImei();

        void logoutUser(boolean isError);

        void showMessage(boolean exists);

    }

    public interface ViewLogin {
        void dismissDialog();
    }
}
