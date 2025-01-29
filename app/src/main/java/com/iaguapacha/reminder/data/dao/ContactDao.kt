package com.iaguapacha.reminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iaguapacha.reminder.data.model.ContactEntity

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("SELECT * FROM ContactEntity WHERE id = :contactId")
    suspend fun getContactById(contactId: Int): ContactEntity?

    @Query("SELECT * FROM ContactEntity")
    suspend fun getAllContacts(): List<ContactEntity>
}