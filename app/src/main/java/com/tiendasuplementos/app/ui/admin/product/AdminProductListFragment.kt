package com.tiendasuplementos.app.ui.admin.product

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.tiendasuplementos.app.databinding.FragmentAdminProductListBinding
import com.tiendasuplementos.app.ui.main.product.ProductManager
import com.tiendasuplementos.app.ui.state.ProductListState
import com.tiendasuplementos.app.ui.state.UiState

class AdminProductListFragment : Fragment() {

    private var _binding: FragmentAdminProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager
    private lateinit var adminProductAdapter: AdminProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productManager = ProductManager.getInstance(requireContext())

        setupRecyclerView()
        setupViews()
        setupObservers()

        productManager.fetchProducts(lifecycleScope)
    }

    private fun setupRecyclerView() {
        adminProductAdapter = AdminProductAdapter(
            products = emptyList(),
            onEditClicked = { product ->
                val action = AdminProductListFragmentDirections.actionAdminProductListFragmentToEditProductFragment(product)
                findNavController().navigate(action)
            },
            onDeleteClicked = { product ->
                showDeleteConfirmationDialog(product.id)
            }
        )
        binding.recyclerViewProductsAdmin.adapter = adminProductAdapter
    }

    private fun setupViews() {
        binding.textFieldSearchAdmin.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productManager.searchProducts(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.fabAddProduct.setOnClickListener {
            val action = AdminProductListFragmentDirections.actionAdminProductListFragmentToEditProductFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        productManager.productListState.observe(viewLifecycleOwner) { state ->
            binding.progressBarAdmin.isVisible = state is ProductListState.Loading
            binding.textViewErrorAdmin.isVisible = state is ProductListState.Error

            if (state is ProductListState.Success) {
                binding.recyclerViewProductsAdmin.isVisible = true
                adminProductAdapter.updateProducts(state.products)
            } else {
                binding.recyclerViewProductsAdmin.isVisible = false
            }
        }

        productManager.deletionState.observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success) {
                Toast.makeText(requireContext(), "Producto eliminado", Toast.LENGTH_SHORT).show()
                productManager.resetDeletionState()
                // No es necesario recargar, el ProductManager ya actualiza la lista local
            }
        }
    }

    private fun showDeleteConfirmationDialog(productId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este producto?")
            .setPositiveButton("Eliminar") { _, _ ->
                productManager.deleteProduct(lifecycleScope, productId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
