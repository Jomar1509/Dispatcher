package com.inkafarma.dispatcher.presenter.presenterImpl;

import android.content.Context;

import com.google.gson.Gson;
import com.inkafarma.dispatcher.data.fireBase.FireBaseInteractor;
import com.inkafarma.dispatcher.data.fireBase.FireBaseListener;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.data.repository.InkaFarmaApi;
import com.inkafarma.dispatcher.data.repository.ws.CallbackCustom;
import com.inkafarma.dispatcher.data.repository.ws.WSConnection;
import com.inkafarma.dispatcher.presenter.presenter.ScanPresenter;
import com.inkafarma.dispatcher.presenter.util.Util;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.inkafarma.dispatcher.presenter.util.DispatcherException.logException;

public class ScanPresenterImpl implements ScanPresenter, FireBaseListener.FirebaseScanView {

    private FireBaseInteractor fireBaseInteractor;
    private ScanView scanView;
    private Context context;

    public ScanPresenterImpl(Context context, ScanView scanView) {
        this.context = context;
        this.scanView = scanView;
        fireBaseInteractor = new FireBaseListener(this);
    }

    @Override
    public void getDispatcher(String token, String dni) {
        InkaFarmaApi inkaFarmaApi = WSConnection.getAPI(context, token);
        System.out.println("****** token --> " + token);
        System.out.println("****** dni --> " + dni);
        final MotorizedOrderModel[] motorizedOrderModel = {new MotorizedOrderModel()};
        try {
            inkaFarmaApi.getDispatcher(dni).enqueue(new CallbackCustom<MotorizedOrderModel>() {
                @Override
                public void onResponseHttpOK(Call<MotorizedOrderModel> call, MotorizedOrderModel object, int code) {
                    System.out.println("****** object --> " + new Gson().toJson(object));
                    motorizedOrderModel[0] = (MotorizedOrderModel) object;
                    scanView.startActivity(motorizedOrderModel[0]);
                    scanView.dismissDialog();
                }

                @Override
                public void onResponseHttpError(int statuCode, Call<MotorizedOrderModel> call,
                                                Response<MotorizedOrderModel> response) throws IOException {
                    System.out.println("****** statuCode --> " + statuCode);
                    System.out.println("****** response --> " + new Gson().toJson(response));
                    scanView.showMessage();
                    scanView.dismissDialog();
                }

                @Override
                public void onFailure(Call<MotorizedOrderModel> call, Throwable t) {
                    scanView.dismissDialog();
                }
            });
        } catch (Exception ex) {
            logException(ex, getClass().getSimpleName(), "Exception caught");
        }

    }

    @Override
    public void showMessage() {
        scanView.showMessage();
    }

    @Override
    public void dismissDialog() {
        scanView.dismissDialog();
    }

    @Override
    public void startActivity(MotorizedOrderModel motorizedOrderModel) {
        scanView.startActivity(motorizedOrderModel);
    }

    @Override
    public void getDispatcher(String dni) {
        fireBaseInteractor.obtainMotorizedOrder(dni);
    }

    public interface ScanView {

        void startActivity(MotorizedOrderModel motorizedOrderModel);

        void showMessage();

        void dismissDialog();

    }
}
