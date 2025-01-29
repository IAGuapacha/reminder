package com.iaguapacha.reminder.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ContactEntity::class,
            parentColumns = ["contactId"],
            childColumns = ["contactOwnerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LabelEntity::class,
            parentColumns = ["labelId"],
            childColumns = ["labelId"],
            onDelete = ForeignKey.SET_NULL // Permite que un recordatorio tenga un label opcional.
        )
    ],
    indices = [Index(value = ["contactOwnerId"]), Index(value = ["labelId"])]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) val reminderId: Long = 0,
    val contactOwnerId: Long, // Relación al contacto
    val labelId: Long?,       // Relación opcional a la etiqueta
    val description: String,
    val dateTime: LocalDateTime         // Tiempo en formato epoch
)


@Entity
data class LabelEntity(
    @PrimaryKey(autoGenerate = true) val labelId: Long = 0,
    val name: String
)