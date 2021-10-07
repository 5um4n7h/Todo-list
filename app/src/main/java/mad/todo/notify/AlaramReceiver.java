package mad.todo.notify;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.atomic.AtomicReference;

import mad.todo.R;
import mad.todo.SplashScreenActivity;

public class AlaramReceiver extends BroadcastReceiver {
 Context context;

 @SuppressLint("LaunchActivityFromNotification")
 @Override
 public void onReceive(Context context, Intent intent) {
  this.context = context;

  createNotificationChannel();
  String title = intent.getExtras().get("title").toString();
  String msg = intent.getExtras().get("due").toString();
  String priority = intent.getExtras().get("priority").toString();
//		Toast.makeText(context, "Received !", Toast.LENGTH_SHORT).show();
  NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "Todo")
    .setSmallIcon(R.drawable.ic_launcher)
    .setContentTitle("Alert")
    .setSubText("You have a " + priority + " priority task due !")
    .setStyle(new NotificationCompat.BigTextStyle()
      .bigText(title + "\n" + msg))
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setAutoCancel(true);

  NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

  // Create an Intent for the activity you want to start
  Intent resultIntent = new Intent(this.context, SplashScreenActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
  TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
  stackBuilder.addNextIntentWithParentStack(resultIntent);


  int reqcode = (int) intent.getExtras().get("id");
  Log.d("TAG", "onReceive: " + reqcode);
  Intent intent1 = new Intent("MY_ALARM_NOTIFICATION");
  intent1.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

  @SuppressLint("LaunchActivityFromNotification") PendingIntent resultPendingIntent = PendingIntent.getBroadcast(context, reqcode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
  builder.setContentIntent(resultPendingIntent);
  notificationManager.notify(reqcode, builder.build());


// notificationId is a unique int for each notification that you must define
  notificationManager.notify(reqcode, builder.build());
 }

 private void createNotificationChannel() {
  // Create the NotificationChannel, but only on API 26+ because
  // the NotificationChannel class is new and not in the support library
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
   CharSequence name = "Todo";
   String description = "Todo";
   int importance = NotificationManager.IMPORTANCE_HIGH;
   NotificationChannel channel = new NotificationChannel("Todo", name, importance);
   channel.setDescription(description);
   // Register the channel with the system; you can't change the importance
   // or other notification behaviors after this
   NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
   notificationManager.createNotificationChannel(channel);
  }
 }
}