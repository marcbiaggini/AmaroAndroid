package amaro.api.Push;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by juan.villa on 08/06/2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

  @Override
  public void onTokenRefresh() {
    // Fetch updated Instance ID token and notify of changes
    Intent intent = new Intent(this, RegistrationIntentService.class);
    startService(intent);
  }
}
