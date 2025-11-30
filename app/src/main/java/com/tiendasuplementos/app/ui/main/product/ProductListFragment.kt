package com.tiendasuplementos.app.ui.main.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentProductListBinding
import com.tiendasuplementos.app.ui.state.ProductListState

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManager = ProductManager.getInstance(requireContext())

        setupViews()
        setupObservers()

        // Cargar los productos al iniciar el fragment
        productManager.fetchProducts(lifecycleScope)
    }

    private fun setupViews() {
        binding.textFieldSearch.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productManager.searchProducts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupObservers() {
        productManager.productListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProductListState.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.recyclerViewProducts.isVisible = false
                    binding.textViewError.isVisible = false
                }
                is ProductListState.Success -> {
                    binding.progressBar.isVisible = false
                    binding.textViewError.isVisible = false
                    binding.recyclerViewProducts.isVisible = true
                    binding.recyclerViewProducts.adapter = ProductAdapter(state.products) { product ->
                        // Añadir al carrito al hacer clic
                        productManager.addToCart(product)
                        Toast.makeText(requireContext(), "'${product.name}' añadido al carrito", Toast.LENGTH_SHORT).show()
                    }
                }
                is ProductListState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerViewProducts.isVisible = false
                    binding.textViewError.isVisible = true
                    binding.textViewError.text = state.message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}