package com.tiendasuplementos.app.ui.admin.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.Product
import com.tiendasuplementos.app.databinding.ItemAdminProductBinding

class AdminProductAdapter(
    private var products: List<Product>,
    private val onEditClicked: (Product) -> Unit,
    private val onDeleteClicked: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminProductViewHolder {
        val binding = ItemAdminProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    inner class AdminProductViewHolder(private val binding: ItemAdminProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private val BASE_IMAGE_URL = "https://x8ki-letl-twmt.n7.xano.io"

        fun bind(product: Product) {
            binding.textViewProductNameAdmin.text = product.name
            binding.textViewProductStockAdmin.text = "Stock: ${product.stock}"

            val imageUrl = product.image?.url
            if (imageUrl != null) {
                val fullImageUrl = if (imageUrl.startsWith("http")) imageUrl else BASE_IMAGE_URL + imageUrl
                binding.imageViewProductAdmin.load(fullImageUrl) {
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                }
            } else {
                binding.imageViewProductAdmin.load(R.drawable.ic_placeholder)
            }

            // Listeners
            binding.buttonEditProductAdmin.setOnClickListener {
                onEditClicked(product)
            }
            binding.buttonDeleteProductAdmin.setOnClickListener {
                onDeleteClicked(product)
            }
        }
    }
}