package com.inkafarma.dispatcher.presenter.presenter;


import android.app.Activity;

/**
 * DashboardPresenter.
 */
public interface DashboardPresenter {

    void init();

    void logout(Activity activity);

    void logoutImei();

    void stopListener();

}
