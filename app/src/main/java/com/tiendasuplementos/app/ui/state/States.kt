package com.tiendasuplementos.app.ui.state

import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.data.remote.dto.Product

// Generic UI State
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// Specific state for the product list
sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}

// Specific state for the order list
sealed class OrderListState {
    object Loading : OrderListState()
    data class Success(val orders: List<Order>) : OrderListState()
    data class Error(val message: String) : OrderListState()
}
