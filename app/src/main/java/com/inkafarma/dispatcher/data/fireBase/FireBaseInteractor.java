package com.inkafarma.dispatcher.data.fireBase;


/**
 * <p>
 * FireBaseInteractor contiene  metodos que seran implementados en  @{Link
 * com.inkafarma.inkapicker.data.fireBase.FireBaseListener}.
 * </p>
 */

public interface FireBaseInteractor {

    void loginFireBase(String user, String pass);

    void stopListeners();

    void getDispatcherFromServer(String dispatcherId);

    void setlogoutPicker();

    void obtainTokenUser();

    void obtainMotorizedOrder(String dni);

}
