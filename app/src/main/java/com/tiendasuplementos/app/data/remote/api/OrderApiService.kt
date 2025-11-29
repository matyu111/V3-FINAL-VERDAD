package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.OrderListResponse // Importar la nueva clase
import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.UpdateOrderStatusRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApiService {

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<Unit>

    @GET("orders")
    suspend fun getOrders(): Response<List<Order>>

    // Corregido: La función ahora espera un OrderListResponse
    @GET("my_orders")
    suspend fun getMyOrders(): Response<OrderListResponse>

    @PATCH("orders/{order_id}")
    suspend fun updateOrderStatus(
        @Path("order_id") orderId: Int,
        @Body statusRequest: UpdateOrderStatusRequest
    ): Response<Order>
}
