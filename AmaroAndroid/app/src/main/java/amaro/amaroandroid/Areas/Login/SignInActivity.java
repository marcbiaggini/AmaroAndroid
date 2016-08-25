package amaro.amaroandroid.Areas.Login;

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

@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends AppCompatActivity {

  @ViewById(R.id.edtxtNameSign)
  MaterialEditText edtxtNameSign;
  @ViewById(R.id.edtxtLastNameSign)
  MaterialEditText edtxtLastNameSign;
  @ViewById(R.id.edtxtEmailSign)
  MaterialEditText edtxtEmailSign;
  @ViewById(R.id.edtxtCpfSign)
  MaterialEditText edtxtCpfSign;
  @ViewById(R.id.edtxtDataSign)
  MaterialEditText edtxtDataSign;
  @ViewById(R.id.edtxtCelSign)
  MaterialEditText edtxtCelSign;
  @ViewById(R.id.edtxtPasswordSign)
  MaterialEditText edtxtPasswordSign;
  @ViewById(R.id.edtxtRpasswordSign)
  MaterialEditText edtxtRpasswordSign;
  LoadingFragment loadingFragment = new LoadingFragment();
  FragmentManager fm = getSupportFragmentManager();


  @AfterViews
  public void init() {
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  @Click(R.id.btnContinuarSign)
  public void continuarCadastro() {
    loadingFragment.setCancelable(false);
    loadingFragment.show(fm, "fragment_loading_dialog");
    if (validateForm()) {
      if (validatePassword()) {
        signIn();
      } else {
        setErrorMessage(this, getResources().getString(R.string.password_error_message));
      }
    } else {
      setErrorMessage(this, getResources().getString(R.string.form_error_message) + "Para cadastrar o usuario");
    }
  }

  @Background
  public void signIn() {
    MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<String, Object>();
    userMap.add("email", edtxtEmailSign.getText().toString());
    userMap.add("name", edtxtNameSign.getText().toString());
    userMap.add("lastname", edtxtLastNameSign.getText().toString());
    userMap.add("cpf", edtxtCpfSign.getText().toString());
    userMap.add("phone", edtxtCelSign.getText().toString());
    userMap.add("birthdate", edtxtDataSign.getText().toString());
    userMap.add("password", edtxtPasswordSign.getText().toString());
    signIn(AmaroServices.getMyAccount().createUser(userMap));
  }

  @UiThread
  public void signIn(GenericResponse response) {
    if (response.getCode().equals("0")) {
      login(edtxtEmailSign.getText().toString(), edtxtPasswordSign.getText().toString());
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  @Background
  public void login(String user, String password) {
    MultiValueMap<String, Object> loginCpfMap = new LinkedMultiValueMap<String, Object>();
    loginCpfMap.add("email", user);
    loginCpfMap.add("password", password);
    login(AmaroServices.getMyAccount().logIn(loginCpfMap));
  }

  @UiThread
  public void login(GenericResponse response) {
    loadingFragment.dismiss();
    if (response.getCode().equals("0")) {
      AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.SESSION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.AUTENTICATION));
      AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, AmaroServices.getMyAccount().getCookie(amaro.api.BuildConfig.SESSION));
      finish();
    } else {
      setErrorMessage(this, response.getMsg());
    }
  }

  private boolean validateForm() {
    boolean form = true;
    if (edtxtEmailSign.getText().toString().equals("") || edtxtNameSign.getText().toString().equals("") || edtxtLastNameSign.getText().toString().equals("") || edtxtCpfSign.getText().toString().equals("") || edtxtCelSign.getText().toString().equals("") || edtxtCelSign.getText().toString().equals("") || edtxtDataSign.getText().toString().equals("") || edtxtPasswordSign.getText().toString().equals("") || edtxtRpasswordSign.getText().toString().equals("")) {
      form = false;
    }
    return form;
  }

  private boolean validatePassword() {
    return edtxtPasswordSign.getText().toString().equals(edtxtRpasswordSign.getText().toString());
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
