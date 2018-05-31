package com.inkafarma.dispatcher.data.repository;

import com.inkafarma.dispatcher.data.model.FirebaseDispatcherDeviceModel;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.data.model.OnlineStatusModel;
import com.inkafarma.dispatcher.data.model.OrderPosModel;
import com.inkafarma.dispatcher.data.model.PosModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * <p>
 * InkaFarmaApi interface donde se declaran todos metodos usados por
 * los webservices.
 * </p>
 */
public interface InkaFarmaApi {
    @PUT("user/login")
    Call<Void> updateUserStatus(@Body OnlineStatusModel onlineStatus);

    @POST("devices")
    Call<Void> updateUserDevice(@Body FirebaseDispatcherDeviceModel firebaseDispatcherDeviceModel);

    @PUT("user/logout")
    Call<Void> updateUserLogout();

    @GET("motorized/dispatch/{dni}")
    Call<MotorizedOrderModel> getDispatcher(@Path("dni") String dni);

    @PUT("motorized/dispatch/{dni}")
    Call<Void> updateDispatcher(@Path("dni") String dni, @Body List<PosModel> posList);

    @PUT("motorized/liquidate")
    Call<Void> updateLiquidate(@Body OrderPosModel orderPosModel);

}
