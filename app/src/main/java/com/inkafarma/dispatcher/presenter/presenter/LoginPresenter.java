package com.inkafarma.dispatcher.presenter.presenter;

import com.inkafarma.dispatcher.data.model.FirebaseDispatcherDeviceModel;

/**
 * LoginPresenter interface para iniciar el login.
 */
public interface LoginPresenter {

    void initLogin(String email, String pass);

    void loginFireBaseImeiOk();

    void loginDevice(FirebaseDispatcherDeviceModel firebaseDispatcherDeviceModel);
}
