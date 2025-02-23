package com.iaguapacha.reminder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iaguapacha.reminder.data.dao.ContactDao
import com.iaguapacha.reminder.data.model.ContactEntity
import com.iaguapacha.reminder.data.model.NotificationEntity

@Database(entities = [ContactEntity::class, NotificationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
