package com.tiendasuplementos.app.data.remote.api

import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProductApiService {

    @GET("product")
    suspend fun getProducts(): Response<List<Product>>

    @Multipart
    @POST("product")
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Product>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") productId: Int): Response<Unit>

    @Multipart
    @PATCH("product/{id}")
    suspend fun updateProduct(
        @Path("id") productId: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<Product>

    @POST("orders")
    suspend fun createOrder(@Body orderRequest: OrderRequest): Response<Unit>
}
