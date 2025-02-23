package com.iaguapacha.reminder.ui.addbirthday

import java.time.LocalDate

sealed interface AddBirthdayEvent {

    data class NameChanged(val name: String): AddBirthdayEvent
    data class DateChanged(val date: LocalDate): AddBirthdayEvent
    data class NotificationToggled(val type: NotificationType): AddBirthdayEvent
    object Save : AddBirthdayEvent
    object ErrorShown : AddBirthdayEvent
    object Navigated : AddBirthdayEvent
}
