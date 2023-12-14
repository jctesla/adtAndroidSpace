import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

  private static final int NOTIFICATION_ID = 1;
  private static final String CHANNEL_ID = "ForegroundServiceChannel";

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    createNotificationChannel();

    Intent notificationIntent = new Intent(this, YourActivity.class); // Replace with your desired activity
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Foreground Service")
        .setContentText("Service is running")
        .setSmallIcon(R.drawable.ic_notification_icon)
        .setContentIntent(pendingIntent)
        .build();

    startForeground(NOTIFICATION_ID, notification);

    // Perform your desired action here, such as starting the activity

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    stopForeground(true);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          CHANNEL_ID,
          "Foreground Service Channel",
          NotificationManager.IMPORTANCE_DEFAULT);
      NotificationManager manager = getSystemService(NotificationManager.class);
      manager.createNotificationChannel(channel);
    }
  }
}
