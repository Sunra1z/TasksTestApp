package com.example.taskstestapp.domain.tasks

data class TaskUiItem(
    val task: Task,
    val isCompletedLocally: Boolean = task.completed
)