package com.iaguapacha.reminder.ui.birthdaydetail

import com.iaguapacha.reminder.data.model.ReminderWithNotifications

sealed class BirthdayDetailState {
    object Loading : BirthdayDetailState()
    data class Success(val reminderDetail: ReminderWithNotifications) : BirthdayDetailState()
    data class Error(val error: String) : BirthdayDetailState()
}
