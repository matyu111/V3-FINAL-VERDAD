package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    @SerializedName("image")
    val image: ImageUrl? // Revertido: Vuelve a ser un único objeto, y opcional por seguridad
)

data class ImageUrl(
    val url: String?
)
