package com.suyf.notificationtoast.toast;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.suyf.notificationtoast.PushResultActivity;
import com.suyf.notificationtoast.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotificationToast {

    public static void show(int notificationId, View contentView) {
        if (contentView == null || contentView.getParent() != null) {
            return;
        }
        showByToastToken(notificationId, contentView);
    }


    private static void showByToastToken(final int notificationId, View contentView) {
        try {
            final Toast toast = Toast.makeText(contentView.getContext(), "", Toast.LENGTH_SHORT);
            final Field mTNField = toast.getClass().getDeclaredField("mTN");
            mTNField.setAccessible(true);
            final Object mTnObj = mTNField.get(toast);

            Field paramField = mTnObj.getClass().getDeclaredField("mParams");
            paramField.setAccessible(true);
            WindowManager.LayoutParams params = (WindowManager.LayoutParams) paramField.get(mTnObj);

            params.windowAnimations = R.style.Notification_Toast;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

            contentView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickContentView(v.getContext(), notificationId);
                    hideToast(mTnObj);
                }
            });
            toast.setView(contentView);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clickContentView(Context context, int notificationId) {
        Intent intentClick = new Intent(context, PushResultActivity.class);
        intentClick.setAction(PushResultActivity.ACTION_CLICK);
        context.startActivity(intentClick);
        //remove notification
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private static void hideToast(Object mTnObj) {
        try {
            Method handleHide = mTnObj.getClass()
                    .getDeclaredMethod("handleHide", (Class<?>[]) null);
            handleHide.setAccessible(true);
            handleHide.invoke(mTnObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
