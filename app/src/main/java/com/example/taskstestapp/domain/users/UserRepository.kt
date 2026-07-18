package com.example.taskstestapp.domain.users

interface UserRepository {
    suspend fun getUserList(): Result<List<User>>

    suspend fun getUserById(id: Int): Result<User>
}