package com.tiendasuplementos.app.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.databinding.ItemOrderHistoryBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class OrderHistoryAdapter : ListAdapter<Order, OrderHistoryAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(private val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        fun bind(order: Order) {
            binding.orderIdTextView.text = "Pedido #${order.id}"
            binding.orderTotalTextView.text = "Total: ${currencyFormat.format(order.totalAmount)}"
            binding.orderStatusTextView.text = order.status.replaceFirstChar { it.uppercase() }

            // Corregido: Se usa el nombre de campo correcto (`createdAt`)
            val date = Date(order.createdAt)
            binding.orderDateTextView.text = "Fecha: ${dateFormat.format(date)}"

            // Cambiar el color del estado
            val statusColor = when (order.status.lowercase()) {
                "completed", "shipped", "aceptado" -> R.color.fitness_success
                "pending" -> R.color.fitness_primary_variant
                "cancelled", "rejected", "rechazado" -> R.color.fitness_error
                else -> R.color.fitness_surface
            }
            binding.orderStatusTextView.setBackgroundColor(ContextCompat.getColor(itemView.context, statusColor))
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}