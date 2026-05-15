package com.example.nimmaguru.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimmaguru.data.model.Guru
import com.example.nimmaguru.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GuruProfileViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun saveProfile(guru: Guru, imageUri: Uri?) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                var finalGuru = guru
                if (imageUri != null) {
                    val uploadResult = repository.uploadProfileImage(imageUri, guru.name) // Using name as ID for simplicity in this demo
                    if (uploadResult.isSuccess) {
                        finalGuru = guru.copy(imageUrl = uploadResult.getOrThrow())
                    }
                }
                
                val saveResult = repository.saveGuruProfile(finalGuru)
                if (saveResult.isSuccess) {
                    _uiState.value = ProfileUiState.Success
                } else {
                    _uiState.value = ProfileUiState.Error(saveResult.exceptionOrNull()?.message ?: "Failed to save profile")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    sealed class ProfileUiState {
        object Idle : ProfileUiState()
        object Loading : ProfileUiState()
        object Success : ProfileUiState()
        data class Error(val message: String) : ProfileUiState()
    }
}