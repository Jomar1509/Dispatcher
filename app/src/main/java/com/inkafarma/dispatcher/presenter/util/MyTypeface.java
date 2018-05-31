package com.inkafarma.dispatcher.presenter.util;

import android.content.Context;
import android.graphics.Typeface;


public class MyTypeface {
    private Context context;
    public Typeface mTf;

    public MyTypeface(Context context) {
        this.context = context;

    }

    public Typeface openRobotoCondensedBold() {
        Typeface mTf = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        return mTf;
    }


    public Typeface openStdRg() {
        Typeface mTf = Typeface.createFromAsset(context.getAssets(), "fonts/foco_std_rg-webfont.ttf");
        return mTf;
    }

    public Typeface openRobotoLt() {
        Typeface mTf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        return mTf;
    }

    public Typeface openRobotoRg() {
        Typeface mTf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        return mTf;
    }

    public Typeface openRobotoBold() {
        Typeface mTf = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        return mTf;
    }

}
