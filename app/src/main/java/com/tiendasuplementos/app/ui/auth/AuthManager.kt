package com.tiendasuplementos.app.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.dto.AuthRequest
import com.tiendasuplementos.app.data.repository.AuthRepository
import com.tiendasuplementos.app.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Corregido: Se añade el contexto para poder instanciar el repositorio y SessionManager
class AuthManager(context: Context) {

    private val authRepository = AuthRepository(context)
    private val sessionManager = SessionManager(context)

    private val _loginResult = MutableLiveData<AuthState>()
    val loginResult: LiveData<AuthState> = _loginResult

    fun login(scope: CoroutineScope, email: String, password: String) {
        _loginResult.value = AuthState.Loading
        scope.launch {
            try {
                val response = authRepository.loginUser(AuthRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    // Guardar todos los datos en la sesión
                    sessionManager.saveAuthToken(authResponse.token)
                    sessionManager.saveUserRole(authResponse.role)
                    sessionManager.saveUserId(authResponse.userId)

                    _loginResult.postValue(AuthState.Success(authResponse))
                } else {
                    _loginResult.postValue(AuthState.Error("Credenciales incorrectas"))
                }
            } catch (e: Exception) {
                _loginResult.postValue(AuthState.Error("Error de red: ${e.message}"))
            }
        }
    }
}