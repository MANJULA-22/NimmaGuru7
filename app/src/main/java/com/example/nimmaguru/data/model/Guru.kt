package com.example.nimmaguru.data.model

data class Guru(
    val id: String = "",
    val name: String = "",
    val skills: List<String> = emptyList(),
    val availability: String = "",
    val village: String = "",
    val imageUrl: String = "",
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val description: String = ""
)