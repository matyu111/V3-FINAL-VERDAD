package com.tiendasuplementos.app.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.ui.auth.LoginActivity
import com.tiendasuplementos.app.ui.main.product.DeletionState
import com.tiendasuplementos.app.ui.main.product.ProductViewModel

class ProfileFragment : Fragment() {

    private val profileViewModel: ProfileViewModel by viewModels()
    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView: TextView = view.findViewById(R.id.textViewName)
        val emailTextView: TextView = view.findViewById(R.id.textViewEmail)
        val logoutButton: Button = view.findViewById(R.id.buttonLogout)
        val deleteEditText: EditText = view.findViewById(R.id.editTextProductIdToDelete)
        val deleteButton: Button = view.findViewById(R.id.buttonDeleteProduct)

        profileViewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.Loading -> {
                    nameTextView.text = "Cargando..."
                    emailTextView.text = "Cargando..."
                }
                is ProfileState.Success -> {
                    nameTextView.text = state.user.name
                    emailTextView.text = state.user.email
                }
                is ProfileState.Error -> {
                    nameTextView.text = "Error"
                    emailTextView.text = state.message
                }
                is ProfileState.LoggedOut -> {
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        productViewModel.deletionState.observe(viewLifecycleOwner) { state ->
            when(state) {
                is DeletionState.Success -> {
                    Toast.makeText(requireContext(), "Producto borrado con éxito", Toast.LENGTH_SHORT).show()
                    productViewModel.resetDeletionState()
                    // No es necesario recargar desde la red, el ViewModel lo hace localmente
                }
                is DeletionState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                    productViewModel.resetDeletionState()
                }
                else -> {}
            }
        }

        logoutButton.setOnClickListener {
            profileViewModel.logout()
        }

        deleteButton.setOnClickListener {
            val productId = deleteEditText.text.toString().toIntOrNull()
            if (productId != null) {
                productViewModel.deleteProduct(productId)
            } else {
                Toast.makeText(requireContext(), "Por favor, introduce un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.fetchProfile()
    }
}