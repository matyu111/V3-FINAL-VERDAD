package com.tiendasuplementos.app.ui.main.user

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.databinding.FragmentUserListBinding

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private lateinit var userManager: UserManager
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userManager = UserManager(requireContext())

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        userAdapter = UserAdapter { user ->
            userManager.toggleUserStatus(lifecycleScope, user)
        }
        binding.usersRecyclerView.adapter = userAdapter
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        userManager.userListState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.visibility = if (state is UserListState.Loading) View.VISIBLE else View.GONE
            binding.usersRecyclerView.visibility = if (state is UserListState.Success) View.VISIBLE else View.GONE
            binding.emptyTextView.visibility = if (state is UserListState.Success && state.users.isEmpty()) View.VISIBLE else View.GONE

            if (state is UserListState.Success) {
                userAdapter.submitList(state.users)
            }
        }

        userManager.userStatusState.observe(viewLifecycleOwner) { state ->
            if (state is UserStatusState.Error) {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Se llama aquí para asegurar que la lista se actualice cada vez que la pestaña es visible.
        userManager.fetchUsers(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Buscar por nombre..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                userManager.fetchUsers(lifecycleScope, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    userManager.fetchUsers(lifecycleScope)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}