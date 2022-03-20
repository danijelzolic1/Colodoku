package se.zolda.coloredsudoku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(listOf("1AA45BB2316351C380AED0F4CE4D1C3C")).build()
        MobileAds.setRequestConfiguration(configuration)
        MobileAds.initialize(this)
    }
}