package com.tiendasuplementos.app.ui.auth

import com.tiendasuplementos.app.data.remote.dto.AuthResponse

sealed class AuthState {
    object Loading : AuthState()
    data class Success(val response: AuthResponse) : AuthState()
    data class Error(val message: String) : AuthState()
    object Idle : AuthState()
}
