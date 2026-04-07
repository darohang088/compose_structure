package com.example.app.viewmodel

import app.cash.turbine.test
import com.example.app.data.model.ExampleModel
import com.example.app.data.repository.ExampleRepository
import com.example.app.utils.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleViewModelTest {

    private val repository = mockk<ExampleRepository>()
    private lateinit classUnderTest: ExampleViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        classUnderTest = ExampleViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchExamples emits success state when repository returns data`() = runTest {
        // Arrange
        val mockData = listOf(ExampleModel(id = 1, title = "Test", body = "Body", userId = 1))
        coEvery { repository.getExamples() } returns flowOf(UiState.Success(mockData))

        // Act & Assert
        classUnderTest.uiState.test {
            // Initial state is Loading
            assertEquals(UiState.Loading, awaitItem())
            
            // Trigger fetch
            classUnderTest.fetchExamples()
            
            // Should emit success
            val successState = awaitItem()
            assert(successState is UiState.Success)
            assertEquals(mockData, (successState as UiState.Success).data)
        }
    }
}
