package com.rockspoon.automationtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rockspoon.automationtest.Adapters.UserListAdapter;
import com.rockspoon.automationtest.Adapters.UserListBaseAdapter;
import com.rockspoon.automationtest.Models.User;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_user)
public class ThirdActivity extends AppCompatActivity {

  @Extra("name")
  String name;

  @ViewById(R.id.userNameTitle)
  TextView userNameTitle;

  @ViewById(R.id.userlist)
  ListView user_list;
  ArrayList<User> users = new ArrayList<>();

  @AfterViews
  public void init() {

  }

  @Click(R.id.getUserListButton)
  public void getUsersFromService() {
    HttpPOSTVolley();
  }


  @Background
  public void HttpPOSTVolley() {
    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    String url = "http://jsonplaceholder.typicode.com/users";
    StringRequest postRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        //Sucess User!
        drawList(deserializeResponse(response));
      }
    }, new com.android.volley.Response.ErrorListener() {
      //Error WebService
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    }) {
      @Override
      public String getBodyContentType() {
        return "application/json; charset=utf-8";
      }

      @Override
      protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
        String responseString = "";
        if (response != null) {
          responseString = new String(response.data);
          // can get more details such as response.headers
        }
        return com.android.volley.Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
      }

      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json; charset=utf-8");
        return header;
      }


    };
    postRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    queue.add(postRequest);
  }

  private ArrayList<User> deserializeResponse(String responseString) {

    List<User> listUsers = new ArrayList<>();
    ObjectMapper serializer = new ObjectMapper();
    serializer.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    try {
      listUsers = new ArrayList<User>(Arrays.asList(serializer.readValue(responseString, User[].class)));
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("Error Serializacion", e.getMessage());
    }
    return users = new ArrayList(listUsers);
  }

  @UiThread
  public void drawList(ArrayList<User> users) {
    user_list.setAdapter(new UserListBaseAdapter(users, this));
  }


}
