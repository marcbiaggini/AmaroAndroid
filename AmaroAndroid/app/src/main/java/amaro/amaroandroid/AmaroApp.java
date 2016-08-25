package amaro.amaroandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.urbanairship.UAirship;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import amaro.api.Error.RSRestErrorHandler;
import amaro.api.Helpers.AmaroServices;
import amaro.api.Helpers.Resource;
import amaro.api.Model.MySearch.Filter.FilterValue;
import amaro.api.Model.MySearch.SearchResult;

/**
 * Created by juan.villa on 03/05/2016.
 */
@EApplication
public class AmaroApp extends Application {
  @Bean
  RSRestErrorHandler errorHandler;

  public static Context getAppContext() {
    return AmaroApp_.getInstance();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    AmaroServices.initializeServices(this, errorHandler);

    SharedPreferences settings = getSharedPreferences(amaro.api.BuildConfig.AUTENTICATION, Context.MODE_PRIVATE);
    String autentication = settings.getString(amaro.api.BuildConfig.AUTENTICATION, "");
    String session = settings.getString(amaro.api.BuildConfig.SESSION, "");

    AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.AUTENTICATION, autentication);
    AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.AUTENTICATION, autentication);
    AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.AUTENTICATION, autentication);
    AmaroServices.getMyAccount().setCookie(amaro.api.BuildConfig.SESSION, session);
    AmaroServices.getMyCart().setCookie(amaro.api.BuildConfig.SESSION, session);
    AmaroServices.getMyCheckOut().setCookie(amaro.api.BuildConfig.SESSION, session);

    setFilters();
    urbanAirshipSetup();

  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  @Background
  public void urbanAirshipSetup() {
    UAirship.takeOff(this, new UAirship.OnReadyCallback() {
      @Override
      public void onAirshipReady(UAirship airship) {

        // Enable user notifications
        airship.getPushManager().setUserNotificationsEnabled(true);
      }
    });
  }

  @Background
  public void setFilters() {
    setFilters(AmaroServices.getMySearch().search());
  }

  @UiThread
  public void setFilters(SearchResult result) {
    AmaroServices.createFilters(result);
  }
}
