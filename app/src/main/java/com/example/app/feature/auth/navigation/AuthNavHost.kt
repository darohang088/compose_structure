package com.example.app.feature.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.feature.auth.ui.ForgotPasswordScreen
import com.example.app.feature.auth.ui.LoginScreen
import com.example.app.feature.auth.ui.OtpScreen
import com.example.app.feature.auth.ui.RegisterScreen

// Type-Safe Route Wrappers
object AuthRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val OTP = "otp"
}

@Composable
fun AuthNavHost(onLoginSuccess: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AuthRoutes.LOGIN) {
        composable(AuthRoutes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(AuthRoutes.REGISTER) },
                onNavigateToForgotPassword = { navController.navigate(AuthRoutes.FORGOT_PASSWORD) },
                onLoginSuccess = onLoginSuccess
            )
        }

        composable(AuthRoutes.REGISTER) {
            RegisterScreen(
                onNavigateBackToLogin = { navController.popBackStack() },
                onRegistrationSuccess = onLoginSuccess
            )
        }

        composable(AuthRoutes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToOtp = { navController.navigate(AuthRoutes.OTP) }
            )
        }

        composable(AuthRoutes.OTP) {
            OtpScreen(
                onNavigateBack = { navController.popBackStack() },
                onVerifySuccess = onLoginSuccess
            )
        }
    }
}
