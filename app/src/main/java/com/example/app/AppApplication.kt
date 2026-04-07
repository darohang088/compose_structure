package com.example.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (com.example.app.utils.AppConfig.IS_LOGGING_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
