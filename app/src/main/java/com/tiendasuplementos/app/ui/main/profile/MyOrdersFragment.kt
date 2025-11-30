package com.tiendasuplementos.app.ui.main.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tiendasuplementos.app.databinding.FragmentMyOrdersBinding
import com.tiendasuplementos.app.ui.state.OrderListState
import com.tiendasuplementos.app.ui.main.product.OrderManager

class MyOrdersFragment : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderManager: OrderManager
    private lateinit var myOrdersAdapter: MyOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderManager = OrderManager.getInstance(requireContext())

        setupRecyclerView()
        setupObservers()
        setupSwipeRefresh()

        orderManager.getMyOrders(lifecycleScope)
    }

    private fun setupRecyclerView() {
        myOrdersAdapter = MyOrdersAdapter(emptyList())
        binding.recyclerViewMyOrders.adapter = myOrdersAdapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayoutMyOrders.setOnRefreshListener {
            orderManager.getMyOrders(lifecycleScope)
        }
    }

    private fun setupObservers() {
        orderManager.orderListState.observe(viewLifecycleOwner) { state ->
            binding.swipeRefreshLayoutMyOrders.isRefreshing = false

            binding.progressBarMyOrders.isVisible = state is OrderListState.Loading && !binding.swipeRefreshLayoutMyOrders.isRefreshing
            binding.textViewErrorMyOrders.isVisible = state is OrderListState.Error

            if (state is OrderListState.Success) {
                binding.recyclerViewMyOrders.isVisible = true
                myOrdersAdapter.updateOrders(state.orders)
            } else {
                if (state !is OrderListState.Loading) {
                    binding.recyclerViewMyOrders.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
