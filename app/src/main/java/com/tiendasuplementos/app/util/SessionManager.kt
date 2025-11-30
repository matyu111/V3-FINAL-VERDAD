package com.tiendasuplementos.app.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ROLE = "user_role"
        const val USER_ID = "user_id"
        private const val TAG = "SessionManager"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        Log.d(TAG, "Guardando token: $token")
        editor.commit() // Usar commit para asegurar que se guarde de forma síncrona
    }

    fun fetchAuthToken(): String? {
        val token = prefs.getString(AUTH_TOKEN, null)
        Log.d(TAG, "Recuperando token: $token")
        return token
    }

    fun isLoggedIn(): Boolean {
        return !fetchAuthToken().isNullOrBlank()
    }

    fun saveUserRole(role: String) {
        val editor = prefs.edit()
        editor.putString(USER_ROLE, role)
        Log.d(TAG, "Guardando rol: $role")
        editor.commit()
    }

    fun fetchUserRole(): String? {
        val role = prefs.getString(USER_ROLE, null)
        Log.d(TAG, "Recuperando rol: $role")
        return role
    }

    fun saveUserId(userId: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, userId)
        Log.d(TAG, "Guardando ID de usuario: $userId")
        editor.commit()
    }

    fun fetchUserId(): Int? {
        return if (prefs.contains(USER_ID)) {
            val userId = prefs.getInt(USER_ID, -1)
            Log.d(TAG, "Recuperando ID de usuario: $userId")
            userId
        } else {
            Log.d(TAG, "ID de usuario no encontrado.")
            null
        }
    }

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(AUTH_TOKEN)
        editor.remove(USER_ROLE)
        editor.remove(USER_ID)
        Log.d(TAG, "Limpiando sesión.")
        editor.commit()
    }
}