package com.tiendasuplementos.app.ui.main.profile

import com.tiendasuplementos.app.data.remote.dto.User

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User) : ProfileState()
    data class Error(val message: String) : ProfileState()
    object LoggedOut : ProfileState()
    object Updated : ProfileState()
}
