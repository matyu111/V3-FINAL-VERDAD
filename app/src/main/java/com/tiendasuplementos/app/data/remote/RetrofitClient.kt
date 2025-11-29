package com.tiendasuplementos.app.data.remote

import android.content.Context
import com.tiendasuplementos.app.data.remote.api.AuthApiService
import com.tiendasuplementos.app.data.remote.api.OrderApiService
import com.tiendasuplementos.app.data.remote.api.ProductApiService
import com.tiendasuplementos.app.data.remote.api.UserApiService
import com.tiendasuplementos.app.util.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL Base para el grupo de API de Autenticación y Usuarios
    private const val AUTH_URL = "https://x8ki-letl-twmt.n7.xano.io/api:dDE3VCk5/"

    // URL Base para el grupo de API de la Tienda (Productos, Órdenes)
    private const val STORE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:aI3nlWmP/"

    private var authApiClient: Retrofit? = null
    private var storeApiClient: Retrofit? = null

    private fun getClient(context: Context): OkHttpClient {
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

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private fun getAuthApiClient(context: Context): Retrofit {
        if (authApiClient == null) {
            authApiClient = Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return authApiClient!!
    }

    private fun getStoreApiClient(context: Context): Retrofit {
        if (storeApiClient == null) {
            storeApiClient = Retrofit.Builder()
                .baseUrl(STORE_URL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return storeApiClient!!
    }

    // --- Proveedores de Servicios de API ---

    fun getAuthApiService(context: Context): AuthApiService {
        return getAuthApiClient(context).create(AuthApiService::class.java)
    }

    fun getUserApiService(context: Context): UserApiService {
        // CORREGIDO: El endpoint de usuarios está en el Store API, no en el Auth API
        return getStoreApiClient(context).create(UserApiService::class.java)
    }

    fun getProductApiService(context: Context): ProductApiService {
        return getStoreApiClient(context).create(ProductApiService::class.java)
    }

    fun getOrderApiService(context: Context): OrderApiService {
        return getStoreApiClient(context).create(OrderApiService::class.java)
    }
}