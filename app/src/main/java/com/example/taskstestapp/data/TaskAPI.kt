package com.example.taskstestapp.data

import com.example.taskstestapp.domain.tasks.Task
import com.example.taskstestapp.domain.users.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskAPI {
    @GET("users")
    suspend fun getUsers(): ArrayList<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @GET("todos")
    suspend fun getTasksByUserId(
        @Query("userId") userId: Int
    ): ArrayList<Task>

}