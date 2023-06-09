package org.helllynx.surveillance

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.helllynx.surveillance.BuildConfig
import timber.log.Timber


@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}