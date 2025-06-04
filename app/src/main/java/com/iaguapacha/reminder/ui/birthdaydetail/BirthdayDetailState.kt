package com.iaguapacha.reminder.ui.birthdaydetail

import com.iaguapacha.reminder.data.model.ContactWithNotifications

sealed class BirthdayDetailState {
    object Loading : BirthdayDetailState()
    data class Success(val contactDetail: ContactWithNotifications) : BirthdayDetailState()
    data class Error(val error: String) : BirthdayDetailState()
}
