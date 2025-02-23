package com.iaguapacha.reminder.ui.addbirthday

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.data.model.ContactEntity
import com.iaguapacha.reminder.data.model.NotificationEntity
import com.iaguapacha.reminder.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddBirthdayViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddBirthdayState())
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleEvent(event: AddBirthdayEvent) {
        when (event) {
            is AddBirthdayEvent.NameChanged -> updateName(event.name)
            is AddBirthdayEvent.DateChanged -> updateDate(event.date)
            is AddBirthdayEvent.NotificationToggled -> toggleNotification(event.type)
            AddBirthdayEvent.ErrorShown -> clearError()
            AddBirthdayEvent.Navigated -> clearNavigation()
            AddBirthdayEvent.Save -> save()
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updateDate(date: LocalDate) {
        _state.update { it.copy(date = date) }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun clearNavigation() {
        _state.update { it.copy(navigationEvent = null) }
    }

    private fun toggleNotification(type: NotificationType) {
        _state.update { currentState ->
            val newNotifications = currentState.notifications.toMutableSet()
            if (newNotifications.contains(type)) {
                newNotifications.remove(type)
            } else {
                newNotifications.add(type)
            }
            currentState.copy(notifications = newNotifications)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun save() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {
                val contactId = insertContact()
                insertNotifications(contactId)
                _state.update {
                    it.copy(
                        isLoading = false,
                        navigationEvent = NavigationEvent.Back
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al guardar: ${e.localizedMessage}"
                    )
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun insertContact(): Long {
        return contactRepository.insertContact(
            ContactEntity(
                name = _state.value.name,
                day = _state.value.date.dayOfMonth,
                month = _state.value.date.monthValue,
                year = _state.value.date.year
            )
        )
    }

    private suspend fun insertNotifications(contactId: Long) {
        _state.value.notifications.forEach { type ->
            contactRepository.insertNotification(
                NotificationEntity(
                    contactId = contactId,
                    type = type.name,
                    enabled = true
                )
            )
        }
    }


    private fun validateInput(): Boolean {
        return when {
            _state.value.name.isBlank() -> {
                _state.update { it.copy(error = "El nombre no puede estar vacío") }
                false
            }
            else -> true
        }
    }

}
