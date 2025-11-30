package com.tiendasuplementos.app.ui.main.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiendasuplementos.app.data.remote.dto.CartItemRequest
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.databinding.ItemOrderBinding
import java.text.NumberFormat
import java.util.Locale

class OrderAdapter(
    private val onAcceptClick: (Order) -> Unit,
    private val onRejectClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding, onAcceptClick, onRejectClick)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(
        private val binding: ItemOrderBinding,
        private val onAcceptClick: (Order) -> Unit,
        private val onRejectClick: (Order) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.orderIdTextView.text = "Orden #${order.id}"
            binding.orderStatusTextView.text = "Estado: ${order.status.replaceFirstChar { it.uppercase() }}"

            val format = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))
            binding.orderTotalTextView.text = "Total: ${format.format(order.totalAmount)}"

            try {
                val type = object : TypeToken<List<CartItemRequest>>() {}.type
                val cartItems: List<CartItemRequest> = Gson().fromJson(order.cartItems, type)
                binding.orderItemsTextView.text = cartItems.joinToString("\n") { "- ${it.productName} (x${it.quantity})" }
            } catch (e: Exception) {
                binding.orderItemsTextView.text = "Error al parsear los ítems."
            }

            binding.buttonAcceptOrder.setOnClickListener { onAcceptClick(order) }
            binding.buttonRejectOrder.setOnClickListener { onRejectClick(order) }

            // Lógica de visibilidad de la UI, pero no de negocio
            when (order.status) {
                "pending" -> {
                    binding.adminActionsLayout.visibility = View.VISIBLE
                    binding.buttonRejectOrder.visibility = View.VISIBLE
                    binding.buttonAcceptOrder.text = "Aceptar"
                }
                "accepted" -> {
                    binding.adminActionsLayout.visibility = View.VISIBLE
                    binding.buttonRejectOrder.visibility = View.GONE
                    binding.buttonAcceptOrder.text = "Marcar como Enviado"
                }
                else -> {
                    binding.adminActionsLayout.visibility = View.GONE
                }
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
    }
}