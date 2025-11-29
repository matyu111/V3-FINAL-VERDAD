package com.tiendasuplementos.app.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentProfileBinding
import com.tiendasuplementos.app.ui.auth.LoginActivity
import com.tiendasuplementos.app.util.SessionManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileManager: ProfileManager
    private lateinit var sessionManager: SessionManager
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileManager = ProfileManager(requireContext())
        sessionManager = SessionManager(requireContext())

        setupUI()
        observeViewModel()
        
        // Se llama solo a la función principal de carga de datos
        fetchData(isInitialLoad = true)
    }

    private fun fetchData(isInitialLoad: Boolean) {
        // Mostrar el indicador de refresco solo si el usuario lo activa
        if (!isInitialLoad) {
            binding.swipeRefreshLayout.isRefreshing = true
        }
        profileManager.fetchProfile(lifecycleScope)
    }

    private fun setupUI() {
        orderHistoryAdapter = OrderHistoryAdapter()
        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderHistoryAdapter
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchData(isInitialLoad = false) // El usuario refresca
        }

        binding.buttonLogout.setOnClickListener {
            profileManager.logout(sessionManager)
        }

        binding.buttonEditProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeViewModel() {
        profileManager.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.Loading -> {
                    // No hacemos nada aquí para no sobreescribir los datos que ya están
                }
                is ProfileState.Success -> {
                    binding.textViewName.text = state.user.name
                    binding.textViewEmail.text = state.user.email
                    
                    // Si el perfil se carga con éxito, Y NO es admin, pedimos el historial
                    if (sessionManager.fetchUserRole() != "admin") {
                        profileManager.fetchOrderHistory(lifecycleScope)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false // Ocultar animación
                }
                is ProfileState.Error -> {
                    binding.textViewName.text = "Error"
                    binding.textViewEmail.text = state.message
                    binding.swipeRefreshLayout.isRefreshing = false // Ocultar animación
                }
                is ProfileState.LoggedOut -> {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                is ProfileState.Updated -> {
                    profileManager.fetchProfile(lifecycleScope)
                }
            }
        }

        profileManager.orderHistoryState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is OrderHistoryState.Loading -> {
                    binding.myOrdersTitle.visibility = View.VISIBLE
                }
                is OrderHistoryState.Success -> {
                    binding.myOrdersTitle.visibility = View.VISIBLE
                    
                    if (state.orders.isEmpty()) {
                        binding.emptyOrdersTextView.visibility = View.VISIBLE
                        binding.ordersRecyclerView.visibility = View.GONE
                    } else {
                        binding.emptyOrdersTextView.visibility = View.GONE
                        binding.ordersRecyclerView.visibility = View.VISIBLE
                        orderHistoryAdapter.submitList(state.orders)
                    }
                }
                is OrderHistoryState.Error -> {
                    binding.myOrdersTitle.visibility = View.GONE
                    binding.emptyOrdersTextView.visibility = View.VISIBLE
                    binding.emptyOrdersTextView.text = state.message // Mostrar mensaje de error
                    binding.ordersRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}