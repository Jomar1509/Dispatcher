package com.inkafarma.dispatcher.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MotorizedOrderModel {

    @SerializedName("motorized")
    private MotorizeModel motorized;
    @SerializedName("orders")
    private List<OrderModel> orders;
    @SerializedName("posList")
    private List<PosModel> posList;

    public MotorizedOrderModel() {
    }

    public MotorizeModel getMotorized() {
        return motorized;
    }

    public void setMotorized(MotorizeModel motorized) {
        this.motorized = motorized;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    public List<PosModel> getPosList() {
        return posList;
    }

    public void setPosList(List<PosModel> posList) {
        this.posList = posList;
    }

    public MotorizedOrderModel(MotorizeModel motorized, List<OrderModel> orders, List<PosModel> posList) {
        this.motorized = motorized;
        this.orders = orders;
        this.posList = posList;
    }
}
