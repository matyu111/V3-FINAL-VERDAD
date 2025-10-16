package com.tiendasuplementos.app.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // Creamos una instancia de SharedPreferences en modo privado
    private val prefs: SharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        // Constante para la clave del token
        const val AUTH_TOKEN = "auth_token"
    }

    /**
     * Guarda el token de autenticación en SharedPreferences.
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.apply() // Se usa apply() para guardar los cambios de forma asíncrona
    }

    /**
     * Recupera el token de autenticación. Devuelve null si no existe.
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    /**
     * Borra el token de autenticación, por ejemplo, al cerrar sesión.
     */
    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(AUTH_TOKEN)
        editor.apply()
    }
}