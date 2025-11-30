package com.tiendasuplementos.app.ui.admin.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentAdminOrderListBinding
import com.tiendasuplementos.app.ui.state.OrderListState
import com.tiendasuplementos.app.ui.main.product.OrderManager
import com.tiendasuplementos.app.ui.state.UiState

class AdminOrderListFragment : Fragment() {

    private var _binding: FragmentAdminOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderManager: OrderManager
    private lateinit var adminOrderAdapter: AdminOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderManager = OrderManager.getInstance(requireContext())

        setupRecyclerView()
        setupObservers()

        orderManager.fetchOrders(lifecycleScope)
    }

    private fun setupRecyclerView() {
        adminOrderAdapter = AdminOrderAdapter(
            orders = emptyList(),
            onAccept = { order ->
                orderManager.updateOrderStatus(lifecycleScope, order.id, "enviado")
            },
            onReject = { order ->
                orderManager.updateOrderStatus(lifecycleScope, order.id, "rechazado")
            }
        )
        binding.recyclerViewOrdersAdmin.adapter = adminOrderAdapter
    }

    private fun setupObservers() {
        orderManager.orderListState.observe(viewLifecycleOwner) { state ->
            binding.progressBarOrdersAdmin.isVisible = state is OrderListState.Loading
            binding.textViewErrorOrdersAdmin.isVisible = state is OrderListState.Error

            if (state is OrderListState.Success) {
                binding.recyclerViewOrdersAdmin.isVisible = true
                adminOrderAdapter.updateOrders(state.orders)
            } else {
                binding.recyclerViewOrdersAdmin.isVisible = false
            }
        }

        orderManager.orderUpdateState.observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success<*>) {
                Toast.makeText(requireContext(), "Estado de la orden actualizado", Toast.LENGTH_SHORT).show()
                orderManager.resetOrderUpdateState()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
