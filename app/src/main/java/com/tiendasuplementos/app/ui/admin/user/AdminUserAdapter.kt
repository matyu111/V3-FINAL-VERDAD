package com.tiendasuplementos.app.ui.admin.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.databinding.ItemAdminUserBinding

class AdminUserAdapter(
    private var users: List<User>,
    private val onUserStatusChanged: (User, Boolean) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.AdminUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserViewHolder {
        val binding = ItemAdminUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminUserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    inner class AdminUserViewHolder(private val binding: ItemAdminUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.textViewUserNameAdmin.text = user.name
            binding.textViewUserEmailAdmin.text = user.email

            // Configurar el Switch sin disparar el listener
            binding.switchUserStatus.setOnCheckedChangeListener(null)
            binding.switchUserStatus.isChecked = user.status == "blocked"

            // Volver a asignar el listener
            binding.switchUserStatus.setOnCheckedChangeListener { _, isChecked ->
                onUserStatusChanged(user, isChecked)
            }
        }
    }
}