package com.inkafarma.dispatcher.presenter.util;

import com.google.firebase.database.ValueEventListener;
import com.inkafarma.dispatcher.data.model.FireBaseDispatcherModel;

public final class DispatcherGlobal {

    public static String imei;
    public static boolean isDashboard = false;
    public static boolean isLoadHttp = false;
    public static boolean isShowAlertChangeSesion = false;
    public static boolean isNegativeButton = false;
    public static boolean logoutDashboard = false;
    public static String dispatcherId;
    public static ValueEventListener eventListenerDispatcher;
    public static FireBaseDispatcherModel dispatcher;
}
