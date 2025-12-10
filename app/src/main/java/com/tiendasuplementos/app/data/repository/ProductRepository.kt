package com.tiendasuplementos.app.data.repository

import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

// No longer needs context
class ProductRepository {

    private val productApiService = RetrofitClient.productApiService

    suspend fun getProducts(): Response<List<Product>> {
        return productApiService.getProducts()
    }

    private fun createImagePart(imageBytes: ByteArray?, mimeType: String?): MultipartBody.Part? {
        if (imageBytes == null || mimeType == null) return null

        val requestFile = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val fileName = "image.${mimeType.substringAfterLast('/', "jpg")}"
        return MultipartBody.Part.createFormData("image", fileName, requestFile)
    }

    suspend fun createProduct(name: String, description: String, price: Double, stock: Int, imageBytes: ByteArray?, mimeType: String?): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = createImagePart(imageBytes, mimeType)

        return productApiService.createProduct(namePart, descriptionPart, pricePart, stockPart, imagePart)
    }

    suspend fun deleteProduct(productId: Int): Response<Unit> {
        return productApiService.deleteProduct(productId)
    }

    suspend fun updateProduct(productId: Int, name: String, description: String, price: Double, stock: Int, imageBytes: ByteArray?, mimeType: String?): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = createImagePart(imageBytes, mimeType)

        return productApiService.updateProduct(productId, namePart, descriptionPart, pricePart, stockPart, imagePart)
    }

    suspend fun createOrder(orderRequest: OrderRequest): Response<Unit> {
        return productApiService.createOrder(orderRequest)
    }
}
