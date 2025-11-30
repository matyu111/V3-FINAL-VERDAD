package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Esta clase envuelve la respuesta de la API que contiene una lista de órdenes.
data class OrderListResponse(
    @SerializedName("orders") // Asegúrate de que el nombre coincida con el de la API
    val orders: List<Order>
)
