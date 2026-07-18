package com.example.taskstestapp.presentation.users

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskstestapp.data.UserRepositoryImpl
import com.example.taskstestapp.domain.users.GetUserByIdUseCase
import com.example.taskstestapp.domain.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailsViewModel : ViewModel() {


    private val repository =
        UserRepositoryImpl // Should use userRepository, but rn without DI, would be like that

    private val getUserByIdUseCase = GetUserByIdUseCase(repository)

    private val _uiState = MutableStateFlow(UserScreenState())
    val uiState: StateFlow<UserScreenState> = _uiState.asStateFlow()



    fun fetchUser(userId: Int){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isError = false,
                    isLoading = true
                )
            }
            getUserByIdUseCase(userId)
                .onSuccess { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            user = user
                        )
                    }
                }
                .onFailure { user ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
        }
    }



    data class UserScreenState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val userId: Int = 0,
        val user: User? = null,
    )

}