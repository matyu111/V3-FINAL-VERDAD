package com.tiendasuplementos.app.data.remote.dto

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val status: String? // "active" o "blocked"
)
