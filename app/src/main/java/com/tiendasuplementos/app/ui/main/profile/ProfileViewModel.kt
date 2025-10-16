package com.tiendasuplementos.app.ui.main.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.data.repository.AuthRepository
import com.tiendasuplementos.app.util.SessionManager
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object LoggedOut : ProfileState()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val sessionManager = SessionManager(application)

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    fun fetchProfile() {
        _profileState.value = ProfileState.Loading
        viewModelScope.launch {
            val token = sessionManager.fetchAuthToken()
            if (token == null) {
                _profileState.postValue(ProfileState.Error("No se encontró el token de sesión."))
                return@launch
            }

            try {
                val response = authRepository.getProfile(token)
                if (response.isSuccessful && response.body() != null) {
                    _profileState.postValue(ProfileState.Success(response.body()!!))
                } else {
                    _profileState.postValue(ProfileState.Error("Error al obtener el perfil."))
                }
            } catch (e: Exception) {
                _profileState.postValue(ProfileState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun logout() {
        sessionManager.clearAuthToken()
        _profileState.value = ProfileState.LoggedOut
    }
}