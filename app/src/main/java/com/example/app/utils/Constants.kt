package com.example.app.utils

import com.example.app.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.BASE_URL
    const val ENDPOINT_PATH = "example/path" // Placeholder endpoint path
    
    const val DATABASE_NAME = "app_database.db"
    const val DATASTORE_NAME = "app_preferences"
    
    // Timeout limits
    const val NETWORK_TIMEOUT = 30L
}
