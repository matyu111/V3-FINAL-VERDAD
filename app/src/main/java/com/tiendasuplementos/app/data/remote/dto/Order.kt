package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

// Representa una orden individual recibida de la API
data class Order(
    val id: Int,
    @SerializedName("created_at") val createdAt: Long, // Campo de fecha añadido
    @SerializedName("user_id") val userId: Int,
    @SerializedName("cart_items") val cartItems: JsonElement, 
    @SerializedName("total_amount") val totalAmount: Double,
    val status: String,
    @SerializedName("shipping_address") val shippingAddress: String? = null // Campo añadido, puede ser nulo
)
