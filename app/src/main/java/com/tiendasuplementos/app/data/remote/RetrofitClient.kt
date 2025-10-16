package com.tiendasuplementos.app.data.remote

import android.content.Context
import com.tiendasuplementos.app.data.remote.api.AuthApiService
import com.tiendasuplementos.app.data.remote.api.ProductApiService
import com.tiendasuplementos.app.util.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL Base para Autenticación (¡Esta es la correcta!)
    private const val AUTH_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:dDE3VCk5/"
    // URL Base para Productos
    private const val PRODUCT_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:aI3nlWmP/"

    // Cliente Retrofit para Autenticación
    private val authRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia del servicio de Autenticación
    val authApiService: AuthApiService by lazy {
        authRetrofit.create(AuthApiService::class.java)
    }

    // El resto del código para ProductApiService se mantiene igual...
    fun getProductApiService(context: Context): ProductApiService {
        val sessionManager = SessionManager(context)
        val authInterceptor = okhttp3.Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            sessionManager.fetchAuthToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}