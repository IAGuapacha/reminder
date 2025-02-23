package com.iaguapacha.reminder.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class ContactEntity(
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
            entity = ContactEntity::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE // Al eliminar el contacto se borran sus notificaciones
        )
    ],
    indices = [Index(value = ["contactId"])]
)
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contactId: Long,
    val type: String, // Ej: "Fecha", "2 días antes", "1 semana antes"
    val enabled: Boolean = true
)

data class ContactWithNotifications(
    @Embedded val contact: ContactEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "contactId"
    )
    val notifications: List<NotificationEntity>
)