package com.example.taskstestapp.data

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    @Volatile
    private var instance: TaskDatabase? = null

    fun getDao(context: Context): LocalTaskDao {
        return instance?.taskDao() ?: synchronized(this) {
            val currentInstance = instance
            if (currentInstance != null){
                currentInstance.taskDao()
            } else {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()
                instance = newInstance
                newInstance.taskDao()
            }
        }
    }

}