package com.tiendasuplementos.app.data.repository

import android.content.Context
import android.net.Uri
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.data.remote.dto.UpdateProductRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class ProductRepository(private val context: Context) {

    private val productApiService = RetrofitClient.getProductApiService(context.applicationContext)

    suspend fun getProducts(): Response<List<Product>> {
        return productApiService.getProducts()
    }

    suspend fun createProduct(name: String, description: String, price: Double, stock: Int, imageUri: Uri): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(imageUri)
        val file = File(context.cacheDir, "upload.tmp")
        contentResolver.openInputStream(imageUri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return productApiService.createProduct(imagePart, namePart, descriptionPart, pricePart, stockPart)
    }

    suspend fun deleteProduct(productId: Int): Response<Unit> {
        return productApiService.deleteProduct(productId)
    }

    suspend fun updateProduct(productId: Int, name: String, description: String, price: Double, stock: Int): Response<Product> {
        val request = UpdateProductRequest(name, description, price, stock)
        return productApiService.updateProduct(productId, request)
    }
}