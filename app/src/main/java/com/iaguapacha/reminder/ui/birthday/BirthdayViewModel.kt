package com.iaguapacha.reminder.ui.birthday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.data.model.ReminderEntity
import com.iaguapacha.reminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor (
    private val reminderRepository: ReminderRepository
): ViewModel() {
    private val _reminders = MutableStateFlow<List<ReminderEntity>>(emptyList())
    val reminders: StateFlow<List<ReminderEntity>> = _reminders.asStateFlow()

    fun getReminders() {
        viewModelScope.launch {
            reminderRepository.getReminders().collect { reminders ->
                _reminders.value = reminders
            }
        }
    }
}

