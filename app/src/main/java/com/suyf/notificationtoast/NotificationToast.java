package com.suyf.notificationtoast;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

public class NotificationToast {

  public static void show(final int notificationId, View contentView) {
    show(notificationId, contentView, null);
  }

  public static void show(final int notificationId, final View contentView, IBinder windowToken) {
    if (contentView == null || contentView.getParent() != null) {
      return;
    }
    Context context = contentView.getContext().getApplicationContext();
    if (context == null) {
      context = contentView.getContext();
    }
    final WindowManager windowManager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    if (windowToken != null) {
      mParams.token = windowToken;
    }
    mParams.gravity = Gravity.TOP;
    mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
    mParams.format = PixelFormat.TRANSLUCENT;
    mParams.packageName = context.getPackageName();
    mParams.windowAnimations = R.style.Notification_Toast;
    mParams.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
    mParams.width = contentView.getResources().getDisplayMetrics().widthPixels;
    mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    mParams.x = 0;
    mParams.y = 0;
    windowManager.addView(contentView, mParams);
    contentView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        windowManager.removeView(contentView);
        Context vContext = v.getContext();
        Intent intentClick = new Intent(vContext, PushResultActivity.class);
        intentClick.setAction(PushResultActivity.ACTION_CLICK);
        vContext.startActivity(intentClick);
        //remove notification
        NotificationManager manager = (NotificationManager) vContext
            .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
      }
    });
    contentView.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (windowManager != null && contentView != null && contentView.getParent() != null) {
          windowManager.removeView(contentView);
        }
      }
    }, 3000);
  }

}
