package com.rockspoon.automationtest;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

  @AfterViews
  public void init() {

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Click(R.id.changeText)
  public void changeText() {
    TextView textView = (TextView) findViewById(R.id.textViewHelloWorld);
    EditText input = (EditText) findViewById(R.id.inputField);
    textView.setText(input.getText().toString());
  }

  @Click(R.id.switchActivity)
  public void openNextActivity() {
    SecondaryActivity_.intent(MainActivity.this).extra("input", "I'm on the Screen Already!").start();
  }

}
