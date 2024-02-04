package cse2.bhooshan.uberforrepair;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Intent to start the service
        Intent serviceIntent = new Intent(context, ReminderService.class);
        context.startService(serviceIntent);
    }
}
