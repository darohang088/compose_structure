package com.example.app.utils

import com.example.app.BuildConfig

/**
 * Helper object reading BuildConfig fields and exposing them cleanly to the app.
 */
object AppConfig {
    val BASE_URL: String = BuildConfig.BASE_URL
    val ENVIRONMENT_NAME: String = BuildConfig.ENVIRONMENT_NAME
    val IS_LOGGING_ENABLED: Boolean = BuildConfig.IS_LOGGING_ENABLED
}
