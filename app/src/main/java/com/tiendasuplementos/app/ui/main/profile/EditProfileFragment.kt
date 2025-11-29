package com.tiendasuplementos.app.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileManager: ProfileManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Corregido: Se pasa el contexto al ProfileManager
        profileManager = ProfileManager(requireContext())

        profileManager.profileState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is ProfileState.Updated -> {
                    Toast.makeText(requireContext(), getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }
                is ProfileState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                else -> { /* No-op for Loading/Idle */ }
            }
        }

        binding.buttonSaveChanges.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (name.isNotBlank()) {
                profileManager.updateProfile(lifecycleScope, name, password)
            } else {
                Toast.makeText(requireContext(), getString(R.string.name_cannot_be_empty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}