package com.iaguapacha.reminder.repository

import com.iaguapacha.reminder.data.dao.ReminderDao
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.data.model.ReminderWithNotifications
import com.iaguapacha.reminder.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {

    suspend fun insertReminder(reminder: ReminderEntity): Long {
        return reminderDao.insertReminder(reminder)
    }

    suspend fun insertNotification(notification: NotificationEntity) {
        reminderDao.insertNotification(notification)
    }

    fun getReminders(): Flow<List<ReminderEntity>> {
        return reminderDao.getReminders()
    }

    suspend fun getDetailReminder(reminderId: Long): ReminderWithNotifications {
        return reminderDao.getReminderWithNotifications(reminderId)
    }

    suspend fun deleteReminderWithNotifications(reminderId: Long) {
        reminderDao.deleteNotificationsByReminderId(reminderId)
        reminderDao.deleteReminderById(reminderId)
    }
}
