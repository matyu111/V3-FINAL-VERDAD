package com.tiendasuplementos.app.ui.admin.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.databinding.ItemAdminOrderBinding
import java.text.SimpleDateFormat
import java.util.*

class AdminOrderAdapter(
    private var orders: List<Order>,
    private val onAccept: (Order) -> Unit,
    private val onReject: (Order) -> Unit
) : RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminOrderViewHolder {
        val binding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminOrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class AdminOrderViewHolder(private val binding: ItemAdminOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

        fun bind(order: Order) {
            binding.textViewOrderId.text = "Orden #${order.id}"
            binding.textViewOrderDate.text = dateFormat.format(Date(order.createdAt))
            binding.textViewOrderTotal.text = "Total: $${String.format("%.2f", order.totalAmount)}"
            binding.textViewOrderStatus.text = order.status

            // Mostrar botones solo si la orden está pendiente
            if (order.status.equals("pendiente", ignoreCase = true)) {
                binding.layoutOrderActions.visibility = View.VISIBLE
            } else {
                binding.layoutOrderActions.visibility = View.GONE
            }

            binding.buttonAcceptOrder.setOnClickListener {
                onAccept(order)
            }
            binding.buttonRejectOrder.setOnClickListener {
                onReject(order)
            }
        }
    }
}