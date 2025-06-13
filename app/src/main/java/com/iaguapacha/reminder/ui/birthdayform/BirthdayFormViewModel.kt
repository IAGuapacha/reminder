package com.iaguapacha.reminder.ui.birthdayform

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.data.model.NotificationEntity
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BirthdayFormViewModel @Inject constructor(
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
            is BirthdayFormEvent.DayChanged -> updateDay(event.day)
            is BirthdayFormEvent.MonthChanged -> updateMonth(event.month)
            is BirthdayFormEvent.YearChanged -> updateYear(event.year)
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun updateDay(day: String) {
        if (day.all { it.isDigit() } || day.isEmpty()) {
            _state.update { it.copy(day = day.take(2)) }
        }
    }

    private fun updateMonth(month: String) {
        if (month.all { it.isDigit() } || month.isEmpty()) {
            _state.update { it.copy(month = month.take(2)) }
        }
    }

    private fun updateYear(year: String) {
        if (year.all { it.isDigit() } || year.isEmpty()) {
            _state.update { it.copy(year = year.take(4)) }
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
        val currentYear = LocalDate.now().year

        // Resetear errores
        _state.update {
            it.copy(
                nameError = null,
                dayError = null,
                monthError = null,
                yearError = null
            )
        }

        // Validar nombre
        if (state.value.name.isBlank()) {
            _state.update { it.copy(nameError = "Nombre requerido") }
            isValid = false
        }

        // Validar día (1-31)
        state.value.day.toIntOrNull()?.let { day ->
            if (day !in 1..31) {
                _state.update { it.copy(dayError = "Día inválido (1-31)") }
                isValid = false
            }
        } ?: run {
            _state.update { it.copy(dayError = "Día requerido") }
            isValid = false
        }

        // Validar mes (1-12)
        state.value.month.toIntOrNull()?.let { month ->
            if (month !in 1..12) {
                _state.update { it.copy(monthError = "Mes inválido (1-12)") }
                isValid = false
            }
        } ?: run {
            _state.update { it.copy(monthError = "Mes requerido") }
            isValid = false
        }

        // Validar año opcional (1900-Actual+1)
        state.value.year.takeIf { it.isNotBlank() }?.let { yearStr ->
            yearStr.toIntOrNull()?.let { year ->
                if (year !in 1900..currentYear + 1) {
                    _state.update {
                        it.copy(
                            yearError = "Año debe estar entre 1900 y ${currentYear + 1}"
                        )
                    }
                    isValid = false
                }
            } ?: run {
                _state.update { it.copy(yearError = "Año inválido") }
                isValid = false
            }
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

        // Actualizar el recordatorio
        reminderRepository.updateReminder(
            ReminderEntity(
                id = birthdayId,
                name = _state.value.name,
                day = _state.value.day.toInt(),
                month = _state.value.month.toInt(),
                year = _state.value.year.takeIf { it.isNotBlank() }?.toInt()
            )
        )

        // Eliminar las notificaciones existentes
        reminderRepository.deleteNotificationsForReminder(birthdayId)

        // Insertar las nuevas notificaciones
        insertNotifications(birthdayId)
    }

    private suspend fun insertNotifications(reminderId: Long) {
        _state.value.notifications.forEach { type ->
            reminderRepository.insertNotification(
                NotificationEntity(
                    reminderId = reminderId,
                    type = type.name,
                    enabled = true
                )
            )
        }
    }
}
