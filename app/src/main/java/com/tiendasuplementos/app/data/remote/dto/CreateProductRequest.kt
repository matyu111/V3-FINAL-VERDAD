package com.tiendasuplementos.app.data.remote.dto

data class CreateProductRequest(
    val name: String,
    val description: String, // Campo añadido
    val price: Double,
    val stock: Int
)