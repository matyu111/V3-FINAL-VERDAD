package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta clase envuelve la respuesta de la API que contiene una lista de órdenes.
data class OrderListResponse(
    @SerializedName("items") // Nombre corregido para coincidir con la respuesta de Xano
    val orders: List<Order>
)
