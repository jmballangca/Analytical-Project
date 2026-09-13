package com.ketchupzzz.analytical

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AnalyticalApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}