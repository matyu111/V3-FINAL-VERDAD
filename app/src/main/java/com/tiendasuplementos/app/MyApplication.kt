package com.tiendasuplementos.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import com.tiendasuplementos.app.data.remote.RetrofitClient
import com.tiendasuplementos.app.util.SessionManager

class MyApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        instance = this
        sessionManager = SessionManager(this)
        // Inicializa el RetrofitClient una sola vez
        RetrofitClient.initialize(this)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .okHttpClient { RetrofitClient.okHttpClient } // Reutiliza el OkHttpClient
            .build()
    }

    companion object {
        lateinit var instance: MyApplication
            private set
            
        lateinit var sessionManager: SessionManager
            private set
    }
}
