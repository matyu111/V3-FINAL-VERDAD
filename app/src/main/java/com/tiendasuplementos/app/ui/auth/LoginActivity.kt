package com.tiendasuplementos.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.ActivityLoginBinding
import com.tiendasuplementos.app.ui.main.MainActivity
import com.tiendasuplementos.app.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authManager: AuthManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Corregido: Se pasa el contexto al AuthManager
        authManager = AuthManager(this)
        sessionManager = SessionManager(this)

        // Si ya hay sesión, vamos directo al MainActivity
        if (sessionManager.fetchAuthToken() != null) {
            navigateToMain()
        }

        authManager.loginResult.observe(this) { state ->
            binding.progressBar.visibility = if (state is AuthState.Loading) View.VISIBLE else View.GONE

            when (state) {
                is AuthState.Success -> {
                    // Comprobamos si el usuario está bloqueado
                    if (state.response.status == "blocked") {
                        Toast.makeText(this, "Tu cuenta ha sido bloqueada.", Toast.LENGTH_LONG).show()
                    } else {
                        sessionManager.saveAuthToken(state.response.token)
                        sessionManager.saveUserRole(state.response.role)
                        navigateToMain()
                    }
                }
                is AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotBlank() && password.isNotBlank()) {
                authManager.login(lifecycleScope, email, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}