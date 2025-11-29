package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Este es el objeto principal que enviaremos a la API
data class OrderRequest(
    @SerializedName("user_id") val userId: Int, // Campo añadido
    @SerializedName("cart_items") val cartItems: List<CartItemRequest>,
    @SerializedName("total_amount") val totalAmount: Double,
    @SerializedName("shipping_address") val shippingAddress: String
)

// Un objeto simplificado para representar cada ítem del carrito en el JSON
data class CartItemRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("product_name") val productName: String // Es bueno tener el nombre para referencia
)
