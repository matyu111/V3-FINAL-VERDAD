package com.tiendasuplementos.app.ui.main.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentCartBinding
import com.tiendasuplementos.app.ui.main.product.ProductManager

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

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems = emptyList(),
            onIncrease = { cartItem -> productManager.increaseCartItemQuantity(cartItem) },
            onDecrease = { cartItem -> productManager.decreaseCartItemQuantity(cartItem) },
            onRemove = { cartItem -> productManager.removeCartItem(cartItem) }
        )
        binding.recyclerViewCart.adapter = cartAdapter
    }

    private fun setupObservers() {
        productManager.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.updateCartItems(cartItems)

            // Update visibility and total
            val isEmpty = cartItems.isEmpty()
            binding.textViewEmptyCart.isVisible = isEmpty
            binding.bottomBar.isVisible = !isEmpty

            if (!isEmpty) {
                val total = cartItems.sumOf { it.product.price * it.quantity }
                binding.textViewTotal.text = "Total: $${String.format("%.2f", total)}"
            }
        }
    }

    private fun setupClickListeners() {
        binding.buttonCheckout.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}