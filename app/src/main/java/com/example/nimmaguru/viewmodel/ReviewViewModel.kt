package com.example.nimmaguru.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimmaguru.data.model.Review
import com.example.nimmaguru.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _isPosting = MutableStateFlow(false)
    val isPosting: StateFlow<Boolean> = _isPosting

    fun postReview(guruId: String, studentName: String, note: String, rating: Int, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isPosting.value = true
            val review = Review(
                guruId = guruId,
                studentName = studentName,
                note = note,
                rating = rating
            )
            repository.postReview(review)
            _isPosting.value = false
            onComplete()
        }
    }
}