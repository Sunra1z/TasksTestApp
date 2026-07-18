package com.example.taskstestapp.domain.tasks

data class Task(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean,
    val completedLocal: Boolean = false
)