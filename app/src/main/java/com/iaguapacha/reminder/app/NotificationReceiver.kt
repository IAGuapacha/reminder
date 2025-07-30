package com.iaguapacha.reminder.app

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.iaguapacha.reminder.R
import com.iaguapacha.reminder.ui.birthdayform.NotificationType

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val reminderName = intent.getStringExtra(REMINDER_NAME)
            ?: context.getString(R.string.default_reminder_name)
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
        val typeNotificationStr = intent.getStringExtra(NOTIFICATION_TYPE)

        val notificationType = try {
            if (typeNotificationStr != null) NotificationType.valueOf(typeNotificationStr) else NotificationType.ON_DATE
        } catch (e: IllegalArgumentException) {
            NotificationType.ON_DATE
        }

        val (title, text) = when (notificationType) {
            NotificationType.ON_DATE -> Pair(
                context.getString(R.string.notification_on_date_title, reminderName),
                context.getString(R.string.notification_on_date_text, reminderName)
            )

            NotificationType.TWO_DAYS_BEFORE -> Pair(
                context.getString(R.string.notification_two_days_before_title, reminderName),
                context.getString(R.string.notification_two_days_before_text, reminderName)
            )

            NotificationType.ONE_WEEK_BEFORE -> Pair(
                context.getString(R.string.notification_one_week_before_title, reminderName),
                context.getString(R.string.notification_one_week_before_text, reminderName)
            )
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(notificationId, notification)
            } else {
                Log.e("NotificationReceiver", "Permiso de notificación no concedido")
            }
        } else {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }
}
