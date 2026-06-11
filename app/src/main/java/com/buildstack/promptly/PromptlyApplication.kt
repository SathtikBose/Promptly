package com.buildstack.promptly

import android.app.Application
import com.buildstack.promptly.di.AppContainer
import com.buildstack.promptly.di.DefaultAppContainer

class PromptlyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
