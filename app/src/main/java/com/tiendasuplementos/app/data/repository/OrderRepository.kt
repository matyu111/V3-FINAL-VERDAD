package com.tiendasuplementos.app.data.repository

import android.content.Context
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.UpdateOrderStatusRequest
import retrofit2.Response

class OrderRepository(context: Context) {

    private val orderApiService = RetrofitClient.getOrderApiService(context)

    suspend fun createOrder(orderRequest: OrderRequest): Response<Unit> {
        return orderApiService.createOrder(orderRequest)
    }

    // Para el admin
    suspend fun getOrders(): Response<List<Order>> {
        return orderApiService.getOrders()
    }

    // Para el cliente
    suspend fun getMyOrders(): Response<List<Order>> {
        // El servicio devuelve un OrderListResponse, aquí lo transformamos
        val response = orderApiService.getMyOrders()
        return if (response.isSuccessful) {
            // Si la llamada fue exitosa, extraemos la lista de "items"
            // y creamos una nueva respuesta exitosa solo con esa lista.
            Response.success(response.body()?.items ?: emptyList())
        } else {
            // Si la llamada falló, propagamos el error pero asegurándonos
            // de que el tipo de la respuesta sea el que el ViewModel espera.
            Response.error(response.code(), response.errorBody()!!)
        }
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: String): Response<Order> {
        val statusRequest = UpdateOrderStatusRequest(newStatus)
        return orderApiService.updateOrderStatus(orderId, statusRequest)
    }
}
