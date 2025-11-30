package com.tiendasuplementos.app.data.repository

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.data.remote.dto.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

// Nota: Este repositorio conserva el contexto porque es necesario para las operaciones de archivos (subir imágenes).
class ProductRepository(private val context: Context) {

    private val productApiService = RetrofitClient.productApiService

    suspend fun getProducts(): Response<List<Product>> {
        return productApiService.getProducts()
    }

    suspend fun createProduct(name: String, description: String, price: Double, stock: Int, imageUris: List<Uri>): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts = imageUris.map { imageUri ->
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(imageUri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")
            contentResolver.openInputStream(imageUri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images[]", file.name, requestFile)
        }

        return productApiService.createProduct(namePart, descriptionPart, pricePart, stockPart, imageParts)
    }

    suspend fun deleteProduct(productId: Int): Response<Unit> {
        return productApiService.deleteProduct(productId)
    }

    suspend fun updateProduct(productId: Int, name: String, description: String, price: Double, stock: Int, imageUris: List<Uri>): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts = imageUris.map { imageUri ->
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(imageUri)
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.$extension")
            contentResolver.openInputStream(imageUri)?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("images[]", file.name, requestFile)
        }

        return productApiService.updateProduct(productId, namePart, descriptionPart, pricePart, stockPart, imageParts)
    }
}