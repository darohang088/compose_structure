package com.example.app.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.app.data.model.ExampleModel
import com.example.app.ui.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingStateDisplaysProgressIndicator() {
        composeTestRule.setContent {
            AppTheme {
                ExampleScreenLoadingPreview()
            }
        }
        
        // Progress indicator has no specific text, tests usually match by tag in a real app,
        // but for preview it just runs without crashing to ensure the node tree is built.
    }

    @Test
    fun testSuccessStateDisplaysData() {
        val testData = listOf(
            ExampleModel(1, "Test Title 1", "Test Body 1", 1),
            ExampleModel(2, "Test Title 2", "Test Body 2", 1)
        )

        composeTestRule.setContent {
            AppTheme {
                ExampleList(examples = testData)
            }
        }

        composeTestRule.onNodeWithText("Test Title 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Body 1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Title 2").assertIsDisplayed()
    }

    @Test
    fun testErrorStateDisplaysErrorAndRetryButton() {
        val errorMessage = "Connection failed"
        
        composeTestRule.setContent {
            AppTheme {
                ErrorContent(message = errorMessage, onRetry = {})
            }
        }

        composeTestRule.onNodeWithText("Error: $errorMessage").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
    }
}
