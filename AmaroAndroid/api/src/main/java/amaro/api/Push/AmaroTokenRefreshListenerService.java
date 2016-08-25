package amaro.api.Push;

import android.content.Intent;

import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by juan.villa on 07/06/2016.
 */
public class AmaroTokenRefreshListenerService extends InstanceIDListenerService {
  /**
   * When token refresh, start service to get new token
   */
  @Override
  public void onTokenRefresh() {
    Intent intent = new Intent(this, AmaroRegistrationIntentService.class);
    startService(intent);
  }
}
