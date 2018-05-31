package com.inkafarma.dispatcher.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderPosModel {

    @SerializedName("orders")
    private List<OrderModel> orders;
    @SerializedName("posList")
    private List<PosModel> posList;

    public OrderPosModel() {
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

    public OrderPosModel(List<OrderModel> orders, List<PosModel> posList) {
        this.orders = orders;
        this.posList = posList;
    }
}
