package com.example.taskstestapp.domain.users

class GetUserListUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getUserList()
    }
}