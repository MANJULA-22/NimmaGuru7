package com.example.nimmaguru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimmaguru.data.model.Guru
import com.example.nimmaguru.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {

    private val _allGurus = MutableStateFlow<List<Guru>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedSubject = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)

    val searchQuery = _searchQuery.asStateFlow()
    val selectedSubject = _selectedSubject.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    // Main list of gurus with filtering
    val gurus: StateFlow<List<Guru>> = combine(
        _allGurus,
        _searchQuery,
        _selectedSubject
    ) { all, query, subject ->
        all.filter { guru ->
            val matchesQuery = query.isBlank() || 
                    guru.name.contains(query, ignoreCase = true) ||
                    guru.village.contains(query, ignoreCase = true)
            
            val matchesSubject = subject == null || 
                    guru.skills.any { it.equals(subject, ignoreCase = true) }
            
            matchesQuery && matchesSubject
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wall of Fame specific list (Rating >= 4.0)
    val wallOfFameGurus: StateFlow<List<Guru>> = gurus
        .map { list -> list.filter { guru -> guru.rating >= 4.0 }.sortedByDescending { it.rating } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchGurus()
    }

    fun fetchGurus() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getAllGurus()
            if (result.isSuccess) {
                _allGurus.value = result.getOrDefault(emptyList())
            }
            _isLoading.value = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onSubjectSelected(subject: String?) {
        _selectedSubject.value = if (subject == "All" || subject == "All Subjects" || subject == "ಎಲ್ಲಾ" || subject == "ಎಲ್ಲಾ ವಿಷಯಗಳು") null else subject
    }
    
    fun toggleSkill(skill: String) {
        if (_selectedSubject.value == skill) {
            _selectedSubject.value = null
        } else {
            _selectedSubject.value = skill
        }
    }
}