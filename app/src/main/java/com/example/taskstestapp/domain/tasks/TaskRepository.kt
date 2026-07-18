package com.example.taskstestapp.domain.tasks

import android.content.Context

interface TaskRepository {
    suspend fun getTasksByUserId(context: Context, userId: Int) : Result<List<Task>>
    suspend fun saveLocalTask(userId: Int, taskId: Int, isChecked: Boolean, context: Context)
}