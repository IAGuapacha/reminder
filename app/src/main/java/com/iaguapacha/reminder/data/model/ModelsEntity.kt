package com.iaguapacha.reminder.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val day: Int,
    val month: Int,
    val year: Int? = null // null si no se conoce el año
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ReminderEntity::class,
            parentColumns = ["id"],
            childColumns = ["reminderId"],
            onDelete = ForeignKey.CASCADE // Al eliminar el reminder se borran sus notificaciones
        )
    ],
    indices = [Index(value = ["reminderId"])]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val reminderId: Long,
    val type: String, // Ej: "Fecha", "2 días antes", "1 semana antes"
    val enabled: Boolean = true
)

data class ReminderWithNotifications(
    @Embedded val reminder: ReminderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "reminderId"
    )
    val notifications: List<NotificationEntity>
)

