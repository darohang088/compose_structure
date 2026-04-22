package com.example.app.domain.usecase

import com.example.app.utils.Resource
import com.example.app.domain.model.User
import com.example.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<Resource<List<User>>> {
        return repository.getUsers()
    }
}
