package com.example.taskstestapp.domain.users

class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int): Result<User>{
        return repository.getUserById(id)
    }
}