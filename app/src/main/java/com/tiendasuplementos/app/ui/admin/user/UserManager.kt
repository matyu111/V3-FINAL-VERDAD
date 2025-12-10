package com.tiendasuplementos.app.ui.admin.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.UpdateStatusRequest
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Estados para la lista de usuarios
sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}

class UserManager private constructor(context: Context) {

    // Cambiado a UserApiService que apunta al endpoint correcto (Store API)
    private val userApiService = RetrofitClient.userApiService

    private val _userListState = MutableLiveData<UserListState>()
    val userListState: LiveData<UserListState> = _userListState

    // Usamos UiState<Unit> para operaciones que no devuelven datos específicos más allá del éxito
    private val _userUpdateState = MutableLiveData<UiState<Unit>>()
    val userUpdateState: LiveData<UiState<Unit>> = _userUpdateState

    fun fetchUsers(scope: CoroutineScope) {
        _userListState.value = UserListState.Loading
        scope.launch {
            try {
                // Pasamos null como query ya que queremos todos los usuarios
                val response = userApiService.getUsers(null)
                if (response.isSuccessful && response.body() != null) {
                    _userListState.postValue(UserListState.Success(response.body()!!))
                } else {
                    _userListState.postValue(UserListState.Error("Error al cargar los usuarios."))
                }
            } catch (e: Exception) {
                _userListState.postValue(UserListState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateUserStatus(scope: CoroutineScope, userId: Int, newStatus: String) {
        _userUpdateState.value = UiState.Loading
        scope.launch {
            try {
                // Convertir el status string a boolean para el DTO
                // Asumimos que "blocked" significa true, cualquier otra cosa false (active)
                val isBlocked = newStatus.equals("blocked", ignoreCase = true)
                // Usamos toggleUserStatus del UserApiService con UpdateStatusRequest
                val response = userApiService.toggleUserStatus(userId, UpdateStatusRequest(isBlocked))
                
                if (response.isSuccessful) {
                    _userUpdateState.postValue(UiState.Success(Unit))
                    // Opcional: Recargar la lista para mostrar el cambio al instante
                    fetchUsers(this)
                } else {
                    _userUpdateState.postValue(UiState.Error("Error al actualizar el usuario."))
                }
            } catch (e: Exception) {
                _userUpdateState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun resetUserUpdateState() {
        _userUpdateState.value = UiState.Idle
    }

    companion object {
        @Volatile
        private var INSTANCE: UserManager? = null

        fun getInstance(context: Context): UserManager {
            return INSTANCE ?: synchronized(this) {
                val instance = UserManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
