package com.example.app.feature.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.components.AppUiWrapper

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    AppUiWrapper(
        title = "",
        isStatusBarTextDark = true,
        topBar = {}
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Login Screen", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onLoginSuccess) {
                    Text(text = "Login")
                }

//                Spacer(modifier = Modifier.height(8.dp))
//                TextButton(onClick = onNavigateToRegister) {
//                    Text(text = "Don't have an account? Register")
//                }
//
//                TextButton(onClick = onNavigateToForgotPassword) {
//                    Text(text = "Forgot Password?")
//                }
            }
        }
    }

}
