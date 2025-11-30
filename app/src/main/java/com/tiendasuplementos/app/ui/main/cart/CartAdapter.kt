package com.tiendasuplementos.app.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.ItemCartBinding
import com.tiendasuplementos.app.ui.main.product.CartItem

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged() // For simplicity. DiffUtil would be better for performance.
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        private val BASE_IMAGE_URL = "https://x8ki-letl-twmt.n7.xano.io"

        fun bind(cartItem: CartItem) {
            val product = cartItem.product
            binding.textViewProductNameCart.text = product.name
            binding.textViewProductPriceCart.text = "$${product.price}"
            binding.textViewQuantity.text = cartItem.quantity.toString()

            val imageUrl = product.image?.url
            if (imageUrl != null) {
                val fullImageUrl = if (imageUrl.startsWith("http")) imageUrl else BASE_IMAGE_URL + imageUrl
                binding.imageViewProductCart.load(fullImageUrl) {
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }
            } else {
                binding.imageViewProductCart.load(R.drawable.ic_placeholder)
            }

            // Listeners
            binding.buttonIncreaseQuantity.setOnClickListener {
                onIncrease(cartItem)
            }
            binding.buttonDecreaseQuantity.setOnClickListener {
                onDecrease(cartItem)
            }
            binding.buttonRemoveItem.setOnClickListener {
                onRemove(cartItem)
            }
        }
    }
}