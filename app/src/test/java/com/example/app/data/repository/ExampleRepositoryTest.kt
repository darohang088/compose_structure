package com.example.app.data.repository

import app.cash.turbine.test
import com.example.app.data.local.ExampleDao
import com.example.app.data.local.toEntity
import com.example.app.data.model.ExampleModel
import com.example.app.remote.ApiService
import com.example.app.utils.NetworkChecker
import com.example.app.utils.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ExampleRepositoryTest {

    private val apiService = mockk<ApiService>()
    private val exampleDao = mockk<ExampleDao>()
    private val networkChecker = mockk<NetworkChecker>()

    private lateinit var repository: ExampleRepository

    @Before
    fun setup() {
        repository = ExampleRepository(apiService, exampleDao, networkChecker)
    }

    @Test
    fun `getExamples returns cached data when available and internet is disconnected`() = runTest {
        val mockModel = ExampleModel(1, "Test", "Body", 1)
        val mockEntities = listOf(mockModel.toEntity())
        
        every { networkChecker.hasInternetConnection() } returns false
        coEvery { exampleDao.getAllExamples() } returns mockEntities

        repository.getExamples().test {
            assertEquals(UiState.Loading, awaitItem())
            
            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            assertEquals(listOf(mockModel), (successState as UiState.Success).data)
            
            awaitComplete()
        }
    }

    @Test
    fun `getExamples returns network data and caches it when internet is available`() = runTest {
        val mockModel = ExampleModel(1, "Test", "Body", 1)
        val mockList = listOf(mockModel)
        val mockEntities = listOf(mockModel.toEntity())
        
        every { networkChecker.hasInternetConnection() } returns true
        coEvery { exampleDao.getAllExamples() } returns emptyList() // No cache
        coEvery { apiService.getExamples() } returns Response.success(mockList)
        coEvery { exampleDao.clearExamples() } returns Unit
        coEvery { exampleDao.insertExamples(mockEntities) } returns Unit

        repository.getExamples().test {
            assertEquals(UiState.Loading, awaitItem())
            
            val successItem = awaitItem()
            assertTrue(successItem is UiState.Success)
            assertEquals(mockList, (successItem as UiState.Success).data)
            
            awaitComplete()
        }
        
        coVerify(exactly = 1) { exampleDao.clearExamples() }
        coVerify(exactly = 1) { exampleDao.insertExamples(mockEntities) }
    }
    
    @Test
    fun `getExamples returns error when network fails and cache is empty`() = runTest {
        every { networkChecker.hasInternetConnection() } returns true
        coEvery { exampleDao.getAllExamples() } returns emptyList()
        coEvery { apiService.getExamples() } returns Response.error(404, "Not found".toResponseBody(null))

        repository.getExamples().test {
            assertEquals(UiState.Loading, awaitItem())
            
            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            
            awaitComplete()
        }
    }
}
