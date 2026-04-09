package com.example.app.data.repository

import com.example.app.data.local.ExampleDao
import com.example.app.data.local.toEntity
import com.example.app.data.local.toModel
import com.example.app.data.model.ExampleModel
import com.example.app.remote.ApiService
import com.example.app.utils.EmptyCacheException
import com.example.app.utils.NetworkChecker
import com.example.app.utils.NetworkTimeoutException
import com.example.app.utils.NoInternetException
import com.example.app.utils.ServerException
import com.example.app.utils.UiState
import com.example.app.utils.UnknownNetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExampleRepository @Inject constructor(
    private val apiService: ApiService,
    private val exampleDao: ExampleDao,
    private val networkChecker: NetworkChecker
) {
    fun getExamples(): Flow<UiState<List<ExampleModel>>> = flow {
        emit(UiState.Loading)
        try {
            // Check local cache first
            val cachedData = exampleDao.getAllExamples()
            if (cachedData.isNotEmpty()) {
                Timber.d("Returning cached data (count: ${cachedData.size})")
                emit(UiState.Success(cachedData.map { it.toModel() }))
            }

            // Sync with network if connected
            if (networkChecker.hasInternetConnection()) {
                Timber.d("Fetching fresh data from network")
//                val response = apiService.getExamples()
                val response = apiService.getExamples()

                if (response.isSuccessful) {
                    response.body()?.let { remoteData ->
                        // Update cache
                        exampleDao.clearExamples()
                        exampleDao.insertExamples(remoteData.map { it.toEntity() })
                        
                        // Emit fresh data
                        emit(UiState.Success(remoteData))
                    } ?: throw ServerException("Empty response body")
                } else {
                    throw ServerException(response.message(), response.code())
                }
            } else if (cachedData.isEmpty()) {
                throw NoInternetException()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching examples")
            val mappedException = when (e) {
                is SocketTimeoutException -> NetworkTimeoutException()
                is NoInternetException, is ServerException, is EmptyCacheException -> e
                else -> UnknownNetworkException()
            }
            emit(UiState.Error(mappedException.message ?: "Unknown error occurred"))
        }
    }
}
