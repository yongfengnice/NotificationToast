package com.suyf.notificationtoast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PushResultActivity extends AppCompatActivity {

  public static final String ACTION_CLICK = "action_click";
  public static final String ACTION_CANCEL = "action_cancel";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_push_result);

    TextView textView = findViewById(R.id.text);
    textView.setText("push result action:" + getIntent().getAction());
  }
}
