package se.zolda.coloredsudoku

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import se.zolda.coloredsudoku.util.AnimationManager
import se.zolda.coloredsudoku.util.AppPreferences

@HiltAndroidApp
class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
        AnimationManager.init(this)
    }
}