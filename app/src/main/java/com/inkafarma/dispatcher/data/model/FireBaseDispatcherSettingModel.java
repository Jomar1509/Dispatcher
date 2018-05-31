package com.inkafarma.dispatcher.data.model;

public class FireBaseDispatcherSettingModel {
    private boolean scanCommand;
    private boolean scanTray;

    public boolean isScanCommand() {
        return scanCommand;
    }

    public void setScanCommand(boolean scanCommand) {
        this.scanCommand = scanCommand;
    }

    public boolean isScanTray() {
        return scanTray;
    }

    public void setScanTray(boolean scanTray) {
        this.scanTray = scanTray;
    }
}
