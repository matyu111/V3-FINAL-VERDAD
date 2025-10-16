package com.tiendasuplementos.app.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.repository.AuthRepository
import kotlinx.coroutines.launch

// Representa los diferentes estados del proceso de login para la UI
sealed class LoginState {
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
    object Idle : LoginState()
}


class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    // _loginState es privado y mutable, solo el ViewModel puede cambiarlo
    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    // loginState es público e inmutable, la UI solo puede observar sus cambios
    val loginState: LiveData<LoginState> = _loginState

    fun login(email: String, password: String) {
        // Evita iniciar un nuevo login si ya hay uno en progreso
        if (_loginState.value == LoginState.Loading) {
            return
        }

        // Cambia el estado a Cargando
        _loginState.value = LoginState.Loading

        // Usamos viewModelScope para lanzar una corutina que se cancelará automáticamente si el ViewModel se destruye
        viewModelScope.launch {
            try {
                val authRequest = AuthRequest(email, password)
                val response = authRepository.loginUser(authRequest)

                if (response.isSuccessful && response.body() != null) {
                    val authToken = response.body()!!.token
                    _loginState.postValue(LoginState.Success(authToken))
                } else {
                    _loginState.postValue(LoginState.Error("Credenciales inválidas o error del servidor."))
                }
            } catch (e: Exception) {
                _loginState.postValue(LoginState.Error("Error de red: ${e.message}"))
            }
        }
    }   
}