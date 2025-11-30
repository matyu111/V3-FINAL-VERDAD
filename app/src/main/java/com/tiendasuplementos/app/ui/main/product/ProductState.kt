package com.tiendasuplementos.app.ui.main.product

import com.tiendasuplementos.app.data.remote.dto.Product

// CartItem model
data class CartItem(val product: Product, var quantity: Int)
