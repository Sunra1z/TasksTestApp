package com.example.taskstestapp.data

import com.example.taskstestapp.domain.users.User
import com.example.taskstestapp.domain.users.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepositoryImpl : UserRepository {

    private val api = RetrofitInstance.api

    override suspend fun getUserList(): Result<List<User>> = withContext(Dispatchers.IO) {
        try {
            val users = api.getUsers()
            Result.success(users)
        } catch (e: Exception){
            Result.failure(e)
        }

    }

    override suspend fun getUserById(id: Int): Result<User> = withContext(Dispatchers.IO) {
        try {
            val user = api.getUserById(id)
            Result.success(user)
        } catch (e: Exception){
            Result.failure(e)
        }

    }

}