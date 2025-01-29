package com.iaguapacha.reminder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.iaguapacha.reminder.data.model.LabelEntity
import com.iaguapacha.reminder.data.model.ReminderEntity

@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("SELECT * FROM ReminderEntity WHERE reminderId = :reminderId")
    suspend fun getReminderById(reminderId: Long): ReminderEntity?

    @Query("SELECT * FROM ReminderEntity WHERE contactOwnerId = :contactId")
    suspend fun getRemindersByContact(contactId: Long): List<ReminderEntity>

    @Transaction
    @Query("SELECT * FROM ReminderEntity WHERE contactOwnerId = :contactId")
    suspend fun getRemindersWithLabelByContact(contactId: Long): List<ReminderWithLabel>
}

data class ReminderWithLabel(
    @Embedded val reminder: ReminderEntity,
    @Relation(
        parentColumn = "labelId",
        entityColumn = "labelId"
    )
    val label: LabelEntity?
)