package amaro.api.Push;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import amaro.api.R;

/**
 * Created by juan.villa on 07/06/2016.
 */
public class AmaroRegistrationIntentService extends IntentService {
  public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
  public static final String REGISTRATION_ERROR = "RegistrationError";

  public AmaroRegistrationIntentService() {
    super("");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    registerGCM();
  }

  public void registerGCM() {
    Intent registrationComplete = null;
    String token = null;
    try {
      InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
      token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      Log.e("RegistrationService","token:"+token);
      //notify UI that registration complete success
      registrationComplete= new Intent(REGISTRATION_SUCCESS);
      registrationComplete.putExtra("token",token);
    }catch (Exception e){
      Log.e("RegistrationService","Registration Error");
      registrationComplete= new Intent(REGISTRATION_ERROR);
    }
    //Send Broadcast
    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
  }
}
