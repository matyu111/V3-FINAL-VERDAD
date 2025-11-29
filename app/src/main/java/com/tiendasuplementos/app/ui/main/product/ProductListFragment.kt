package com.tiendasuplementos.app.ui.main.product

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentProductListBinding
// Corregido: Importar desde el paquete correcto
import com.tiendasuplementos.app.ui.main.product.CreateProductFragment 
import com.tiendasuplementos.app.ui.main.product.EditProductFragment
import com.tiendasuplementos.app.util.SessionManager

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productManager: ProductManager
    private lateinit var productAdapter: ProductAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        productManager = ProductManager.getInstance(requireContext())
        val userRole = sessionManager.fetchUserRole()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Productos"

        if (userRole == "admin") {
            binding.fabAddProduct.isVisible = true
            binding.fabAddProduct.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, CreateProductFragment())
                    .addToBackStack(null)
                    .commit()
            }
        } else {
            binding.fabAddProduct.isVisible = false
        }

        productAdapter = ProductAdapter(userRole) { product ->
            if (userRole == "admin") {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, EditProductFragment(product))
                    .addToBackStack(null)
                    .commit()
            } else {
                productManager.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} añadido al carrito", Toast.LENGTH_SHORT).show()
            }
        }
        binding.productsRecyclerView.adapter = productAdapter
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        productManager.productListState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is ProductListState.Loading
            binding.productsRecyclerView.isVisible = state is ProductListState.Success

            if (state is ProductListState.Success) {
                productAdapter.submitList(state.products)
                binding.emptyTextView.isVisible = state.products.isEmpty()
            } else {
                binding.emptyTextView.isVisible = false
            }
        }

        productManager.fetchProducts(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Buscar por nombre..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                productManager.searchProducts(newText)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}