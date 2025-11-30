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

    private const val AUTH_URL = "https://x8ki-letl-twmt.n7.xano.io/api:dDE3VCk5/"
    private const val STORE_URL = "https://x8ki-letl-twmt.n7.xano.io/api:aI3nlWmP/"

    lateinit var okHttpClient: OkHttpClient
        private set

    private val authApiClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(AUTH_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val storeApiClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(STORE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun initialize(context: Context) {
        if (::okHttpClient.isInitialized) return

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

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val authApiService: AuthApiService by lazy {
        authApiClient.create(AuthApiService::class.java)
    }

    val userApiService: UserApiService by lazy {
        storeApiClient.create(UserApiService::class.java)
    }

    val productApiService: ProductApiService by lazy {
        storeApiClient.create(ProductApiService::class.java)
    }

    val orderApiService: OrderApiService by lazy {
        storeApiClient.create(OrderApiService::class.java)
    }
}