package com.rockspoon.automationtest.Service;


import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rockspoon.automationtest.Models.User;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by juancamilovilladuarte on 3/14/16.
 */
public class RepeatService extends IntentService {
  public RepeatService() {
    super(RepeatService.class.getName());
  }

  public static final String INTENT_REPEAT = "com.rockspoon.automationtest.Service.REPEAT";
  public static final String KEY_INPUT = "input";
  public static final String KEY_OUTPUT = "output";


  @Override
  protected void onHandleIntent(Intent intent) {
    SystemClock.sleep(1000);  // Pretend it runs for a long time.

    String query = intent.getStringExtra(KEY_INPUT);

    Intent replyIntent = new Intent(INTENT_REPEAT);
    replyIntent.putExtra(KEY_OUTPUT, query + "\n" + query);

    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
    manager.sendBroadcast(replyIntent);
  }

}
