package com.example.app.feature.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.components.AppUiWrapper

@Composable
fun ProfileHomeScreen(
    onNavigateToEditProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToDetails: () -> Unit,
    onNavigateToAlbums: () -> Unit,
    onLogout: () -> Unit
) {
    AppUiWrapper(
        title = "Profile Home",
        isStatusBarTextDark = true,
        topBar = {}
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(onClick = onNavigateToEditProfile) {
//                    Text(text = "Edit Profile")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Button(onClick = onNavigateToSettings) {
//                    Text(text = "Settings")
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Button(onClick = onNavigateToDetails) {
//                    Text(text = "Profile Details")
//                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onNavigateToAlbums) {
                    Text(text = "get Albums List")
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(onClick = onLogout) {
                    Text(text = "Back")
                }
            }
        }
    }

}
