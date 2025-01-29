package com.iaguapacha.reminder.ui.contactlist

import com.iaguapacha.reminder.data.model.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)