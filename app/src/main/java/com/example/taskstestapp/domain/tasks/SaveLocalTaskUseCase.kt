package com.example.taskstestapp.domain.tasks

import android.content.Context


class SaveLocalTaskUseCase(
    private val repository: TaskRepository
) {

    suspend operator fun invoke(userId: Int, taskId: Int, isChecked: Boolean, context: Context){
        return repository.saveLocalTask(userId = userId, taskId = taskId, isChecked = isChecked, context = context)
    }
}