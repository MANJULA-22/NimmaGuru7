package com.example.nimmaguru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimmaguru.data.model.MentorshipClass
import com.example.nimmaguru.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {

    private val _classes = MutableStateFlow<List<MentorshipClass>>(emptyList())
    val classes: StateFlow<List<MentorshipClass>> = _classes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchClasses()
    }

    fun fetchClasses() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getUpcomingClasses()
            if (result.isSuccess) {
                _classes.value = result.getOrDefault(emptyList())
            }
            _isLoading.value = false
        }
    }

    fun scheduleClass(mentorshipClass: MentorshipClass, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.scheduleClass(mentorshipClass)
            if (result.isSuccess) {
                fetchClasses()
                onComplete()
            }
            _isLoading.value = false
        }
    }
}