package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val name: String,
    val description: String?, // CORREGIDO: La descripción ahora puede ser nula
    val price: Double,
    val stock: Int,
    @SerializedName("image")
    val image: ImageUrl?
)

data class ImageUrl(
    val url: String?
)
