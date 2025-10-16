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

sealed class DeletionState {
    object Loading : DeletionState()
    // Ahora Success devolverá el ID del producto borrado
    data class Success(val productId: Int) : DeletionState()
    data class Error(val message: String) : DeletionState()
    object Idle : DeletionState()
}

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = ProductRepository(application.applicationContext)

    private val _productListState = MutableLiveData<ProductListState>()
    val productListState: LiveData<ProductListState> = _productListState

    private val _creationState = MutableLiveData<CreationState>(CreationState.Idle)
    val creationState: LiveData<CreationState> = _creationState

    private val _deletionState = MutableLiveData<DeletionState>(DeletionState.Idle)
    val deletionState: LiveData<DeletionState> = _deletionState

    private var fullProductList: List<Product> = listOf()

    fun fetchProducts() {
        _productListState.value = ProductListState.Loading
        viewModelScope.launch {
            try {
                val response = productRepository.getProducts()
                if (response.isSuccessful && response.body() != null) {
                    fullProductList = response.body()!!
                    _productListState.postValue(ProductListState.Success(fullProductList))
                } else {
                    _productListState.postValue(ProductListState.Error("Error al cargar productos. Código: ${response.code()}"))
                }
            } catch (e: Exception) {
                _productListState.postValue(ProductListState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun searchProducts(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            fullProductList
        } else {
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

    fun deleteProduct(productId: Int) {
        _deletionState.value = DeletionState.Loading
        viewModelScope.launch {
            try {
                val response = productRepository.deleteProduct(productId)
                if (response.isSuccessful) {
                    // En caso de éxito, actualizamos la lista localmente
                    removeProductFromLocalList(productId)
                    _deletionState.postValue(DeletionState.Success(productId))
                } else {
                    _deletionState.postValue(DeletionState.Error("Error al borrar el producto."))
                }
            } catch (e: Exception) {
                _deletionState.postValue(DeletionState.Error("Error de red: ${e.message}"))
            }
        }
    }

    // Nueva función para eliminar el producto de la lista en memoria
    private fun removeProductFromLocalList(productId: Int) {
        fullProductList = fullProductList.filter { it.id != productId }
        _productListState.value = ProductListState.Success(fullProductList)
    }

    fun resetCreationState() {
        _creationState.value = CreationState.Idle
    }

    fun resetDeletionState() {
        _deletionState.value = DeletionState.Idle
    }
}