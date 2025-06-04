package com.iaguapacha.reminder.ui.birthdaydetail

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
class BirthdayDetailViewModel @Inject constructor(
    private val repository: ContactRepository
): ViewModel() {

    private val _state = MutableStateFlow<BirthdayDetailState>(BirthdayDetailState.Loading)
    val state: StateFlow<BirthdayDetailState> = _state.asStateFlow()

    fun loadData(contactId: Long){
        viewModelScope.launch {
            runCatching {
                val contactDetail = repository.getDetailContact(contactId)
                _state.value = BirthdayDetailState.Success(contactDetail)
            }.onFailure {
                _state.value = BirthdayDetailState.Error(it.message.toString())
            }
        }
    }
}