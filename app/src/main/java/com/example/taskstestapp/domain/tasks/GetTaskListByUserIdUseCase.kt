package com.example.taskstestapp.domain.tasks

import android.content.Context

class GetTaskListByUserIdUseCase(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(userId: Int, context: Context) : Result<List<Task>>{
        return repository.getTasksByUserId(userId = userId, context = context)
    }
}