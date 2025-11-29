package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta clase representa la estructura de la respuesta del endpoint /my_orders
data class OrderListResponse(
    @SerializedName("items") val items: List<Order>
)
