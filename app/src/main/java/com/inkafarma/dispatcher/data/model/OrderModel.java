package com.inkafarma.dispatcher.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("id")
    private long id;
    @SerializedName("externalId")
    private long externalId;
    @SerializedName("shelf")
    private String shelf;
    @SerializedName("liquidated")
    private boolean liquidated;
    @SerializedName("status")
    private String status;
    private boolean checked;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public boolean isLiquidated() {
        return liquidated;
    }

    public void setLiquidated(boolean liquidated) {
        this.liquidated = liquidated;
    }

    public OrderModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public OrderModel(long id, long externalId, String shelf, boolean liquidated, String status, boolean checked) {
        this.id = id;
        this.externalId = externalId;
        this.shelf = shelf;
        this.liquidated = liquidated;
        this.status = status;
        this.checked = checked;
    }
}
