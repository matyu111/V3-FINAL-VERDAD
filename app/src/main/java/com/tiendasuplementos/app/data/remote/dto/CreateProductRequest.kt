package com.tiendasuplementos.app.data.remote.dto

data class CreateProductRequest(
    val name: String,
    val price: Double,
    val stock: Int,
    // La API de Xano puede inferir el user_id del token de autenticación.
    // Si necesitas enviarlo manually, inclúyelo aquí.
    // val user_id: Int
)