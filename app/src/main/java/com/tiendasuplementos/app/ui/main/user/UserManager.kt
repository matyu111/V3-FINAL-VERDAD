package com.tiendasuplementos.app.ui.main.user

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserManager(context: Context) {

    private val userRepository = UserRepository(context)

    private val _userListState = MutableLiveData<UserListState>()
    val userListState: LiveData<UserListState> = _userListState

    private val _userStatusState = MutableLiveData<UserStatusState>(UserStatusState.Idle)
    val userStatusState: LiveData<UserStatusState> = _userStatusState

    fun fetchUsers(scope: CoroutineScope, query: String? = null) {
        _userListState.value = UserListState.Loading
        scope.launch {
            try {
                val response = userRepository.getUsers(query)
                if (response.isSuccessful && response.body() != null) {
                    _userListState.postValue(UserListState.Success(response.body()!!))
                } else {
                    _userListState.postValue(UserListState.Error("Error al cargar usuarios."))
                }
            } catch (e: Exception) {
                _userListState.postValue(UserListState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun toggleUserStatus(scope: CoroutineScope, user: User) {
        _userStatusState.value = UserStatusState.Loading
        scope.launch {
            try {
                val newStatus = if (user.status.equals("active", ignoreCase = true)) "blocked" else "active"
                
                // Corregido: Se pasa el objeto User completo, no solo el ID.
                val response = userRepository.toggleUserStatus(user, newStatus)
                
                if (response.isSuccessful) {
                    _userStatusState.postValue(UserStatusState.Success(user.id))
                    fetchUsers(scope) // Recarga la lista para mostrar el cambio
                } else {
                    _userStatusState.postValue(UserStatusState.Error("Error al cambiar el estado del usuario."))
                }
            } catch (e: Exception) {
                _userStatusState.postValue(UserStatusState.Error("Error de red: ${e.message}"))
            }
        }
    }
}