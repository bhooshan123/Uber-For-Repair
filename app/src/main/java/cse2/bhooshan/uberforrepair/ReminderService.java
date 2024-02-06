package cse2.bhooshan.uberforrepair;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class ReminderService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Start your AsyncTask here
        new ReminderTask().execute();
        return START_NOT_STICKY;
    }

    private class ReminderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            createNotificationChannel();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Now that background work is done, create and show the notification
            Intent notificationIntent = new Intent(ReminderService.this, ScheduleRepairActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(ReminderService.this,
                    0, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ReminderService.this, "REMINDER_CHANNEL")
                    .setSmallIcon(R.drawable.logo) // Make sure this resource exists
                    .setContentTitle("Repair Reminder")
                    .setContentText("You have an upcoming repair scheduled.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            startForeground(1, builder.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "REMINDER_CHANNEL",
                    "Reminder Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
