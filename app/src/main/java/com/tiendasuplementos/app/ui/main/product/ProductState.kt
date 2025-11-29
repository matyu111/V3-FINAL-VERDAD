package com.tiendasuplementos.app.ui.main.product

import com.tiendasuplementos.app.data.remote.dto.Product

// --- Sealed Classes para los estados de la UI ---

sealed class ProductListState {
    object Loading : ProductListState()
    data class Success(val products: List<Product>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}

// Estado unificado para acciones simples (Crear, Actualizar, Borrar, Ordenar)
sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
    object Idle : UiState()
}

// --- Modelo de datos para el Carrito ---

data class CartItem(val product: Product, var quantity: Int)
