package com.tiendasuplementos.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.ui.main.MainActivity
import com.tiendasuplementos.app.util.SessionManager

class LoginActivity : AppCompatActivity() {

    // Inicializa el ViewModel usando la delegación de KTX
    private val loginViewModel: LoginViewModel by viewModels()

    // Declara las vistas
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    // Declara el SessionManager
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa SessionManager
        sessionManager = SessionManager(this)

        // Vincula las vistas con sus IDs del XML
        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)

        // Configura el listener del botón
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Observa los cambios de estado del ViewModel
        observeLoginState()
    }

    private fun observeLoginState() {
        loginViewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Loading -> {
                    // Muestra el ProgressBar y deshabilita el botón


                   progressBar.visibility = View.VISIBLE
                    loginButton.isEnabled = false
                }
                is LoginState.Success -> {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                    Toast.makeText(this, "¡Login exitoso!", Toast.LENGTH_SHORT).show()

                    // Guarda el token
                    sessionManager.saveAuthToken(state.token)

                    // NAVEGA A LA PANTALLA DE PRODUCTOS
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    
                    // Cierra LoginActivity para que el usuario no pueda volver con el botón "atrás"
                    finish()
                }
                is LoginState.Error -> {
                    // Oculta el ProgressBar, habilita el botón y muestra el error
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                }
                is LoginState.Idle -> {
                    // Estado inicial, no hacer nada
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                }
            }
        }
    }
}