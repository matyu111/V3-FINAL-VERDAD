package com.tiendasuplementos.app.ui.main.product

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.api.OrderApiService
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.OrderListResponse
import com.tiendasuplementos.app.data.remote.dto.UpdateOrderStatusRequest
import com.tiendasuplementos.app.ui.state.OrderListState
import com.tiendasuplementos.app.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class OrderManager(context: Context) {

    // Corregido: RetrofitClient es un object, se accede directamente a sus propiedades.
    private val orderApiService: OrderApiService = RetrofitClient.orderApiService

    private val _orderListState = MutableLiveData<OrderListState>()
    val orderListState: LiveData<OrderListState> = _orderListState

    private val _orderUpdateState = MutableLiveData<UiState<Order>>()
    val orderUpdateState: LiveData<UiState<Order>> = _orderUpdateState

    fun fetchOrders(scope: CoroutineScope) {
        scope.launch {
            _orderListState.postValue(OrderListState.Loading)
            try {
                val response = orderApiService.getOrders()
                if (response.isSuccessful && response.body() != null) {
                    _orderListState.postValue(OrderListState.Success(response.body()!!))
                } else {
                    _orderListState.postValue(OrderListState.Error("Error al obtener las órdenes"))
                }
            } catch (e: Exception) {
                _orderListState.postValue(OrderListState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun getMyOrders(scope: CoroutineScope) {
        scope.launch {
            _orderListState.postValue(OrderListState.Loading)
            try {
                val response = orderApiService.getMyOrders()
                if (response.isSuccessful && response.body() != null) {
                    _orderListState.postValue(OrderListState.Success(response.body()!!.orders))
                } else {
                    _orderListState.postValue(OrderListState.Error("Error al obtener tus órdenes"))
                }
            } catch (e: Exception) {
                _orderListState.postValue(OrderListState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun updateOrderStatus(scope: CoroutineScope, orderId: Int, status: String) {
        scope.launch {
            _orderUpdateState.postValue(UiState.Loading)
            try {
                val response = orderApiService.updateOrderStatus(orderId, UpdateOrderStatusRequest(status))
                if (response.isSuccessful && response.body() != null) {
                    _orderUpdateState.postValue(UiState.Success(response.body()!!))
                    fetchOrders(scope) // Recargar la lista de órdenes
                } else {
                    _orderUpdateState.postValue(UiState.Error("Error al actualizar la orden"))
                }
            } catch (e: Exception) {
                _orderUpdateState.postValue(UiState.Error(e.message ?: "Error desconocido"))
            }
        }
    }

    fun resetOrderUpdateState() {
        _orderUpdateState.value = UiState.Idle
    }

    companion object {
        @Volatile
        private var INSTANCE: OrderManager? = null

        fun getInstance(context: Context): OrderManager {
            return INSTANCE ?: synchronized(this) {
                val instance = OrderManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
