package com.tiendasuplementos.app.ui.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiendasuplementos.app.databinding.FragmentCartBinding
import com.tiendasuplementos.app.ui.main.product.ProductManager
import com.tiendasuplementos.app.ui.main.product.UiState
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManager = ProductManager.getInstance(requireContext())

        cartAdapter = CartAdapter(
            onIncrease = { productManager.increaseCartItemQuantity(it) },
            onDecrease = { productManager.decreaseCartItemQuantity(it) },
            onRemove = { productManager.removeCartItem(it) }
        )

        binding.cartRecyclerView.adapter = cartAdapter
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        productManager.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)
            val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            val formattedTotal = format.format(totalAmount)
            binding.totalAmountTextView.text = "Total: $formattedTotal"

            binding.emptyCartTextView.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
            binding.checkoutSection.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE
        }

        productManager.orderState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    Toast.makeText(requireContext(), "¡Orden creada con éxito!", Toast.LENGTH_LONG).show()
                    productManager.clearCart() // Limpiar el carrito después de una orden exitosa
                    productManager.resetOrderState()
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_LONG).show()
                    productManager.resetOrderState()
                }
                else -> {}
            }
        }

        binding.buttonCheckout.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        val shippingAddress = binding.editTextShippingAddress.text.toString().trim()
        if (shippingAddress.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, ingresa una dirección de envío", Toast.LENGTH_SHORT).show()
            return
        }

        val cartItems = productManager.cartItems.value ?: return
        if (cartItems.isEmpty()) return

        val totalAmount = cartItems.sumOf { it.product.price * it.quantity }
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        val formattedTotal = format.format(totalAmount)

        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Pedido")
            .setMessage("¿Deseas confirmar tu pedido por un total de $formattedTotal a la dirección \"$shippingAddress\"?")
            .setPositiveButton("Confirmar") { _, _ ->
                productManager.createOrder(lifecycleScope, shippingAddress)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}