package com.tiendasuplementos.app.ui.main.profile

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
import com.tiendasuplementos.app.databinding.FragmentProfileBinding
import com.tiendasuplementos.app.ui.auth.AuthManager
import com.tiendasuplementos.app.ui.auth.ProfileState
import com.tiendasuplementos.app.ui.state.UiState

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())

        setupObservers()
        setupClickListeners()

        // Cargar los datos del perfil al iniciar
        authManager.getProfile(lifecycleScope)
    }

    private fun setupObservers() {
        authManager.profileState.observe(viewLifecycleOwner) { state ->
            binding.progressBarProfile.isVisible = state is ProfileState.Loading

            if (state is ProfileState.Success) {
                binding.textFieldProfileName.editText?.setText(state.user.name)
                binding.textFieldProfileEmail.editText?.setText(state.user.email)
            }
        }

        authManager.profileUpdateState.observe(viewLifecycleOwner) { state ->
            binding.progressBarProfile.isVisible = state is UiState.Loading

            if (state is UiState.Success<*>) {
                Toast.makeText(requireContext(), "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
                authManager.resetProfileUpdateState()
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonSaveChanges.setOnClickListener {
            val name = binding.textFieldProfileName.editText?.text.toString().trim()
            val password = binding.textFieldProfilePassword.editText?.text.toString()

            if (name.isNotBlank()) {
                authManager.updateProfile(lifecycleScope, name, password)
            } else {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonMyOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myOrdersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        authManager.resetProfileUpdateState()
    }
}
