package com.tiendasuplementos.app.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.data.repository.AuthRepository
import com.tiendasuplementos.app.data.repository.OrderRepository
import com.tiendasuplementos.app.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

sealed class OrderHistoryState {
    object Loading : OrderHistoryState()
    data class Success(val orders: List<Order>) : OrderHistoryState()
    data class Error(val message: String) : OrderHistoryState()
}

class ProfileManager() {

    private val authRepository = AuthRepository()
    private val orderRepository = OrderRepository() // Repositorio de órdenes añadido

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    private val _orderHistoryState = MutableLiveData<OrderHistoryState>()
    val orderHistoryState: LiveData<OrderHistoryState> = _orderHistoryState

    fun fetchProfile(scope: CoroutineScope) {
        _profileState.value = ProfileState.Loading
        scope.launch {
            try {
                val response = authRepository.getMyProfile()
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

    fun fetchOrderHistory(scope: CoroutineScope) {
        _orderHistoryState.value = OrderHistoryState.Loading
        scope.launch {
            try {
                val response = orderRepository.getMyOrders()
                if (response.isSuccessful && response.body() != null) {
                    _orderHistoryState.postValue(OrderHistoryState.Success(response.body()!!.orders))
                } else {
                    _orderHistoryState.postValue(OrderHistoryState.Error("Error al obtener el historial de pedidos."))
                }
            } catch (e: Exception) {
                _orderHistoryState.postValue(OrderHistoryState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateProfile(scope: CoroutineScope, name: String, password: String?) {
        _profileState.value = ProfileState.Loading
        scope.launch {
            try {
                val pass = password?.takeIf { it.isNotEmpty() }
                val response = authRepository.updateMyProfile(name, pass)
                if (response.isSuccessful) {
                    _profileState.postValue(ProfileState.Updated)
                } else {
                    _profileState.postValue(ProfileState.Error("Error al actualizar el perfil."))
                }
            } catch (e: Exception) {
                _profileState.postValue(ProfileState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun logout(sessionManager: SessionManager) {
        sessionManager.clearAuthToken()
        _profileState.value = ProfileState.LoggedOut
    }
}
