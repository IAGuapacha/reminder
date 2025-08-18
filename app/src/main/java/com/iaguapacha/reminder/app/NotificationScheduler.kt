package com.iaguapacha.reminder.app

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.iaguapacha.reminder.ui.birthdayform.NotificationType
import java.util.*

class NotificationScheduler(private val context: Context) {

    fun scheduleBirthdayNotification(
        notificationId: Int,
        reminderName: String,
        day: Int,
        month: Int,
        type: NotificationType
    ): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 9)  // Notificar a las 9 AM
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // Ajustar la fecha según el tipo de notificación
            when (type) {
                NotificationType.TWO_DAYS_BEFORE -> add(Calendar.DAY_OF_MONTH, -2)
                NotificationType.ONE_WEEK_BEFORE -> add(Calendar.DAY_OF_MONTH, -7)
                NotificationType.ON_DATE -> { /* No ajuste */ }
            }

            // Si la fecha ya pasó este año, programar para el próximo año
            val now = Calendar.getInstance()
            if (timeInMillis <= now.timeInMillis) {
                add(Calendar.YEAR, 1)
            }
        }

        // Incluir el tipo de notificación en el intent para mostrar mensaje adecuado
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(REMINDER_NAME, reminderName)
            putExtra(NOTIFICATION_ID, notificationId)
            putExtra(NOTIFICATION_TYPE, type.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        PendingIntent.FLAG_MUTABLE else 0
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        return calendar.timeInMillis
    }

    fun cancelNotification(notificationId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                        PendingIntent.FLAG_MUTABLE else 0
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
