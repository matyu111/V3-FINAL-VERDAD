package com.tiendasuplementos.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentAdminDashboardBinding

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonManageProducts.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_adminProductListFragment)
        }

        binding.buttonManageUsers.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_adminUserListFragment)
        }

        binding.buttonReviewOrders.setOnClickListener {
            findNavController().navigate(R.id.action_adminDashboardFragment_to_adminOrderListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}