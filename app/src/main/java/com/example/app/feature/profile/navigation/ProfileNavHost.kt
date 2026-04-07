package com.example.app.feature.profile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.feature.profile.ui.EditProfileScreen
import com.example.app.feature.profile.ui.ProfileDetailsScreen
import com.example.app.feature.profile.ui.ProfileHomeScreen
import com.example.app.feature.profile.ui.SettingsScreen
import com.example.app.feature.albums.ui.AlbumsScreen

object ProfileRoutes {
    const val PROFILE_HOME = "profile_home"
    const val EDIT_PROFILE = "edit_profile"
    const val SETTINGS = "settings"
    const val PROFILE_DETAILS = "profile_details"
    const val ALBUMS = "albums"
}

@Composable
fun ProfileNavHost(onLogout: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ProfileRoutes.PROFILE_HOME) {
        composable(ProfileRoutes.PROFILE_HOME) {
            ProfileHomeScreen(
                onNavigateToEditProfile = { navController.navigate(ProfileRoutes.EDIT_PROFILE) },
                onNavigateToSettings = { navController.navigate(ProfileRoutes.SETTINGS) },
                onNavigateToDetails = { navController.navigate(ProfileRoutes.PROFILE_DETAILS) },
                onNavigateToAlbums = { navController.navigate(ProfileRoutes.ALBUMS) },
                onLogout = onLogout
            )
        }

        composable(ProfileRoutes.EDIT_PROFILE) {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileRoutes.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileRoutes.PROFILE_DETAILS) {
            ProfileDetailsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(ProfileRoutes.ALBUMS) {
            AlbumsScreen()
        }
    }
}
