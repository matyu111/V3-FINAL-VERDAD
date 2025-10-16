package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProductApiService {

    // Revertido: Vuelve a ser una llamada GET simple sin parámetros
    @GET("product")
    suspend fun getProducts(): Response<List<Product>>

    @Multipart
    @POST("product")
    suspend fun createProduct(
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody
    ): Response<Product>
}