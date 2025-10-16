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

    // URL Base para Autenticación
    private const val AUTH_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:dDE3VCk5/"

    // URL Base para Productos
    private const val PRODUCT_BASE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:aI3nlWmP/"

    // Instancia del servicio de Autenticación (no necesita token)
    val authApiService: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)
    }

    // Función para obtener el servicio de Productos (necesita token)
    fun getProductApiService(context: Context): ProductApiService {
        val sessionManager = SessionManager(context)

        // Creamos un interceptor para añadir el token a las cabeceras
        val authInterceptor = okhttp3.Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            sessionManager.fetchAuthToken()?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(requestBuilder.build())
        }

        // Creamos un interceptor de logs
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Creamos un cliente de OkHttp y le añadimos los interceptores
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        // Creamos una instancia de Retrofit con el cliente personalizado
        return Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }
}