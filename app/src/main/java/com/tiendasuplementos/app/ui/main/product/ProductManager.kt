package com.tiendasuplementos.app.ui.main.product

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiendasuplementos.app.data.remote.dto.CartItemRequest
import com.tiendasuplementos.app.data.remote.dto.OrderRequest
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.data.repository.OrderRepository
import com.tiendasuplementos.app.data.repository.ProductRepository
import com.tiendasuplementos.app.util.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductManager private constructor(context: Context) {

    private val productRepository = ProductRepository(context)
    private val orderRepository = OrderRepository(context)
    private val sessionManager = SessionManager(context) // SessionManager añadido

    // --- LiveData para los estados ---

    private val _productListState = MutableLiveData<ProductListState>()
    val productListState: LiveData<ProductListState> = _productListState

    private val _creationState = MutableLiveData<UiState>(UiState.Idle)
    val creationState: LiveData<UiState> = _creationState

    private val _deletionState = MutableLiveData<UiState>(UiState.Idle)
    val deletionState: LiveData<UiState> = _deletionState

    private val _updateState = MutableLiveData<UiState>(UiState.Idle)
    val updateState: LiveData<UiState> = _updateState

    private val _orderState = MutableLiveData<UiState>(UiState.Idle)
    val orderState: LiveData<UiState> = _orderState

    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private var fullProductList: List<Product> = listOf()

    // --- Funciones de Lógica de Productos ---

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

    fun createProduct(scope: CoroutineScope, name: String, description: String, price: Double, stock: Int, imageUri: Uri) {
        _creationState.value = UiState.Loading
        scope.launch {
            try {
                val response = productRepository.createProduct(name, description, price, stock, imageUri)
                if (response.isSuccessful) {
                    _creationState.postValue(UiState.Success)
                } else {
                    _creationState.postValue(UiState.Error("Error al crear el producto."))
                }
            } catch (e: Exception) {
                _creationState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    fun updateProduct(scope: CoroutineScope, productId: Int, name: String, description: String, price: Double, stock: Int) {
        _updateState.value = UiState.Loading
        scope.launch {
            try {
                val response = productRepository.updateProduct(productId, name, description, price, stock)
                if (response.isSuccessful) {
                    _updateState.postValue(UiState.Success)
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
                    _deletionState.postValue(UiState.Success)
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

    // --- Funciones del Carrito ---

    fun addToCart(product: Product) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            _cartItems.value = currentCart.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
            }
        } else {
            currentCart.add(CartItem(product, 1))
            _cartItems.value = currentCart
        }
    }

    fun increaseCartItemQuantity(cartItem: CartItem) {
        _cartItems.value = _cartItems.value?.map {
            if (it.product.id == cartItem.product.id) it.copy(quantity = it.quantity + 1) else it
        }
    }

    fun decreaseCartItemQuantity(cartItem: CartItem) {
        if (cartItem.quantity > 1) {
            _cartItems.value = _cartItems.value?.map {
                if (it.product.id == cartItem.product.id) it.copy(quantity = it.quantity - 1) else it
            }
        } else {
            removeCartItem(cartItem)
        }
    }

    fun removeCartItem(cartItem: CartItem) {
        _cartItems.value = _cartItems.value?.filter { it.product.id != cartItem.product.id }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // --- Funciones de Órdenes ---

    fun createOrder(scope: CoroutineScope, shippingAddress: String) {
        _orderState.value = UiState.Loading
        val cart = _cartItems.value.orEmpty()
        if (cart.isEmpty()) {
            _orderState.value = UiState.Error("El carrito está vacío.")
            return
        }
        
        val userId = sessionManager.fetchUserId()
        if (userId == null) {
            _orderState.value = UiState.Error("No se pudo obtener el ID del usuario.")
            return
        }

        scope.launch {
            try {
                val cartItemsRequest = cart.map { CartItemRequest(it.product.id, it.quantity, it.product.name) }
                val totalAmount = cart.sumOf { it.product.price * it.quantity }
                val orderRequest = OrderRequest(userId, cartItemsRequest, totalAmount, shippingAddress)

                val response = orderRepository.createOrder(orderRequest)
                if (response.isSuccessful) {
                    _orderState.postValue(UiState.Success)
                } else {
                    _orderState.postValue(UiState.Error("Error al crear la orden."))
                }
            } catch (e: Exception) {
                _orderState.postValue(UiState.Error("Error de red: ${e.message}"))
            }
        }
    }

    // --- Funciones de reseteo ---

    fun resetCreationState() { _creationState.value = UiState.Idle }
    fun resetDeletionState() { _deletionState.value = UiState.Idle }
    fun resetUpdateState() { _updateState.value = UiState.Idle }
    fun resetOrderState() { _orderState.value = UiState.Idle }

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