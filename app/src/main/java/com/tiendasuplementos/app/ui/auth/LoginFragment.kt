package com.tiendasuplementos.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentLoginBinding
import com.tiendasuplementos.app.ui.state.UiState
import com.tiendasuplementos.app.util.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        sessionManager = SessionManager(requireContext())

        // Comprobar si ya hay una sesión activa y redirigir
        if (sessionManager.isLoggedIn()) {
            navigateToDashboard()
            return // Evitar configurar el resto de la pantalla
        }

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        authManager.loginState.observe(viewLifecycleOwner) { state ->
            binding.progressBarLogin.isVisible = state is UiState.Loading

            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                    navigateToDashboard()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    authManager.resetLoginState()
                }
                else -> { /* No-op */ }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.textFieldEmail.editText?.text.toString().trim()
            val password = binding.textFieldPassword.editText?.text.toString().trim()

            if (email.isNotBlank() && password.isNotBlank()) {
                authManager.login(lifecycleScope, email, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDashboard() {
        val role = sessionManager.fetchUserRole()
        // La redirección por rol debe limpiar el historial de navegación
        if (role == "admin") {
            findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
        } else {
            findNavController().navigate(R.id.action_loginFragment_to_productListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        authManager.resetLoginState()
    }
}