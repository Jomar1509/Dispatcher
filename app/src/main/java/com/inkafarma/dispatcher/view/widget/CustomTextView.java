package com.inkafarma.dispatcher.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.inkafarma.dispatcher.R;
import com.inkafarma.dispatcher.presenter.util.MyTypeface;
import com.inkafarma.dispatcher.presenter.util.Util;

import static com.inkafarma.dispatcher.presenter.util.DispatcherException.logException;


public class CustomTextView extends AppCompatTextView {

    private Context mContext;
    private MyTypeface myTypeface;
    private int mAdditionalPadding;

    public CustomTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        try {
            this.mContext = context;
            setIncludeFontPadding(false);
            myTypeface = new MyTypeface(mContext);
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTextView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //Do something
                //letterSpacing
                this.setLetterSpacing(-0.03f);
            }
            switch (typedArray.getInteger(R.styleable.CustomTextView_typeFont_, 0)) {
                case 0:
                    this.setTypeface(myTypeface.openStdRg());
                    break;
                case 1:
                    this.setTypeface(myTypeface.openRobotoBold());
                    break;
                case 2:
                    this.setTypeface(myTypeface.openRobotoRg());
                    break;
                case 4:
                    this.setTypeface(myTypeface.openRobotoBold());
                    break;
                default:
                    this.setTypeface(myTypeface.openRobotoRg());
                    break;

            }
            typedArray.recycle();
        } catch (Exception ex) {
            logException(ex, Util.class.getSimpleName(), "Exception caught");
        }

    }


}
