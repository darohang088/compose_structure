package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.feature.auth.navigation.AuthNavHost
import com.example.app.feature.profile.navigation.ProfileNavHost
import com.example.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val mainViewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Prevent unused variable compilation error due to -Werror.
        splashScreen.setKeepOnScreenCondition { false }
        
        setContent {
            AppTheme {
                val isLoggedIn by mainViewModel.isLoggedIn.collectAsStateWithLifecycle()
                
                Crossfade(targetState = isLoggedIn, label = "DualNavHostTransition") { isAuthenticated ->
                    if (isAuthenticated) {
                        ProfileNavHost(onLogout = { mainViewModel.updateLoginState(false) })
                    } else {
                        AuthNavHost(onLoginSuccess = { mainViewModel.updateLoginState(true) })
                    }
                }
            }
        }
    }
}
