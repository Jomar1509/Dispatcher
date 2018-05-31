package com.inkafarma.dispatcher.presenter.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inkafarma.dispatcher.R;

import static com.inkafarma.dispatcher.presenter.util.DispatcherGlobal.isShowAlertChangeSesion;

public final class DialogUtils {


    private static final int NO_ICON_RES_ID = 0;

    private DialogUtils() {
    }

    public static void showDialog(@NonNull Activity activity, @StringRes int titleRes, String messageRes,
                                  @StringRes int positiveButtonTextRes, Context context, @Nullable int iconRes) {
        showDialog(activity, titleRes, messageRes, positiveButtonTextRes, null, context, iconRes);
    }

    public static void showDialogImei(@NonNull Activity activity, @StringRes int titleRes, @StringRes int messageRes,
                                      @StringRes int positiveButtonTextRes,
                                      final DialogInterface.OnClickListener positiveButtonListener,
                                      final DialogInterface.OnClickListener negativeButtonListener, Context context,
                                      @Nullable int iconRes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(titleRes)
                .setCancelable(false)
                .setPositiveButton(positiveButtonTextRes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (positiveButtonListener != null) {
                            positiveButtonListener.onClick(dialog, id);
                        }
                        dialog.cancel();
                    }
                });

        //FIXME Change this logic, overload the method
        if (positiveButtonListener != null) {
            builder.setNegativeButton(R.string.mobile_session_not, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    negativeButtonListener.onClick(dialogInterface, i);
                    dialogInterface.cancel();
                }
            });
        }
        builder.setMessage(messageRes);

        final AlertDialog dialog = builder.create();
        dialog.show();

        isShowAlertChangeSesion = true;

        if (iconRes != NO_ICON_RES_ID) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialog.cancel();
                }
            });
        } else {

            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            LinearLayout parent = (LinearLayout) positiveButton.getParent();
            parent.setGravity(Gravity.CENTER_HORIZONTAL);
            View leftSpacer = parent.getChildAt(1);
            leftSpacer.setVisibility(View.GONE);

            LinearLayout negativeParent = (LinearLayout) negativeButton.getParent();
            negativeParent.setGravity(Gravity.CENTER_HORIZONTAL);
            View negativeLeftSpacer = negativeParent.getChildAt(1);
            negativeLeftSpacer.setVisibility(View.GONE);
            positiveButton.setTextColor(context.getResources().getColor(R.color.selected_thumb_color));
            negativeButton.setTextColor(context.getResources().getColor(R.color.negative_button_grey));
        }

    }

    public static void showDialog(@NonNull final Activity activity, @StringRes int titleRes, String messageRes,
                                  @StringRes int positiveButtonTextRes,
                                  final DialogInterface.OnClickListener positiveButtonListener, Context context,
                                  @Nullable int iconRes) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle(titleRes)
                .setCancelable(false)
                .setPositiveButton(positiveButtonTextRes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (positiveButtonListener != null) {
                            positiveButtonListener.onClick(dialog, id);
                        }
                        dialog.cancel();
                    }
                });

        //FIXME Change this logic, overload the method
        if (positiveButtonListener != null) {
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        builder.setMessage(Html.fromHtml(messageRes));

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00da5f"));
            }
        });

        dialog.show();

        if (iconRes != NO_ICON_RES_ID) {
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dialog.cancel();
                }
            });
        } else {

            TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
            messageView.setGravity(Gravity.CENTER);

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

            LinearLayout parent = (LinearLayout) positiveButton.getParent();
            parent.setGravity(Gravity.CENTER_HORIZONTAL);
            View leftSpacer = parent.getChildAt(1);
            leftSpacer.setVisibility(View.GONE);

            LinearLayout negativeParent = (LinearLayout) negativeButton.getParent();
            negativeParent.setGravity(Gravity.CENTER_HORIZONTAL);
            View negativeLeftSpacer = negativeParent.getChildAt(1);
            negativeLeftSpacer.setVisibility(View.GONE);

            positiveButton.setTextColor(context.getResources().getColor(R.color.selected_thumb_color));

            negativeButton.setTextColor(context.getResources().getColor(R.color.negative_button_grey));
        }

    }

}
