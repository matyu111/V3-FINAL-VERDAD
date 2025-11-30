package com.tiendasuplementos.app.ui.main.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiendasuplementos.app.databinding.FragmentOrderListBinding

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderManager: OrderManager
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderManager = OrderManager()

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        // El Fragment ahora contiene la lógica de negocio
        orderAdapter = OrderAdapter(
            onAcceptClick = { order ->
                val newStatus = if (order.status == "pending") "accepted" else "shipped"
                orderManager.updateOrderStatus(lifecycleScope, order.id, newStatus)
            },
            onRejectClick = { order ->
                if (order.status == "pending") {
                    orderManager.updateOrderStatus(lifecycleScope, order.id, "rejected")
                }
            }
        )
        
        binding.ordersRecyclerView.adapter = orderAdapter
        binding.ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        orderManager.orderListState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is OrderListState.Loading) View.VISIBLE else View.GONE
            binding.ordersRecyclerView.visibility = if (state is OrderListState.Success) View.VISIBLE else View.GONE
            binding.emptyTextView.visibility = if (state is OrderListState.Success && state.orders.isEmpty()) View.VISIBLE else View.GONE

            if (state is OrderListState.Success) {
                orderAdapter.submitList(state.orders)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        orderManager.fetchOrders(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}