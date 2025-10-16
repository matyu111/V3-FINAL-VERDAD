package com.tiendasuplementos.app.ui.main.product

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.data.repository.ProductRepository
import kotlinx.coroutines.launch

sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}

sealed class CreationState {
    object Loading : CreationState()
    object Success : CreationState()
    data class Error(val message: String) : CreationState()
    object Idle : CreationState()
}

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository(application.applicationContext)

    private val _productListState = MutableLiveData<ProductListState>()
    val productListState: LiveData<ProductListState> = _productListState

    private val _creationState = MutableLiveData<CreationState>(CreationState.Idle)
    val creationState: LiveData<CreationState> = _creationState

    // Variable para guardar la lista completa de productos
    private var fullProductList: List<Product> = listOf()

    // Carga los productos desde la API la primera vez
    fun fetchProducts() {
        _productListState.value = ProductListState.Loading
        viewModelScope.launch {
            try {
                val response = productRepository.getProducts()
                if (response.isSuccessful && response.body() != null) {
                    fullProductList = response.body()!! // Guardamos la lista completa
                    _productListState.postValue(ProductListState.Success(fullProductList))
                } else {
                    _productListState.postValue(ProductListState.Error("Error al cargar productos. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                _productListState.postValue(ProductListState.Error("Error de red: ${e.message}"))
            }
        }
    }

    // CORREGIDO: Ahora sí filtra la lista localmente
    fun searchProducts(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            fullProductList // Si la búsqueda está vacía, devuelve la lista completa
        } else {
            // Filtra la lista en memoria (rápido)
            fullProductList.filter { product ->
                product.name.contains(query, ignoreCase = true)
            }
        }
        _productListState.value = ProductListState.Success(filteredList)
    }

    fun createProduct(name: String, price: Double, stock: Int, imageUri: Uri) {
        _creationState.value = CreationState.Loading
        viewModelScope.launch {
            try {
                val response = productRepository.createProduct(name, price, stock, imageUri)
                if (response.isSuccessful) {
                    _creationState.postValue(CreationState.Success)
                } else {
                    _creationState.postValue(CreationState.Error("Error al crear el producto."))
                }
            } catch (e: Exception) {
                _creationState.postValue(CreationState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun resetCreationState() {
        _creationState.value = CreationState.Idle
    }
}