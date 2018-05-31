package com.inkafarma.dispatcher.presenter.util.ui;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.inkafarma.dispatcher.presenter.util.Constants.MIN_PASSWORD_LENGTH;

public final class UiComponent {

    private UiComponent() {
    }

    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showToastMessage(@StringRes int message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


}
