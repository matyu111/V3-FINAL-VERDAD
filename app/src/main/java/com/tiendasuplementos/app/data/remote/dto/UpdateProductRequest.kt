package com.tiendasuplementos.app.data.remote.dto

data class UpdateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int
)
