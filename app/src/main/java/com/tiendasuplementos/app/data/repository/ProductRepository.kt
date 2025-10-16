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

class ProductRepository(private val context: Context) {

    private val productApiService = RetrofitClient.getProductApiService(context.applicationContext)

    suspend fun getProducts(): Response<List<Product>> {
        return productApiService.getProducts()
    }

    suspend fun createProduct(name: String, price: Double, stock: Int, imageUri: Uri): Response<Product> {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(imageUri)
        val fileExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        val fileName = "upload_${System.currentTimeMillis()}.$fileExtension"
        val file = File(context.cacheDir, fileName)

        contentResolver.openInputStream(imageUri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        val requestFile = file.asRequestBody(mimeType?.toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return productApiService.createProduct(imagePart, namePart, pricePart, stockPart)
    }

    // Nueva función para borrar un producto
    suspend fun deleteProduct(productId: Int): Response<Unit> {
        return productApiService.deleteProduct(productId)
    }
}