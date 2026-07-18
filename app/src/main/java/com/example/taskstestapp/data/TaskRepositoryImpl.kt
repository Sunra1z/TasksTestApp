package com.example.taskstestapp.data

import android.content.Context
import com.example.taskstestapp.domain.tasks.Task
import com.example.taskstestapp.domain.tasks.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

object TaskRepositoryImpl : TaskRepository {

    private val api = RetrofitInstance.api

    override suspend fun getTasksByUserId(context: Context, userId: Int): Result<List<Task>> = withContext(Dispatchers.IO) {
        try {
            val serverTasks = api.getTasksByUserId(userId)
            val dao = DatabaseClient.getDao(context)

            val localTasks = dao.getLocalStatusesForUser(userId)

            val overrideMap = localTasks.associateBy { it.taskId }

            val mergedList = serverTasks.map { task ->
                val localOverride = overrideMap[task.id]
                Task(
                    userId = task.userId,
                    id = task.id,
                    title = task.title,
                    completed = task.completed,
                    completedLocal = localOverride?.completedLocal ?: task.completed
                )
            }

            Result.success(mergedList)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun saveLocalTask(userId: Int, taskId: Int, isChecked: Boolean, context: Context) = withContext(Dispatchers.IO) {
        val dao = DatabaseClient.getDao(context = context)
        dao.saveLocalStatus(
            LocalTask(
                taskId = taskId,
                userId = userId,
                isLocallyModified = true,
                completedLocal = isChecked
            )
        )
    }
}