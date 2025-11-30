package com.tiendasuplementos.app.ui.main.product

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.data.repository.ProductRepository
import com.tiendasuplementos.app.ui.state.ProductListState
import com.tiendasuplementos.app.ui.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductManager private constructor(context: Context) {

    private val productRepository = ProductRepository(context)

    // --- State for Products List ---
    private val _productListState = MutableLiveData<ProductListState>()
    val productListState: LiveData<ProductListState> = _productListState

    // --- State for Product CRUD Operations ---
    private val _creationState = MutableLiveData<UiState<Product>>()
    val creationState: LiveData<UiState<Product>> = _creationState

    private val _deletionState = MutableLiveData<UiState<Unit>>()
    val deletionState: LiveData<UiState<Unit>> = _deletionState

    private val _updateState = MutableLiveData<UiState<Product>>()
    val updateState: LiveData<UiState<Product>> = _updateState

    // --- Local Cache ---
    private var fullProductList: List<Product> = listOf()

    // --- Cart State ---
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // --- Order State (for checkout) ---
    private val _orderState = MutableLiveData<UiState<Unit>>()
    val orderState: LiveData<UiState<Unit>> = _orderState


    // ==============================
    // Product List Operations
    // ==============================

    fun fetchProducts(scope: CoroutineScope) {
        _productListState.value = ProductListState.Loading
        scope.launch {
            try {
                val response = productRepository.getProducts()
                if (response.isSuccessful && response.body() != null) {
                    fullProductList = response.body()!!
                    _productListState.postValue(ProductListState.Success(fullProductList))
                } else {
                    _productListState.postValue(ProductListState.Error("Error al cargar productos."))
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
            fullProductList.filter { it.name.contains(query, ignoreCase = true) }
        }
        _productListState.value = ProductListState.Success(filteredList)
    }

    // ==============================
    // Product CRUD Operations (Admin)
    // ==============================

    fun createProduct(scope: CoroutineScope, name: String, description: String, price: Double, stock: Int, imageUris: List<Uri>) {
        _creationState.value = UiState.Loading
        scope.launch {
            try {
                val response = productRepository.createProduct(name, description, price, stock, imageUris)
                if (response.isSuccessful) {
                    _creationState.postValue(UiState.Success(response.body()!!))
                    // Optionally refresh list
                    fetchProducts(this)
                } else {
                    _creationState.postValue(UiState.Error("Error al crear el producto."))
                }
            } catch (e: Exception) {
                _creationState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateProduct(scope: CoroutineScope, productId: Int, name: String, description: String, price: Double, stock: Int, imageUris: List<Uri>) {
        _updateState.value = UiState.Loading
        scope.launch {
            try {
                val response = productRepository.updateProduct(productId, name, description, price, stock, imageUris)
                if (response.isSuccessful) {
                    _updateState.postValue(UiState.Success(response.body()!!))
                    fetchProducts(this)
                } else {
                    _updateState.postValue(UiState.Error("Error al actualizar el producto."))
                }
            } catch (e: Exception) {
                _updateState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun deleteProduct(scope: CoroutineScope, productId: Int) {
        _deletionState.value = UiState.Loading
        scope.launch {
            try {
                val response = productRepository.deleteProduct(productId)
                if (response.isSuccessful) {
                    removeProductFromLocalList(productId)
                    _deletionState.postValue(UiState.Success(Unit))
                } else {
                    _deletionState.postValue(UiState.Error("Error al borrar el producto."))
                }
            } catch (e: Exception) {
                _deletionState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    private fun removeProductFromLocalList(productId: Int) {
        fullProductList = fullProductList.filter { it.id != productId }
        _productListState.value = ProductListState.Success(fullProductList)
    }

    fun resetCreationState() { _creationState.value = UiState.Idle }
    fun resetDeletionState() { _deletionState.value = UiState.Idle }
    fun resetUpdateState() { _updateState.value = UiState.Idle }


    // ==============================
    // Cart Operations
    // ==============================

    fun addToCart(product: Product) {
        val currentList = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentList.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentList.add(CartItem(product, 1))
        }
        _cartItems.value = currentList
    }

    fun increaseCartItemQuantity(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: return
        val item = currentList.find { it.product.id == cartItem.product.id }
        item?.let {
            it.quantity++
            _cartItems.value = currentList
        }
    }

    fun decreaseCartItemQuantity(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: return
        val item = currentList.find { it.product.id == cartItem.product.id }
        item?.let {
            if (it.quantity > 1) {
                it.quantity--
            } else {
                currentList.remove(it)
            }
            _cartItems.value = currentList
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        val currentList = _cartItems.value?.toMutableList() ?: return
        currentList.removeIf { it.product.id == cartItem.product.id }
        _cartItems.value = currentList
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // ==============================
    // Order / Checkout Operations
    // ==============================

    fun createOrder(scope: CoroutineScope, shippingAddress: String) {
        _orderState.value = UiState.Loading
        scope.launch {
            try {
                // Por simplicidad, asumimos que el backend maneja la creación de la orden basada en items
                // enviados o que simplemente simula el checkout.
                // Si necesitaras enviar los items del carrito, tendrías que mapearlos a un DTO.
                // val orderRequest = CreateOrderRequest(items = _cartItems.value!!.map { ... }, address = shippingAddress)
                
                // Ejemplo ficticio llamando a un repositorio (que deberías tener implementado si existe el endpoint real)
                // val response = orderRepository.createOrder(orderRequest)
                
                // Simulamos éxito por ahora o llamamos a una función existente si la tienes en ProductRepository
                // Si no tienes el endpoint implementado, simulamos un delay y éxito:
                 kotlinx.coroutines.delay(1000) 
                 _orderState.postValue(UiState.Success(Unit))

            } catch (e: Exception) {
                _orderState.postValue(UiState.Error("Error al crear la orden: ${e.message}"))
            }
        }
    }

    fun resetOrderState() {
        _orderState.value = UiState.Idle
    }

    companion object {
        @Volatile
        private var INSTANCE: ProductManager? = null

        fun getInstance(context: Context): ProductManager {
            return INSTANCE ?: synchronized(this) {
                val instance = ProductManager(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
