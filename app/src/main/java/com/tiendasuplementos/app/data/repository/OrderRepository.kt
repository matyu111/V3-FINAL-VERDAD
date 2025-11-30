package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.OrderListResponse
import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.UpdateOrderStatusRequest
import retrofit2.Response

class OrderRepository {

    private val orderApiService = RetrofitClient.orderApiService

    suspend fun createOrder(orderRequest: OrderRequest): Response<Unit> {
        return orderApiService.createOrder(orderRequest)
    }

    suspend fun getOrders(): Response<List<Order>> {
        return orderApiService.getOrders()
    }

    suspend fun getMyOrders(): Response<OrderListResponse> {
        return orderApiService.getMyOrders()
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: String): Response<Order> {
        val statusRequest = UpdateOrderStatusRequest(newStatus)
        return orderApiService.updateOrderStatus(orderId, statusRequest)
    }
}
