package com.tiendasuplementos.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateStatusRequest(
    @SerializedName("is_blocked") val is_blocked: Boolean
)
