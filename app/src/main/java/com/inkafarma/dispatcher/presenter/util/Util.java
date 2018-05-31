package com.inkafarma.dispatcher.presenter.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.inkafarma.dispatcher.data.model.FireBaseDispatcherModel;
import com.inkafarma.dispatcher.data.model.MotorizeModel;
import com.inkafarma.dispatcher.data.model.MotorizedOrderModel;
import com.inkafarma.dispatcher.data.model.OrderModel;
import com.inkafarma.dispatcher.data.model.PosModel;

import java.util.ArrayList;
import java.util.List;

import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_MASTER_ID;
import static com.inkafarma.dispatcher.presenter.util.Constants.CARD_VISA_ID;
import static com.inkafarma.dispatcher.presenter.util.Constants.DISPATCHER_UID;

public final class Util {

    private Util() {
    }

    public static boolean isActivityShow(Context context, String className) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String classNameShow = null;

        try {
            ComponentName componentName = null;
            if (taskInfo != null && taskInfo.get(0) != null) {
                componentName = taskInfo.get(0).topActivity;
            }
            if (componentName != null) {
                classNameShow = componentName.getClassName();
            }

        } catch (NullPointerException e) {
            return false;
        }
        return className.equals(classNameShow);
    }

    public static void saveDispatcher(FireBaseDispatcherModel fireBasePicker, Context context) {
        SharedPreferences.Editor editor = UtilPreferences.getSharedPreferencesEditor(context);
        editor.putString(DISPATCHER_UID, fireBasePicker.getUid());
        editor.apply();
    }

    public static MotorizedOrderModel loadDataMock() {
        MotorizedOrderModel motorizedOrderModel;
        List<OrderModel> orderModelList = new ArrayList<>();
        List<PosModel> posModelList = new ArrayList<>();
        MotorizeModel motorizeModel;

        orderModelList.add(new OrderModel(9346, 1524505109524L, "A7", false, null, false));
        orderModelList.add(new OrderModel(6386, 1524505109524L, "D3", false, null, false));
        orderModelList.add(new OrderModel(8546, 1524505109524L, "D6", false, null, false));
        posModelList.add(new PosModel("Visa",null, null, false));
        posModelList.add(new PosModel("MasterCard",null, null, false));
        motorizeModel = new MotorizeModel("BLT1tEuSyjPlJo7cZ6bL6FJfbTF2", "Joel Martin", "Chuco Marrufo");

        motorizedOrderModel = new MotorizedOrderModel(motorizeModel, orderModelList, posModelList);

        return motorizedOrderModel;
    }

    public static boolean searchList(String textQuery, MotorizedOrderModel dataList) {
        if (dataList != null) {
            if (dataList.getOrders() != null
                    && !dataList.getOrders().isEmpty()) {
                for (OrderModel orderModel : dataList.getOrders()) {
                    if (textQuery.equals(String.valueOf(orderModel.getId()))) {
                        orderModel.setChecked(true);
                        return true;
                    }
                }
            }
            if (dataList.getPosList() != null
                    && !dataList.getPosList().isEmpty()) {
                for (PosModel posModel : dataList.getPosList()) {
                    if (textQuery.toUpperCase().contains(CARD_VISA_ID)
                            && posModel.getProvider().contains(CARD_VISA_ID)) {
                        posModel.setChecked(true);
                        return true;
                    } else if (textQuery.toUpperCase().contains(CARD_MASTER_ID)
                            && posModel.getProvider().contains(CARD_MASTER_ID)) {
                        posModel.setChecked(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}