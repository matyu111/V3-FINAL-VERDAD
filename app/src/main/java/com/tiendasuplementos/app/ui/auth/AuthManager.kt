package com.tiendasuplementos.app.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.remote.dto.UpdateProfileRequest
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.ui.state.UiState
import com.tiendasuplementos.app.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Estado para el perfil del usuario
sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class AuthManager(context: Context) {

    private val authApiService = RetrofitClient.authApiService
    private val sessionManager = SessionManager(context)

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Idle)
    val loginState: LiveData<UiState<Unit>> = _loginState

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    private val _profileUpdateState = MutableLiveData<UiState<Unit>>()
    val profileUpdateState: LiveData<UiState<Unit>> = _profileUpdateState

    fun login(scope: CoroutineScope, email: String, password: String) {
        _loginState.value = UiState.Loading
        scope.launch {
            try {
                val response = authApiService.loginUser(AuthRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.token)
                    sessionManager.saveUserRole(loginResponse.role)
                    sessionManager.saveUserId(loginResponse.userId)
                    _loginState.postValue(UiState.Success(Unit))
                } else {
                    _loginState.postValue(UiState.Error("Email o contraseña incorrectos."))
                }
            } catch (e: Exception) {
                _loginState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun getProfile(scope: CoroutineScope) {
        _profileState.value = ProfileState.Loading
        scope.launch {
            try {
                val response = authApiService.getMyProfile()
                if (response.isSuccessful && response.body() != null) {
                    _profileState.postValue(ProfileState.Success(response.body()!!))
                } else {
                    _profileState.postValue(ProfileState.Error("Error al cargar el perfil."))
                }
            } catch (e: Exception) {
                _profileState.postValue(ProfileState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateProfile(scope: CoroutineScope, name: String, password: String?) {
        _profileUpdateState.value = UiState.Loading
        scope.launch {
            try {
                val request = UpdateProfileRequest(name, password.takeIf { !it.isNullOrBlank() })
                val response = authApiService.updateMyProfile(request)
                if (response.isSuccessful) {
                    _profileUpdateState.postValue(UiState.Success(Unit))
                } else {
                    _profileUpdateState.postValue(UiState.Error("Error al actualizar el perfil."))
                }
            } catch (e: Exception) {
                _profileUpdateState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = UiState.Idle
    }

    fun resetProfileUpdateState() {
        _profileUpdateState.value = UiState.Idle
    }
}