package com.iaguapacha.reminder.ui.addbirthday

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.data.model.NotificationEntity
import com.iaguapacha.reminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddBirthdayViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddBirthdayState())
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleEvent(event: AddBirthdayEvent) {
        when (event) {
            is AddBirthdayEvent.NameChanged -> updateName(event.name)
            is AddBirthdayEvent.NotificationToggled -> toggleNotification(event.type)
            AddBirthdayEvent.ErrorShown -> clearError()
            AddBirthdayEvent.Navigated -> clearNavigation()
            AddBirthdayEvent.Save -> save()
            is AddBirthdayEvent.DayChanged -> updateDay(event.day)
            is AddBirthdayEvent.MonthChanged -> updateMonth(event.month)
            is AddBirthdayEvent.YearChanged -> updateYear(event.year)
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
            _state.update { it.copy(month = month.take(2))
            }
        }
    }

    private fun updateYear(year: String) {
        if (year.all { it.isDigit() } || year.isEmpty()) {
            _state.update { it.copy(year = year.take(4))
            }
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
                val reminderId = insertReminder()
                insertNotifications(reminderId)
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
    private suspend fun insertReminder(): Long {
        return reminderRepository.insertReminder(
            ReminderEntity(
                name = _state.value.name,
                day = _state.value.day.toInt(),
                month = _state.value.month.toInt(),
                year = state.value.year.takeIf { it.isNotBlank() }?.toInt()
            )
        )
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
