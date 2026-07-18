package com.example.taskstestapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_tasks")
data class LocalTask(
    @PrimaryKey val taskId: Int,
    val userId: Int,
    val completedLocal: Boolean,
    val isLocallyModified: Boolean = false,
)
