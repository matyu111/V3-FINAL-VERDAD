package com.tiendasuplementos.app.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int,
    @SerializedName("image")
    val image: ImageUrl?
) : Parcelable

@Parcelize
data class ImageUrl(
    val url: String?
) : Parcelable
