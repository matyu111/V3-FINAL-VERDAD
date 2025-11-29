package com.tiendasuplementos.app.ui.main.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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

class ProductAdapter(
    private val userRole: String?,
    private val onActionClick: (product: Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), userRole, onActionClick)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.productNameTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.productDescriptionTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val stockTextView: TextView = itemView.findViewById(R.id.productStockTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.productImageView)
        private val idTextView: TextView = itemView.findViewById(R.id.productIdTextView)
        private val addToCartButton: ImageButton = itemView.findViewById(R.id.buttonAddToCart)
        private val editButton: ImageButton = itemView.findViewById(R.id.buttonEditProduct)

        fun bind(product: Product, userRole: String?, onActionClick: (Product) -> Unit) {
            nameTextView.text = product.name
            descriptionTextView.text = product.description ?: ""

            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            priceTextView.text = format.format(product.price)

            stockTextView.text = "Stock: ${product.stock} unidades"
            idTextView.text = "ID: ${product.id}"

            product.image?.url?.let {
                imageView.load(it)
            }

            // Lógica de visibilidad y acción de botones según el rol
            if (userRole == "admin") {
                addToCartButton.visibility = View.GONE
                editButton.visibility = View.VISIBLE
                editButton.setOnClickListener { onActionClick(product) }
            } else { // Cliente
                addToCartButton.visibility = View.VISIBLE
                editButton.visibility = View.GONE
                addToCartButton.setOnClickListener { onActionClick(product) }
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}