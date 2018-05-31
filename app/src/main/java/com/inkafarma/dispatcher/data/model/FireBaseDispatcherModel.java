package com.inkafarma.dispatcher.data.model;

public class FireBaseDispatcherModel {
    private String uid;
    private FirebaseDispatcherUserModel user;
    private FirebaseDispatcherStatusModel status;
    private FirebaseDispatcherDeviceModel device;
    private FirebaseDispatcherDrugstoreModel drugstore;
    private FireBaseDispatcherSettingModel setting;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public FirebaseDispatcherUserModel getUser() {
        return user;
    }

    public void setUser(FirebaseDispatcherUserModel user) {
        this.user = user;
    }

    public FirebaseDispatcherStatusModel getStatus() {
        return status;
    }

    public void setStatus(FirebaseDispatcherStatusModel status) {
        this.status = status;
    }

    public FirebaseDispatcherDeviceModel getDevice() {
        return device;
    }

    public void setDevice(FirebaseDispatcherDeviceModel device) {
        this.device = device;
    }

    public FirebaseDispatcherDrugstoreModel getDrugstore() {
        return drugstore;
    }

    public void setDrugstore(FirebaseDispatcherDrugstoreModel drugstore) {
        this.drugstore = drugstore;
    }

    public FireBaseDispatcherSettingModel getSetting() {
        return setting;
    }

    public void setSetting(FireBaseDispatcherSettingModel setting) {
        this.setting = setting;
    }
}
