package se.zolda.coloredsudoku.startpage.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import se.zolda.coloredsudoku.BuildConfig
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.databinding.FragmentStartPageBinding
import se.zolda.coloredsudoku.util.*
import kotlin.random.Random

@AndroidEntryPoint
class StartPageFragment : Fragment() {
    private lateinit var binding: FragmentStartPageBinding
    private var firstShow = true
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStartPageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadInterstitialAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        if (!firstShow) {
            AnimationManager.alphaAnimation(binding.buttonLayout) {}
            AnimationManager.alphaAnimation(binding.currentLevelLayout) {}
            binding.playButton.isEnabled = true
        }
        AnimationManager.alphaAnimation(binding.logo) {
            if (firstShow) {
                firstShow = false
                binding.logo.animate()
                    .rotation(180f)
                    .y(binding.guidelineTop.y)
                    .setDuration(1000)
                    .withEndAction {
                        binding.logo.y = binding.guidelineTop.y
                        AnimationManager.alphaAnimation(binding.buttonLayout) {}
                        AnimationManager.alphaAnimation(binding.currentLevelLayout) {}
                        binding.buttonLayout.show()
                        binding.currentLevelLayout.show()
                    }
                    .start()
            }
        }
    }


    private fun loadInterstitialAd(){
        InterstitialAd.load(requireContext(), BuildConfig.INTERSTITIAL_AD_ID,
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    FirebaseCrashlytics.getInstance().log("StartPage failed to LOAD interstitial ad. Code: ${adError.code} Message: ${adError.message}")
                    mInterstitialAd?.fullScreenContentCallback = null
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = fullScreenContentCallback
                }
            })
    }

    private val fullScreenContentCallback = object: FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            mInterstitialAd = null
            navigateToGameView()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            FirebaseCrashlytics.getInstance().log("StartPage failed to SHOW interstitial ad. Code: ${adError?.code} Message: ${adError?.message}")
            navigateToGameView()
        }

        override fun onAdShowedFullScreenContent() {}
    }

    private fun navigateToGameView(){
        AnimationManager.reverseAlphaAnimation(binding.mainLayout) {
            findNavController().navigate(R.id.action_startPageFragment_to_gameFragment)
        }
    }

    private fun setupViews() {
        enableButtons(true)
        binding.playButton.setOnClickListener {
            enableButtons(false)
            if(shouldShowInterstitialAd()) mInterstitialAd?.show(requireActivity()) ?: kotlin.run { navigateToGameView() }
            else navigateToGameView()
        }
        binding.levelsButton.setOnClickListener {
            enableButtons(false)
            findNavController().navigate(R.id.action_startPageFragment_to_levelsFragment)
        }
        binding.currentLevelTitle.text =
            String.format(getString(R.string.current_level, AppPreferences.currentLevel))
        getRandomColor()?.let { color ->
            binding.currentLevelTitle.setTextColor(
                ContextCompat.getColor(requireContext(), color)
            )
        }
    }

    private fun enableButtons(enable: Boolean){
        binding.playButton.isEnabled = enable
        binding.levelsButton.isEnabled = enable
    }
}