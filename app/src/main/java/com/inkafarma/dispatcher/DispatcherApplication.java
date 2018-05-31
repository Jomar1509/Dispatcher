package com.inkafarma.dispatcher;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class DispatcherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initializeTimber();
        initializeCrashlytics();

    }

    private void initializeCrashlytics() {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(!BuildConfig.LOG_CRASHLYTICS).build();
        Fabric.with(this, new Crashlytics.Builder().core(core).build(), new Crashlytics());
    }

    private void initializeTimber() {
        if (BuildConfig.SHOULD_LOG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public interface OrdersSubscription {
        void notifyChanges();
    }


}
