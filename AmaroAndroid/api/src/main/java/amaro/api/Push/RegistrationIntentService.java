package amaro.api.Push;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.firebase.iid.FirebaseInstanceId;

import amaro.api.R;

/**
 * Created by juan.villa on 08/06/2016.
 */
public class RegistrationIntentService extends IntentService {
  // abbreviated tag name
  private static final String TAG = "RegIntentService";
  public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
  public static final String FCM_TOKEN = "FCMToken";

  public RegistrationIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    // Fetch token here
    try {
      // [START register_for_gcm]
      // Initially this call goes out to the network to retrieve the token, subsequent calls
      // are local.
      // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
      // See https://developers.google.com/cloud-messaging/android/start for details on this file.
      // [START get_token]
      InstanceID instanceID = InstanceID.getInstance(this);
      String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
          GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
      // [END get_token]
      Log.i(TAG, "GCM Registration Token: " + token);

      // TODO: Implement this method to send any registration to your app's servers.
      sendRegistrationToServer(token);

      // You should store a boolean that indicates whether the generated token has been
      // sent to your server. If the boolean is false, send the token to your server,
      // otherwise your server should have already received the token.
      sharedPreferences.edit().putBoolean("sentTokenToServer", true).apply();
      sharedPreferences.edit().putString("token",token).apply();
      // [END register_for_gcm]
    } catch (Exception e) {
      Log.d(TAG, "Failed to complete token refresh", e);
      // If an exception happens while fetching the new token or updating our registration data
      // on a third-party server, this ensures that we'll attempt the update at a later time.
      sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
    }
    // Notify UI that registration has completed, so the progress indicator can be hidden.
    Intent registrationComplete = new Intent("registrationComplete");
    LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
  }

  private void sendRegistrationToServer(String token) {
    // send network request

    // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
    sharedPreferences.edit().putString("token",token).apply();
  }



}
