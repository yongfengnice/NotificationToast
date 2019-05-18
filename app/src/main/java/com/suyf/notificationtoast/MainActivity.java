package com.suyf.notificationtoast;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        new Handler(getMainLooper()).postDelayed(new Runnable() {
          @Override
          public void run() {
            showNotification();
          }
        }, 1000);
      }
    }).start();
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
