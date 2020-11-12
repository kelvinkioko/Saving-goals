package com.savings.savinggoals.app

import android.app.Application
import com.facebook.ads.AudienceNetworkAds
import com.savings.savinggoals.di.environmentModule
import com.savings.savinggoals.ui.goalmanager.goalsModule
import com.savings.savinggoals.ui.home.homeModule
import com.savings.savinggoals.ui.settings.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SavingsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this)

        startKoin {
            androidContext(this@SavingsApp)
            modules(
                environmentModule,
                settingsModule,
                homeModule,
                goalsModule
            )
        }
    }
}
