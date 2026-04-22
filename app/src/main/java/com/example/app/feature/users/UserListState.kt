package com.example.app.feature.users

import com.example.app.domain.model.User

sealed class UserListState {
    object Loading : UserListState()
    data class Success(val users: List<User>) : UserListState()
    data class Error(val message: String) : UserListState()
}
