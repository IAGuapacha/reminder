package com.iaguapacha.reminder.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.data.model.ReminderWithNotifications
import com.iaguapacha.reminder.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Insert
    suspend fun insertNotification(notification: NotificationEntity)

    @Transaction
    @Query("SELECT * FROM ReminderEntity WHERE id = :reminderId")
    suspend fun getReminderWithNotifications(reminderId: Long): ReminderWithNotifications

    @Query("SELECT * FROM ReminderEntity")
    fun getReminders(): Flow<List<ReminderEntity>>

    @Query("DELETE FROM NotificationEntity WHERE reminderId = :reminderId")
    suspend fun deleteNotificationsByReminderId(reminderId: Long)

    @Query("DELETE FROM ReminderEntity WHERE id = :reminderId")
    suspend fun deleteReminderById(reminderId: Long)
}
