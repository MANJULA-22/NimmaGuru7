package com.example.nimmaguru.data.model

data class Review(
    val id: String = "",
    val guruId: String = "",
    val studentName: String = "",
    val note: String = "",
    val rating: Int = 5,
    val timestamp: Long = System.currentTimeMillis()
)