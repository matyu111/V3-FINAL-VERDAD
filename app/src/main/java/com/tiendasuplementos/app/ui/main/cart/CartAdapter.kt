package com.tiendasuplementos.app.ui.main.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.ItemCartBinding
import com.tiendasuplementos.app.ui.main.product.CartItem

class CartAdapter(
    private val onIncrease: (CartItem) -> Unit,
    private val onDecrease: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding, onIncrease, onDecrease, onRemove)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartViewHolder(
        private val binding: ItemCartBinding,
        private val onIncrease: (CartItem) -> Unit,
        private val onDecrease: (CartItem) -> Unit,
        private val onRemove: (CartItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.productNameTextView.text = cartItem.product.name
            binding.quantityTextView.text = cartItem.quantity.toString()

            cartItem.product.image?.url?.let { 
                binding.productImageView.load(it)
            } ?: binding.productImageView.setImageResource(R.drawable.ic_launcher_background)

            binding.buttonIncreaseQuantity.setOnClickListener { onIncrease(cartItem) }
            binding.buttonDecreaseQuantity.setOnClickListener { onDecrease(cartItem) }
            binding.buttonRemoveItem.setOnClickListener { onRemove(cartItem) }
        }
    }

    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}