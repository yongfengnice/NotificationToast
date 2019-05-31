package com.suyf.notificationtoast;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.suyf.notificationtoast.notification.NotificationFactory;
import com.suyf.notificationtoast.toast.NotificationToast;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void show_notification(View view) {
    Toast.makeText(this, "get push message background...", Toast.LENGTH_SHORT).show();
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        new Handler(getMainLooper()).postDelayed(new Runnable() {
          @Override
          public void run() {
            showNotification();
          }
        }, 100);
      }
    }).start();
  }

  public void show_notification2(final View view) {
    final int notificationId = 0x1001;
    final NotificationManager manager = (NotificationManager) getSystemService(
        NOTIFICATION_SERVICE);
    NotificationFactory factory = new NotificationFactory(getApplicationContext(), manager);
    Notification notification = factory.createNotification("id1", "I am notification title1",
        "I am notification contentxxxxxxxxxxx1");
    factory.clear();
    manager.notify(notificationId, notification);

    final View notificationView = getLayoutInflater()
        .inflate(R.layout.view_notification_toast, null);
    findTopView().addView(notificationView);
    notificationView
        .startAnimation(AnimationUtils.loadAnimation(this, R.anim.notification_toast_enter));

    notificationView.postDelayed(new Runnable() {
      @Override
      public void run() {
        manager.cancel(notificationId);
        ViewParent parent = notificationView.getParent();
        if (parent instanceof ViewGroup) {
          ((ViewGroup) parent).removeView(notificationView);
        }
      }
    }, 3000);
  }

  public void show_notification3(View view) {

  }

  public void show_second_page(View view) {
    startActivity(new Intent(this, SecondActivity.class));
  }

  private ViewGroup findTopView() {
    View view = findViewById(Window.ID_ANDROID_CONTENT);
    FrameLayout frameLayout = null;
    while (view != null) {
      ViewParent parent = view.getParent();
      if (parent instanceof View) {
        view = (View) parent;
      } else {
        view = null;
      }
      if (parent instanceof FrameLayout) {
        frameLayout = (FrameLayout) parent;
      }
    }
    return frameLayout;
  }

  private void showNotification() {
    int notificationId = 0x1001;
    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    NotificationFactory factory = new NotificationFactory(getApplicationContext(), manager);
    Notification notification = factory.createNotification("id1", "I am notification title1",
        "I am notification contentxxxxxxxxxxx1");
    factory.clear();
    manager.notify(notificationId, notification);
    NotificationToast
        .show(notificationId, getLayoutInflater().inflate(R.layout.view_notification_toast, null));
  }

}
