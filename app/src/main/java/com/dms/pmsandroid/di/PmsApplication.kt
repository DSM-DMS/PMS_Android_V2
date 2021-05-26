package com.dms.pmsandroid.di

import android.app.Application
import com.dms.pmsandroid.di.register.registerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PmsApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PmsApplication)
            modules(
                listOf(
                    registerModule
                )
            )
        }
    }
}