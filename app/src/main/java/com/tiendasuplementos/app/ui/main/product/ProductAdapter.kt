package com.tiendasuplementos.app.ui.main.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.databinding.ItemProductBinding

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClicked: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private val BASE_IMAGE_URL = "https://x8ki-letl-twmt.n7.xano.io"

        fun bind(product: Product) {
            binding.textViewProductName.text = product.name
            binding.textViewProductPrice.text = "$${product.price}"

            // Cargar la imagen del producto, si existe.
            val imageUrl = product.image?.url
            if (imageUrl != null) {
                val fullImageUrl = if (imageUrl.startsWith("http")) imageUrl else BASE_IMAGE_URL + imageUrl
                binding.imageViewProduct.load(fullImageUrl) {
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }
            } else {
                binding.imageViewProduct.load(R.drawable.ic_placeholder)
            }

            binding.root.setOnClickListener {
                onProductClicked(product)
            }
        }
    }
}