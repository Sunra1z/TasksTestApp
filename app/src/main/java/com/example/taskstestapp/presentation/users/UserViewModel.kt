package com.example.taskstestapp.presentation.users

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskstestapp.data.UserRepositoryImpl
import com.example.taskstestapp.domain.users.GetUserListUseCase
import com.example.taskstestapp.domain.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    init {
        observeSearchQuery()
        fetchUserList()
    }

    private val repository =
        UserRepositoryImpl // Should use userRepository, but rn without DI, would be like that

    private val getUserListUseCase = GetUserListUseCase(repository)

    private val _uiState = MutableStateFlow(UserScreenState())
    val uiState: StateFlow<UserScreenState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _usersCache = MutableStateFlow<List<User>>(emptyList())

    private fun observeSearchQuery(){
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    filterUserList(query)
                }
        }
    }

    fun fetchUserList(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isError = false
                )
            }
            getUserListUseCase()
                .onSuccess { users ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userList = users,
                        )
                    }
                    _usersCache.value = users
                    Log.d("Viewmodel", "Got list: ${users}")
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                    Log.d("Viewmodel", "error occured: ${exception}")
                }

        }
    }

    private fun filterUserList(query: String){
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }
        val filtered = if (query.isBlank()){
            _usersCache.value
        } else {
            _usersCache.value.filter {
                it.username.contains(query, ignoreCase = true) ||
                it.email.contains(query, ignoreCase = true) ||
                it.name.contains(query, ignoreCase = true)
            }
        }
        _uiState.update {
            it.copy(
                isLoading = false,
                userList = filtered
            )
        }
    }

    fun onQueryChanged(newText: String) {
        _searchQuery.value = newText
    }


    data class UserScreenState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val userList: List<User> = emptyList(),
    )
}