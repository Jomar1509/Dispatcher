package com.inkafarma.dispatcher.presenter.util;

import android.util.Log;

import com.crashlytics.android.Crashlytics;


public class DispatcherException {
    private DispatcherException() {
    }

    public static void logException(Throwable ex, String className, String errorMessage) {
        Crashlytics.log(Log.ERROR, className, errorMessage);
        Crashlytics.logException(ex);
    }
}
