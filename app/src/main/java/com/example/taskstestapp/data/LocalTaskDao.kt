package com.example.taskstestapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface LocalTaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocalStatus(localTask: LocalTask)

    @Query("SELECT * FROM local_tasks WHERE userId =:userId")
    suspend fun getLocalStatusesForUser(userId: Int): List<LocalTask>
}