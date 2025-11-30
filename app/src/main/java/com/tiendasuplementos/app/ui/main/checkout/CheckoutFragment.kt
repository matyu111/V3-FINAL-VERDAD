package com.tiendasuplementos.app.ui.main.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tiendasuplementos.app.databinding.FragmentCheckoutBinding
import com.tiendasuplementos.app.ui.main.product.ProductManager
import com.tiendasuplementos.app.ui.state.UiState

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManager = ProductManager.getInstance(requireContext())

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Observar el carrito para mostrar el total
        productManager.cartItems.observe(viewLifecycleOwner) { cartItems ->
            if (cartItems.isNotEmpty()) {
                val total = cartItems.sumOf { it.product.price * it.quantity }
                binding.textViewOrderSummary.text = "Total del Pedido: $${String.format("%.2f", total)}"
            }
        }

        // Observar el estado de la creación de la orden
        productManager.orderState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "Pedido realizado con éxito", Toast.LENGTH_LONG).show()
                    productManager.clearCart() // Limpiar el carrito
                    productManager.resetOrderState()
                    // Navegar de vuelta al catálogo (o a una pantalla de "mis pedidos")
                    findNavController().popBackStack(com.tiendasuplementos.app.R.id.productListFragment, false)
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    productManager.resetOrderState()
                }
                else -> { /* No-op */ }
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonConfirmOrder.setOnClickListener {
            val shippingAddress = binding.textFieldShippingAddress.editText?.text.toString().trim()

            if (shippingAddress.isNotBlank()) {
                productManager.createOrder(lifecycleScope, shippingAddress)
            } else {
                Toast.makeText(requireContext(), "Por favor, ingresa una dirección de envío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productManager.resetOrderState()
    }
}