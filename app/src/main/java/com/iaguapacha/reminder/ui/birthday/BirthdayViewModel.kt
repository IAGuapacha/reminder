package com.iaguapacha.reminder.ui.birthday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.data.model.ContactEntity
import com.iaguapacha.reminder.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor (
    private val contactRepository: ContactRepository
): ViewModel() {
    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> = _contacts.asStateFlow()


    fun getContacts() {
        viewModelScope.launch {
            contactRepository.getContacts().collect { contacts ->
                _contacts.value = contacts
            }
        }
    }

}