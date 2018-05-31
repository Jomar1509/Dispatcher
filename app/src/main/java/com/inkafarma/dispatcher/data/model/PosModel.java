package com.inkafarma.dispatcher.data.model;

import com.google.gson.annotations.SerializedName;

public class PosModel {

    @SerializedName("provider")
    private String provider;
    @SerializedName("posCode")
    private String posCode;
    @SerializedName("posStatus")
    private String posStatus;
    private boolean checked;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getPosStatus() {
        return posStatus;
    }

    public void setPosStatus(String posStatus) {
        this.posStatus = posStatus;
    }

    public PosModel() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public PosModel(String provider, String posCode, String posStatus, boolean checked) {
        this.provider = provider;
        this.posCode = posCode;
        this.posStatus = posStatus;
        this.checked = checked;
    }
}
