package com.suyf.notificationtoast;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NotificationToast {

  static String TAG = "ActivityRecord";
  private static Toast sToast;
  private static int sNotificationId;
  private static View sContentView;

  public static void show(final int notificationId, View contentView) {
    show(notificationId, contentView, null);
  }

  public static void show(final int notificationId, final View contentView, IBinder windowToken) {
    if (contentView == null || contentView.getParent() != null) {
      return;
    }
    sNotificationId = notificationId;
    sContentView = contentView;
    showByWindowToken(contentView, windowToken, notificationId);
  }

  private static void showByWindowToken(final View contentView, IBinder windowToken,
      final int notificationId) {

    Context context = contentView.getContext().getApplicationContext();
    if (context == null) {
      context = contentView.getContext();
    }
    final WindowManager windowManager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);

    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    params.token = windowToken;
    params.gravity = Gravity.TOP;
    params.type = WindowManager.LayoutParams.TYPE_TOAST;
    params.format = PixelFormat.TRANSLUCENT;
    params.windowAnimations = R.style.Notification_Toast;
    params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
    params.width = contentView.getResources().getDisplayMetrics().widthPixels;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    params.x = 0;
    params.y = 0;
    try {
      Log.d(TAG, "showByWindowToken1: " + contentView.getParent());
      windowManager.addView(contentView, params);
      Log.d(TAG, "showByWindowToken2: " + contentView.getParent());
      sContentView = null;
    } catch (BadTokenException e) {
      e.printStackTrace();
      Log.d(TAG, "showByWindowToken3: " + contentView.getParent());
      windowManager.removeView(contentView);
      showByToastToken(context);
      return;
    }
    contentView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        removeContentView(contentView);
        clickContentView(v.getContext(), notificationId);
      }
    });
    contentView.postDelayed(new Runnable() {
      @Override
      public void run() {
        removeContentView(contentView);
      }
    }, 3000);
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

  private static void removeContentView(View contentView) {
    if (contentView == null || contentView.getParent() == null) {
      return;
    }
    WindowManager manager = (WindowManager) contentView.getContext()
        .getSystemService(Context.WINDOW_SERVICE);
    manager.removeView(contentView);
    sContentView = null;
  }

  private static void showByToastToken(Context context) {
    if (sToast == null) {
      sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }
    try {
      Field mTNField = sToast.getClass().getDeclaredField("mTN");
      mTNField.setAccessible(true);
      Object mTnObj = mTNField.get(sToast);

      Field handlerField = mTnObj.getClass().getDeclaredField("mHandler");
      handlerField.setAccessible(true);
      handlerField.set(mTnObj, handler);

      Field viewField = mTnObj.getClass().getDeclaredField("mView");
      viewField.setAccessible(true);
      viewField.set(mTnObj, new View(context));
      sToast.show();
    } catch (Exception e) {
      e.printStackTrace();
      sContentView = null;
    }
  }

  private static Handler handler = new Handler(Looper.myLooper()) {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch (msg.what) {
        case 0: {//SHOW
          IBinder token = (IBinder) msg.obj;
          show(sNotificationId, sContentView, token);
          sNotificationId = 0;
          sContentView = null;
          break;
        }
        case 1: {//hide
          if (sToast != null) {
            try {
              Field mTNField = sToast.getClass().getDeclaredField("mTN");
              mTNField.setAccessible(true);
              Object mTnObj = mTNField.get(sToast);

              Field viewField = mTnObj.getClass().getDeclaredField("mView");
              viewField.setAccessible(true);

              Method handleHide = mTnObj.getClass()
                  .getDeclaredMethod("handleHide", (Class<?>[]) null);
              handleHide.setAccessible(true);
              handleHide.invoke(mTnObj);
            } catch (Exception e) {
              e.printStackTrace();
              sContentView = null;
            }
          }
          break;
        }
        case 2: {//cancel
          break;
        }
      }
    }
  };

}
