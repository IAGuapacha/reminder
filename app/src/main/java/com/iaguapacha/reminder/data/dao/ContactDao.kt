package com.iaguapacha.reminder.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.iaguapacha.reminder.data.model.ContactEntity
import com.iaguapacha.reminder.data.model.ContactWithNotifications
import com.iaguapacha.reminder.data.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    suspend fun insertContact(contact: ContactEntity): Long

    @Insert
    suspend fun insertNotification(notification: NotificationEntity)

    @Transaction
    @Query("SELECT * FROM ContactEntity WHERE id = :contactId")
    suspend fun getContactWithNotifications(contactId: Long): ContactWithNotifications

    @Query("SELECT * FROM ContactEntity")
    fun getContacts(): Flow<List<ContactEntity>>
}
