package com.iaguapacha.reminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iaguapacha.reminder.data.dao.ReminderDao
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.data.model.NotificationEntity

@Database(entities = [ReminderEntity::class, NotificationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
