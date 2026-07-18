package com.example.taskstestapp.presentation.tasks

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskstestapp.data.TaskRepositoryImpl
import com.example.taskstestapp.domain.tasks.GetTaskListByUserIdUseCase
import com.example.taskstestapp.domain.tasks.SaveLocalTaskUseCase
import com.example.taskstestapp.domain.tasks.Task
import com.example.taskstestapp.domain.tasks.TaskUiItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {


    private val repository = TaskRepositoryImpl // Should use userRepository, but rn without DI, would be like that

    private val getTaskListByUserIdUseCase = GetTaskListByUserIdUseCase(repository)
    private val saveLocalTaskUseCase = SaveLocalTaskUseCase(repository)

    private val _uiState = MutableStateFlow(TaskListScreenState())
    val uiState : StateFlow<TaskListScreenState> = _uiState.asStateFlow()

    fun fetchTaskList(context: Context, userId: Int, filter: TaskStatusFilter = TaskStatusFilter.ALL){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isError = false
                )
            }
            Log.d("Viewmodel", "Fetch start")
            getTaskListByUserIdUseCase(userId = userId, context = context)
                .onSuccess { tasks ->
                    val filteredTasks = when (filter) {
                        TaskStatusFilter.ALL -> tasks
                        TaskStatusFilter.IN_WORK -> tasks.filter { !it.completedLocal }
                        TaskStatusFilter.COMPLETED -> tasks.filter { it.completedLocal }
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = false,
                            taskList = filteredTasks
                        )
                    }
                    Log.d("Viewmodel", "On success: ${tasks}")
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                        )
                    }
                    Log.d("Viewmodel", "On failure: ${exception}")
                }
        }
    }

    fun saveLocalTask(userId: Int, taskId: Int, isChecked: Boolean, context: Context){
        viewModelScope.launch {
            try {
                saveLocalTaskUseCase(userId = userId, taskId = taskId, isChecked = isChecked, context)
                Log.d("SaveTask", "Success")
            } catch (e: Exception){
                Log.d("SaveTask", "Error ${e}")
            }
        }
    }

    data class TaskListScreenState(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val taskList: List<Task> = emptyList(),
    )

}