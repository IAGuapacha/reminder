package com.iaguapacha.reminder.ui.contactlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState: StateFlow<ContactsUiState> = _uiState

    fun getAllContacts(){
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val contacts = repository.getAllContacts()
                _uiState.value = _uiState.value.copy(
                    contacts = contacts,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar los contactos"
                )
            }
        }

    }

}