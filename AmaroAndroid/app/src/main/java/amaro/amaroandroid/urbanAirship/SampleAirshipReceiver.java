package amaro.amaroandroid.urbanAirship;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.urbanairship.AirshipReceiver;
import com.urbanairship.push.PushMessage;

/**
 * Created by juan.villa on 14/06/2016.
 */
public class SampleAirshipReceiver extends AirshipReceiver {

  /**
   * Intent action sent as a local broadcast to update the channel.
   */
  public static final String ACTION_UPDATE_CHANNEL = "ACTION_UPDATE_CHANNEL";
  private static final String TAG = "SampleAirshipReceiver";

  @Override
  protected void onChannelRegistrationSucceeded(Context context, String channelId) {
    Log.i(TAG, "Channel registration updated. Channel Id:" + channelId + ".");

    // Broadcast that the channel updated. Used to refresh the channel ID on the home fragment
    LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_UPDATE_CHANNEL));
  }

  @Override
  protected void onChannelRegistrationFailed(Context context) {
    Log.i(TAG, "Channel registration failed.");
  }

  @Override
  protected void onPushReceived(@NonNull Context context, @NonNull PushMessage message, boolean notificationPosted) {
    Log.i(TAG, "Received push message. Alert: " + message.getAlert() + ". posted notification: " + notificationPosted);
  }

  @Override
  protected void onNotificationPosted(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification posted. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: " + notificationInfo.getNotificationId());
  }

  @Override
  protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification opened. Alert: " + notificationInfo.getMessage().getAlert() + ". NotificationId: " + notificationInfo.getNotificationId());

    // Return false here to allow Urban Airship to auto launch the launcher activity
    return false;
  }

  @Override
  protected boolean onNotificationOpened(@NonNull Context context, @NonNull NotificationInfo notificationInfo, @NonNull ActionButtonInfo actionButtonInfo) {
    Log.i(TAG, "Notification action button opened. Button ID: " + actionButtonInfo.getButtonId() + ". NotificationId: " + notificationInfo.getNotificationId());

    // Return false here to allow Urban Airship to auto launch the launcher
    // activity for foreground notification action buttons
    return false;
  }

  @Override
  protected void onNotificationDismissed(@NonNull Context context, @NonNull NotificationInfo notificationInfo) {
    Log.i(TAG, "Notification dismissed. Alert: " + notificationInfo.getMessage().getAlert() + ". Notification ID: " + notificationInfo.getNotificationId());
  }
}
