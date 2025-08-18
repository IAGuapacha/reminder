package com.iaguapacha.reminder.ui.birthdayform

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.app.NotificationScheduler
import com.iaguapacha.reminder.data.model.NotificationEntity
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayFormViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BirthdayFormState())
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadBirthday(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val reminderWithNotifications = reminderRepository.getReminderWithNotifications(id)
                reminderWithNotifications?.let { reminderData ->
                    val reminder = reminderData.reminder
                    val notificationTypes = reminderData.notifications.map { notification ->
                        NotificationType.valueOf(notification.type)
                    }.toSet()

                    _state.update {
                        it.copy(
                            birthdayId = reminder.id,
                            isEditMode = true,
                            name = reminder.name,
                            day = reminder.day.toString(),
                            month = reminder.month.toString(),
                            year = reminder.year?.toString() ?: "",
                            notifications = notificationTypes,
                            isLoading = false
                        )
                    }
                } ?: run {
                    _state.update {
                        it.copy(
                            error = "No se encontró el cumpleaños",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Error al cargar los datos: ${e.localizedMessage}",
                        isLoading = false
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleEvent(event: BirthdayFormEvent) {
        when (event) {
            is BirthdayFormEvent.NameChanged -> updateName(event.name)
            is BirthdayFormEvent.NotificationToggled -> toggleNotification(event.type)
            BirthdayFormEvent.ErrorShown -> clearError()
            BirthdayFormEvent.Navigated -> clearNavigation()
            BirthdayFormEvent.Save -> save()
            is BirthdayFormEvent.DateChanged -> updateDate(event.day, event.month, event.year)
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updateDate(day: String, month: String, year: String) {
        _state.update { currentState ->
            currentState.copy(
                day = day,
                month = month,
                year = year
            )
        }
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
    private fun validateInput(): Boolean {
        var isValid = true

        // Resetear errores
        _state.update {
            it.copy(
                nameError = null,
                dateError = null
            )
        }

        // Validar nombre
        if (state.value.name.isBlank()) {
            _state.update { it.copy(nameError = "Nombre requerido") }
            isValid = false
        }

        // validar fecha
        if (state.value.day.isBlank() || state.value.month.isBlank()) {
            _state.update { it.copy(dateError = "Fecha requerida") }
            isValid = false
        }

        return isValid
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun save() {
        if (!validateInput()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            runCatching {
                if (_state.value.isEditMode && _state.value.birthdayId != null) {
                    updateReminder()
                } else {
                    insertReminder()
                }
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
    private suspend fun insertReminder() {
        val reminderId = reminderRepository.insertReminder(
            ReminderEntity(
                name = _state.value.name,
                day = _state.value.day.toInt(),
                month = _state.value.month.toInt(),
                year = _state.value.year.takeIf { it.isNotBlank() }?.toInt()
            )
        )

        insertNotifications(reminderId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateReminder() {
        val birthdayId = _state.value.birthdayId ?: return

        reminderRepository.updateReminder(
            ReminderEntity(
                id = birthdayId,
                name = _state.value.name,
                day = _state.value.day.toInt(),
                month = _state.value.month.toInt(),
                year = _state.value.year.takeIf { it.isNotBlank() }?.toInt()
            )
        )

        deleteNotifications(birthdayId)

        insertNotifications(birthdayId)
    }

    private suspend fun deleteNotifications(reminderId: Long) {

        val notificationScheduler = NotificationScheduler(context)
        val reminderWithNotifications = reminderRepository.getReminderWithNotifications(reminderId)

        reminderWithNotifications?.notifications?.forEach { notification ->
            notificationScheduler.cancelNotification(notification.id)
        }
    }

    private suspend fun insertNotifications(reminderId: Long) {
        _state.value.notifications.forEach { type ->
            val newNotificationId = System.currentTimeMillis().toInt()
            val notificationScheduler = NotificationScheduler(context)

            val trigger = notificationScheduler.scheduleBirthdayNotification(
                newNotificationId.hashCode(),
                _state.value.name,
                _state.value.day.toInt(),
                _state.value.month.toInt(),
                type
            )

            reminderRepository.insertNotification(
                NotificationEntity(
                    id = newNotificationId,
                    reminderId = reminderId,
                    type = type.name,
                    enabled = true,
                    triggerTime = trigger
                )
            )
        }
    }
}
