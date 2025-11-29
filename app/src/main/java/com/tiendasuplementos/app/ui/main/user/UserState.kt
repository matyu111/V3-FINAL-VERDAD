package com.tiendasuplementos.app.ui.main.user

import com.tiendasuplementos.app.data.remote.dto.User

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}

sealed class UserStatusState {
    object Loading : UserStatusState()
    data class Success(val userId: Int) : UserStatusState()
    data class Error(val message: String) : UserStatusState()
    object Idle : UserStatusState()
}
