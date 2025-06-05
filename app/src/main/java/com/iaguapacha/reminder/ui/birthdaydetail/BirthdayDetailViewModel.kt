package com.iaguapacha.reminder.ui.birthdaydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iaguapacha.reminder.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayDetailViewModel @Inject constructor(
    private val repository: ReminderRepository
): ViewModel() {

    private val _state = MutableStateFlow<BirthdayDetailState>(BirthdayDetailState.Loading)
    val state: StateFlow<BirthdayDetailState> = _state.asStateFlow()

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog.asStateFlow()

    private val _navigateBack = MutableStateFlow(false)
    val navigateBack: StateFlow<Boolean> = _navigateBack.asStateFlow()

    private var currentReminderId: Long? = null

    fun loadData(contactId: Long) {
        currentReminderId = contactId
        viewModelScope.launch {
            runCatching {
                val contactDetail = repository.getDetailReminder(contactId)
                _state.value = BirthdayDetailState.Success(contactDetail)
            }.onFailure {
                _state.value = BirthdayDetailState.Error(it.message.toString())
            }
        }
    }

    fun onDeleteClick() {
        _showDeleteDialog.value = true
    }

    fun onDeleteDialogDismiss() {
        _showDeleteDialog.value = false
    }

    fun onDeleteConfirm() {
        val id = currentReminderId ?: return
        viewModelScope.launch {
            repository.deleteReminderWithNotifications(id)
            _showDeleteDialog.value = false
            _navigateBack.value = true
        }
    }

    fun onNavigatedBack() {
        _navigateBack.value = false
    }
}

