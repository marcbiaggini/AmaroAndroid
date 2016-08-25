package amaro.amaroandroid.Areas.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import amaro.amaroandroid.Fragments.Message.LoadingFragment;
import amaro.amaroandroid.R;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Model.GenericResponse;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

  @ViewById(R.id.edtxtNameLog)
  MaterialEditText edtxtUser;
  @ViewById(R.id.edtxtPasswordLog)
  MaterialEditText edtxtPassword;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm = getSupportFragmentManager();


  @AfterViews
  public void init() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlogin);
    setSupportActionBar(toolbar);
  }

  @Click(R.id.btnContinuarLogin)
  public void continuar() {
    if (validateForm()) {
      loadingFragment.setCancelable(false);
      loadingFragment.show(fm, "fragment_loading_dialog");
      login(edtxtUser.getText().toString(), edtxtPassword.getText().toString());
    } else {
      setErrorMessage(this, getResources().getString(R.string.form_error_message) + " Par realizr o Login");
    }
  }

  @Click(R.id.btnCadastroLogin)
  public void signin() {
    SignInActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
    finish();
  }

  public boolean validateForm() {
    boolean form = true;
    if (edtxtUser.getText().toString().equals("") || edtxtPassword.getText().toString().equals("")) {
      form = false;
    }
    return form;
  }

  @Background
  public void login(String user, String password) {
    MultiValueMap<String, Object> loginCpfMap = new LinkedMultiValueMap<String, Object>();
    loginCpfMap.add("email", user);
    loginCpfMap.add("password", password);
    login(AmaroServices.getMyAccount().logIn(loginCpfMap));
  }

  @UiThread
  public void login(GenericResponse logIn) {
    loadingFragment.dismiss();
    if (logIn.getCode().equals("0")) {
      AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.SESSION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.SESSION));
      SharedPreferences settings = getSharedPreferences(amaro.api.BuildConfig.AUTENTICATION, Context.MODE_PRIVATE);
      SharedPreferences.Editor editor = settings.edit();
      editor.putString(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      editor.putString(amaro.api.BuildConfig.SESSION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.SESSION));
      editor.commit();
      finish();
    } else {
      SignInActivity_.intent(getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).start();
      finish();
    }
  }

  public void setErrorMessage(AppCompatActivity activity, String message) {

    new LovelyStandardDialog(activity)
        .setTopColorRes(R.color.colorAccent)
        .setButtonsColorRes(R.color.colorLightDark)
        .setIcon(new IconicsDrawable(activity.getBaseContext())
            .icon(Ionicons.Icon.ion_android_alert)
            .color(Color.parseColor("#FFFFFF"))
            .sizeDp(32).getCurrent())
        .setTitle(R.string.delete_item_title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          }
        })
        .show();
  }
}
