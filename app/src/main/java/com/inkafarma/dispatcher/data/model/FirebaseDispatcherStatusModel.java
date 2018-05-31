package com.inkafarma.dispatcher.data.model;

public class FirebaseDispatcherStatusModel {
    private String statusName;
    private String message;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
