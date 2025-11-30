package com.tiendasuplementos.app.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.data.remote.dto.Order
import com.tiendasuplementos.app.databinding.ItemMyOrderBinding

class MyOrdersAdapter(private var orders: List<Order>) : RecyclerView.Adapter<MyOrdersAdapter.MyOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder {
        val binding = ItemMyOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyOrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int = orders.size

    fun updateOrders(newOrders: List<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    inner class MyOrderViewHolder(private val binding: ItemMyOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.textViewOrderId.text = "Pedido #${order.id}"
            binding.textViewOrderStatus.text = "Estado: ${order.status}"
            binding.textViewOrderTotal.text = "Total: $${order.totalAmount}"
        }
    }
}
