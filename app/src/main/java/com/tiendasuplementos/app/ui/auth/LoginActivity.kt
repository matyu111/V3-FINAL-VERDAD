package com.tiendasuplementos.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.MyApplication
import com.tiendasuplementos.app.databinding.ActivityLoginBinding
import com.tiendasuplementos.app.MainActivity // Corrected import
import com.tiendasuplementos.app.ui.state.UiState

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val authManager: AuthManager by lazy { AuthManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Usar la instancia única del SessionManager
        val sessionManager = MyApplication.sessionManager

        if (sessionManager.fetchAuthToken() != null) {
            navigateToMain()
            return // Salir para evitar configurar observadores innecesariamente
        }

        authManager.loginState.observe(this) { state ->
            binding.progressBar.visibility = if (state is UiState.Loading) View.VISIBLE else View.GONE

            when (state) {
                is UiState.Success -> {
                    // El AuthManager ya se encarga de guardar la sesión
                    navigateToMain()
                }
                is UiState.Error -> {
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
        finish() // Finalizar LoginActivity para que el usuario no pueda volver atrás
    }
}