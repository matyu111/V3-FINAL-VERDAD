package com.tiendasuplementos.app.ui.main.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class OrderManager {

    private val orderRepository = OrderRepository()

    private val _orderListState = MutableLiveData<OrderListState>()
    val orderListState: LiveData<OrderListState> = _orderListState

    fun fetchOrders(scope: CoroutineScope) {
        _orderListState.value = OrderListState.Loading
        scope.launch {
            try {
                val response = orderRepository.getOrders()
                if (response.isSuccessful && response.body() != null) {
                    _orderListState.postValue(OrderListState.Success(response.body()!!))
                } else {
                    _orderListState.postValue(OrderListState.Error("Error al cargar las órdenes."))
                }
            } catch (e: Exception) {
                _orderListState.postValue(OrderListState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateOrderStatus(scope: CoroutineScope, orderId: Int, newStatus: String) {
        scope.launch {
            try {
                orderRepository.updateOrderStatus(orderId, newStatus)
                // La lista se recargará para reflejar el cambio
                fetchOrders(scope)
            } catch (e: Exception) {
                // Manejar error si es necesario, por ahora solo recargamos
                fetchOrders(scope)
            }
        }
    }
}