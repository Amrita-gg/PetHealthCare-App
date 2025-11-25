package com.example.pethealthcare.vaccines;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class VaccineNotificationManager {

    public void scheduleNotification(Context context, String vaccineName, long timeInMillis) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("vaccineName", vaccineName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                vaccineName.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE  // <-- Fixed flag
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        }
    }
}
