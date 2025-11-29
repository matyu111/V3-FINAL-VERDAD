package com.tiendasuplementos.app.ui.main.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tiendasuplementos.app.R
import com.tiendasuplementos.app.data.remote.dto.User
import com.tiendasuplementos.app.databinding.ItemUserBinding

class UserAdapter(private val onToggleClick: (User) -> Unit) : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onToggleClick)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(
        private val binding: ItemUserBinding,
        private val onToggleClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.userNameTextView.text = user.name
            binding.userEmailTextView.text = user.email

            // Corregido: La comparación ahora ignora mayúsculas/minúsculas para ser más robusta.
            if (user.status.equals("active", ignoreCase = true)) {
                binding.buttonToggleStatus.text = "Bloquear"
                binding.buttonToggleStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.fitness_error))
            } else {
                binding.buttonToggleStatus.text = "Desbloquear"
                binding.buttonToggleStatus.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.fitness_success))
            }

            binding.buttonToggleStatus.setOnClickListener { onToggleClick(user) }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}