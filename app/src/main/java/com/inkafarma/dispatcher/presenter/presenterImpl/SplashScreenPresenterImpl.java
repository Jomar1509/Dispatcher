package com.inkafarma.dispatcher.presenter.presenterImpl;

import android.content.Context;
import android.os.Handler;
import com.inkafarma.dispatcher.presenter.presenter.SplashScreenPresenter;
import com.inkafarma.dispatcher.presenter.util.UtilPreferences;

import static com.inkafarma.dispatcher.presenter.util.Constants.SPLASH_DISPLAY_DURATION;


public class SplashScreenPresenterImpl implements SplashScreenPresenter {
    private View view;
    private Context context;


    public SplashScreenPresenterImpl(Context context, View view) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void init() {
        startSplashScreenHandler();
    }

    public void startSplashScreenHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UtilPreferences.getMotorizedLogin(context)) {
                    view.startDashboard();
                } else {
                    view.startActivity();
                }
            }
        }, SPLASH_DISPLAY_DURATION);
    }


    public interface View {
        void startActivity();

        void startDashboard();
    }
}
