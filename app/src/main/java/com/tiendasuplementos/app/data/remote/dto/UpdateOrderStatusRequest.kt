package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

// Clase de datos específica para actualizar el estado de una orden
data class UpdateOrderStatusRequest(
    @SerializedName("status") val status: String
)
