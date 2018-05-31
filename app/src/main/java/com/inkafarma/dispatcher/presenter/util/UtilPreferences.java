package com.inkafarma.dispatcher.presenter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.inkafarma.dispatcher.presenter.util.Constants.USER_FIREBASE_UID;
import static com.inkafarma.dispatcher.presenter.util.Constants.USER_LOGIN_UID;

public final class UtilPreferences {

    public static Boolean getMotorizedLogin(Context context) {
        boolean isLogin = false;

        if (getSharedPreferences(context) != null)
            isLogin = getSharedPreferences(context).getBoolean(USER_LOGIN_UID, false);

        return isLogin;
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit();
    }

    public static String getCurrentUserUid(Context context) {
        return getSharedPreferences(context).getString(USER_FIREBASE_UID, "");
    }

    public static void saveDispatcherUid(String uid, Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(USER_FIREBASE_UID, uid);
        editor.apply();
    }

    public static void saveDispatcherLogin(boolean isLogin, Context context) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);

        if (!isLogin) {
            editor.clear();
            editor.commit();
            editor.apply();
        }

        editor.putBoolean(USER_LOGIN_UID, isLogin);
        editor.apply();
    }
}
