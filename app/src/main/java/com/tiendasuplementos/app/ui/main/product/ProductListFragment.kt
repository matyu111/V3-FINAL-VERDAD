package com.tiendasuplementos.app.ui.main.product

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.R

class ProductListFragment : Fragment() {

    private val productViewModel: ProductViewModel by activityViewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true) // Habilita el menú para este fragment
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerView = view.findViewById(R.id.productsRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        emptyTextView = view.findViewById(R.id.emptyTextView)

        productAdapter = ProductAdapter()
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeProductListState()
        observeCreationState()

        // Carga inicial de productos
        productViewModel.fetchProducts()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Buscar por nombre..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No hacemos nada al presionar "Enter"
            }

            // Se llama cada vez que el usuario escribe o borra una letra
            override fun onQueryTextChange(newText: String?): Boolean {
                // Llama a la función de filtrado local del ViewModel
                productViewModel.searchProducts(newText)
                return true
            }
        })
    }

    private fun observeProductListState() {
        productViewModel.productListState.observe(viewLifecycleOwner) { state ->
            progressBar.visibility = if (state is ProductListState.Loading) View.VISIBLE else View.GONE
            recyclerView.visibility = if (state is ProductListState.Success && state.products.isNotEmpty()) View.VISIBLE else View.GONE
            emptyTextView.visibility = if (state is ProductListState.Success && state.products.isEmpty()) View.VISIBLE else View.GONE

            when (state) {
                is ProductListState.Success -> productAdapter.submitList(state.products)
                is ProductListState.Error -> {
                    emptyTextView.visibility = View.VISIBLE
                    emptyTextView.text = "Error: ${state.message}"
                }
                else -> {}
            }
        }
    }

    private fun observeCreationState() {
        productViewModel.creationState.observe(viewLifecycleOwner) { state ->
            if (state is CreationState.Success) {
                productViewModel.fetchProducts() // Recarga la lista después de crear
            }
        }
    }
}