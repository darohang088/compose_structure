package com.example.app.feature.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.utils.Resource
import com.example.app.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<UserListState>(UserListState.Loading)
    val state: StateFlow<UserListState> = _state.asStateFlow()

    init {
        getUsers()
    }

    private fun getUsers() {
        getUsersUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = UserListState.Success(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = UserListState.Error(result.message ?: "An unexpected error occurred")
                }
                is Resource.Loading -> {
                    _state.value = UserListState.Loading
                }
            }
        }.launchIn(viewModelScope)
    }
}
