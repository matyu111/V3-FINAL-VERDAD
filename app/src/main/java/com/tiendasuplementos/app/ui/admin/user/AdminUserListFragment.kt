package com.tiendasuplementos.app.ui.admin.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentAdminUserListBinding
import com.tiendasuplementos.app.ui.state.UiState

class AdminUserListFragment : Fragment() {

    private var _binding: FragmentAdminUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var userManager: UserManager
    private lateinit var adminUserAdapter: AdminUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager.getInstance(requireContext())

        setupRecyclerView()
        setupObservers()

        userManager.fetchUsers(lifecycleScope)
    }

    private fun setupRecyclerView() {
        adminUserAdapter = AdminUserAdapter(
            users = emptyList(),
            onUserStatusChanged = { user, isBlocked ->
                val newStatus = if (isBlocked) "blocked" else "active"
                userManager.updateUserStatus(lifecycleScope, user.id, newStatus)
            }
        )
        binding.recyclerViewUsersAdmin.adapter = adminUserAdapter
    }

    private fun setupObservers() {
        // Observador para la lista de usuarios
        userManager.userListState.observe(viewLifecycleOwner) { state ->
            binding.progressBarUsersAdmin.isVisible = state is UserListState.Loading
            binding.textViewErrorUsersAdmin.isVisible = state is UserListState.Error

            if (state is UserListState.Success) {
                binding.recyclerViewUsersAdmin.isVisible = true
                adminUserAdapter.updateUsers(state.users)
            } else {
                binding.recyclerViewUsersAdmin.isVisible = false
            }
        }

        // Observador para la actualización del estado del usuario
        userManager.userUpdateState.observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success) {
                Toast.makeText(requireContext(), "Estado del usuario actualizado", Toast.LENGTH_SHORT).show()
                userManager.resetUserUpdateState()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}