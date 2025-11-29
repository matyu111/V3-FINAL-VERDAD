package com.tiendasuplementos.app.ui.main.order

import com.tiendasuplementos.app.data.remote.dto.Order

sealed class OrderListState {
    object Loading : OrderListState()
    data class Success(val orders: List<Order>) : OrderListState()
    data class Error(val message: String) : OrderListState()
}
