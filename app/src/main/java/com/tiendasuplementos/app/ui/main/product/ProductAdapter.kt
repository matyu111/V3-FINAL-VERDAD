package com.tiendasuplementos.app.ui.main.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView) // Nuevo TextView
        private val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val stockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        private val idTextView: TextView = itemView.findViewById(R.id.productIdTextView)

        fun bind(product: Product) {
            nameTextView.text = product.name
            descriptionTextView.text = product.description // Mostramos la descripción

            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            priceTextView.text = format.format(product.price)

            stockTextView.text = "Stock: ${product.stock} unidades"
            idTextView.text = "ID: ${product.id}"

            product.image?.url?.let {
                imageView.load(it) {
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_foreground)
                }
            } ?: imageView.setImageDrawable(null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}