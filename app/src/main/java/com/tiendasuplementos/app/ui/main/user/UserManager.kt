package com.tiendasuplementos.app.ui.main.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.UpdateUserStatusRequest
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserManager private constructor(context: Context) {

    private val authApiService = RetrofitClient.authApiService

    private val _userListState = MutableLiveData<UserListState>()
    val userListState: LiveData<UserListState> = _userListState

    private val _userUpdateState = MutableLiveData<UiState<Unit>>()
    val userUpdateState: LiveData<UiState<Unit>> = _userUpdateState

    fun fetchUsers(scope: CoroutineScope) {
        _userListState.value = UserListState.Loading
        scope.launch {
            try {
                val response = authApiService.getUsers()
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
                val isBlocked = newStatus.equals("blocked", ignoreCase = true)
                val response = authApiService.updateUserStatus(userId, UpdateUserStatusRequest(isBlocked))
                
                if (response.isSuccessful) {
                    _userUpdateState.postValue(UiState.Success(Unit))
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
