package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.model.ExampleModel
import com.example.app.data.repository.ExampleRepository
import com.example.app.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val repository: ExampleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ExampleModel>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<ExampleModel>>> = _uiState.asStateFlow()

    fun fetchExamples() {
        viewModelScope.launch {
            repository.getExamples().collect { state ->
                _uiState.value = state
            }
        }
    }
}
