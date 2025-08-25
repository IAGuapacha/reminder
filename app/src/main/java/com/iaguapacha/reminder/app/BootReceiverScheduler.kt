package com.iaguapacha.reminder.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.iaguapacha.reminder.repository.ReminderRepository
import com.iaguapacha.reminder.ui.birthdayform.NotificationType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiverScheduler : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                rescheduleAllNotifications()
            }
        }
    }

    private suspend fun rescheduleAllNotifications() {
        val reminders = reminderRepository.getReminders().first()

        reminders.forEach { reminder ->
            val reminderWithNotifications = reminderRepository.getReminderWithNotifications(reminder.id)

            reminderWithNotifications?.notifications?.forEach { notification ->
                notificationScheduler.scheduleBirthdayNotification(
                    notificationId = notification.id.toInt(),
                    reminderName = reminder.name,
                    day = reminder.day,
                    month = reminder.month,
                    type = NotificationType.valueOf(notification.type)
                )
            }
        }
    }
}
