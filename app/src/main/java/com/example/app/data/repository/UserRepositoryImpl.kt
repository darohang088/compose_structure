package com.example.app.data.repository

import com.example.app.utils.Resource
import com.example.app.data.remote.api.UserApi
import com.example.app.domain.model.User
import com.example.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override fun getUsers(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())
        try {
            val users = api.getUsers().map { it.toUser() }
            emit(Resource.Success(users))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
