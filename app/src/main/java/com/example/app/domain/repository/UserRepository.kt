package com.example.app.domain.repository

import com.example.app.utils.Resource
import com.example.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<Resource<List<User>>>
}
