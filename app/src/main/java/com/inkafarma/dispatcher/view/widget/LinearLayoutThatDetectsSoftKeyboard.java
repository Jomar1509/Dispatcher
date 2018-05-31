package com.inkafarma.dispatcher.view.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * <p>
 * LinearLayoutThatDetectsSoftKeyboard contiene algunas modificaciones de  FrameLayout.
 * Contiene una interface {Link LinearLayoutThatDetectsSoftKeyboard.Listener } para manejar
 * cuando el teclado esta oculto o visible.
 * </p>
 */
public class LinearLayoutThatDetectsSoftKeyboard extends FrameLayout {
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public LinearLayoutThatDetectsSoftKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity) getContext();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int diff = (screenHeight - statusBarHeight) - height;
        if (listener != null) {
            listener.onSoftKeyboardShown(diff > 128); // assume all soft keyboards are at least 128 pixels high
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public interface Listener {
        void onSoftKeyboardShown(boolean isShowing);
    }
}
