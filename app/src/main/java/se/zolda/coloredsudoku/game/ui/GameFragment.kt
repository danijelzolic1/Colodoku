package se.zolda.coloredsudoku.game.ui

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import se.zolda.coloredsudoku.BuildConfig
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.databinding.FragmentGameBinding
import se.zolda.coloredsudoku.game.color.ColorGridAdapter
import se.zolda.coloredsudoku.data.model.SudokuBoardState
import se.zolda.coloredsudoku.game.ui.dialog.RestartCurrentLevelDialog
import se.zolda.coloredsudoku.game.ui.dialog.RestartCurrentLevelListener
import se.zolda.coloredsudoku.game.viewmodel.GameViewModel
import se.zolda.coloredsudoku.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


@AndroidEntryPoint
class GameFragment : Fragment(), RestartCurrentLevelListener {
    private lateinit var binding: FragmentGameBinding
    private lateinit var adapter: GameGridAdapter
    private lateinit var colorAdapter: ColorGridAdapter
    private val viewModel: GameViewModel by viewModels()
    private var restartDialog: RestartCurrentLevelDialog? = null
    private var gameState: SudokuBoardState = SudokuBoardState.SOLVED
    private var firstUpdate = true

    private var mInterstitialAd: InterstitialAd? = null
    private var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGameBinding.inflate(layoutInflater)
        setupGrid()
        setupViews()
        loadInterstitialAd()
        loadRewardedAdd()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        startClock()
    }

    private fun loadRewardedAdd(){
        RewardedAd.load(requireContext(), BuildConfig.REWARD_AD_ID, AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("GameFragment", "Error loading rewarded ad: ${adError.message} - ${adError.code}")
                mRewardedAd?.fullScreenContentCallback = null
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
                mRewardedAd?.fullScreenContentCallback = rewardedFullScreenContentCallback
            }
        })
    }

    private fun loadInterstitialAd(){
        InterstitialAd.load(requireContext(), BuildConfig.INTERSTITIAL_AD_ID,
            AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.e("GameFragment", "Error loading interstitial ad: ${adError.message} - ${adError.code}")
                    mInterstitialAd?.fullScreenContentCallback = null
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback = interstitialFullScreenContentCallback
                }
            })
    }

    private val rewardedFullScreenContentCallback = object: FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            mRewardedAd = null
            loadRewardedAdd()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
        }

        override fun onAdShowedFullScreenContent() {
        }
    }

    private val interstitialFullScreenContentCallback = object: FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            mInterstitialAd = null
            loadInterstitialAd()
            loadNextLevel()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            loadNextLevel()
        }

        override fun onAdShowedFullScreenContent() {
        }
    }

    override fun onPause() {
        super.onPause()
        binding.clock.stop()
    }

    private fun showHintRewardAd(){
        mRewardedAd?.show(
            requireActivity()
        ) { _ ->
            AppPreferences.numberOfHints += 1
            viewModel.onHintRewarded()
        }
    }

    private fun setupViews() {
        binding.back.setOnClickListener {
            AnimationManager.reverseAlphaAnimation(binding.mainLayout) {
                findNavController().navigateUp()
            }
        }
        binding.actionButtons.noteButton.setOnClickListener {
            viewModel.toggleNoteSelected()
        }
        binding.actionButtons.restartButton.setOnClickListener {
            restartDialog?.show() ?: kotlin.run {
                restartDialog = RestartCurrentLevelDialog(requireActivity(), this)
                restartDialog?.show()
            }
        }
        binding.actionButtons.helpButton.setOnClickListener {
            showHintRewardAd()
        }

        binding.levelCompleteButtons.homeButton.setOnClickListener {
            binding.levelCompleteLayout.hide()
            AnimationManager.reverseAlphaAnimation(binding.mainLayout) {
                findNavController().navigateUp()
            }
        }

        binding.levelCompleteButtons.nextButton.setOnClickListener {
            if(shouldShowInterstitialAd()){
                mInterstitialAd?.show(requireActivity()) ?: kotlin.run { loadNextLevel() }
            } else loadNextLevel()
        }

        binding.clock.setOnChronometerTickListener {
            if(gameState == SudokuBoardState.PLAYING) viewModel.updateTime(it.base)
        }
    }

    private fun loadNextLevel(){
        binding.levelCompleteLayout.hide()
        AnimationManager.reverseAlphaAnimation(binding.mainLayout){
            binding.mainLayout.hide()
            binding.mainLayout.alpha = 1.0f
            firstUpdate = true
            viewModel.nextLevel()
        }
    }

    private fun setupGrid() {
        val gridLayoutManager = GridLayoutManager(requireContext(), AppPreferences.spanCount)
        binding.grid.layoutManager = gridLayoutManager
        binding.grid.setHasFixedSize(true)
        binding.grid.addItemDecoration(GridItemDecoration(requireContext()))
        adapter = GameGridAdapter(viewModel, context?.isDarkThemeOn() ?: false)
        binding.grid.adapter = adapter

        val colorGridLayoutManager = GridLayoutManager(requireContext(), 5)
        binding.colors.layoutManager = colorGridLayoutManager
        binding.colors.setHasFixedSize(true)
        colorAdapter = ColorGridAdapter(viewModel)
        binding.colors.adapter = colorAdapter

    }

    private fun setupViewModel() {
        viewModel.board.observe(viewLifecycleOwner) {
            gameState = it.sudokuBoardState
            when(gameState){
                SudokuBoardState.SOLVED -> {
                    onPuzzleSolved()
                }
                else -> {
                    adapter.update(it)
                    colorAdapter.update(it.colors)
                    if (firstUpdate) {
                        startClock()
                        firstUpdate = false
                        AnimationManager.alphaAnimation(binding.mainLayout) {}
                        binding.mainLayout.show()
                    }
                }
            }
        }

        viewModel.noteSelected.observe(viewLifecycleOwner) {
            binding.actionButtons.noteButton.setCellDrawable(R.drawable.game_notes_button_bg)
            if (it) binding.actionButtons.noteButton.background.setDrawableTint(requireContext(), R.color.notes)
            binding.actionButtons.noteButton.drawable.setDrawableTintColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (it) R.color.sudoku_white else R.color.notes
                )
            )
        }
    }

    private fun onPuzzleSolved(){
        binding.clock.stop()
        AppPreferences.timer = 0
        binding.confetti.start(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                size = listOf(Size.LARGE),
                colors = getListOfColors(requireContext()),
                emitter = Emitter(duration = 200, TimeUnit.MILLISECONDS).max(200),
                position = Position.Relative(0.5, 0.3)
            )
        )
        getRandomColor()?.let { color ->
            binding.levelCompleteTitle.backgroundTintList = AppCompatResources.getColorStateList(requireContext(), color)
        }
        AnimationManager.scaleUp(binding.levelCompleteCv, {})
        binding.levelCompleteTitle.text = getRandomLevelCompleteTitle()
        binding.levelCompleteInfo.text = getRandomLevelCompleteInfo()
        binding.levelCompleteLayout.show()
    }

    private fun startClock() {
        if(gameState == SudokuBoardState.SOLVED) return
        binding.clock.base = SystemClock.elapsedRealtime() - AppPreferences.timer
        binding.clock.start()
    }

    override fun onRestart() {
        viewModel.restartLevel()
    }

    private fun getRandomLevelCompleteTitle(): String{
        val array = resources.getStringArray(R.array.level_complete_titles)
        return array[Random.nextInt(array.size)]
    }

    private fun getRandomLevelCompleteInfo(): String{
        return when (AppPreferences.currentLevel) {
            in 1..20 -> getRandomLevelCompleteEasy()
            in 21..40 -> getRandomLevelCompleteMedium()
            in 41..55 -> getRandomLevelCompleteHard()
            else -> getRandomLevelCompleteExtreme()
        }
    }

    private fun getRandomLevelCompleteEasy(): String{
        val array = resources.getStringArray(R.array.level_complete_easy)
        return array[Random.nextInt(array.size)]
    }

    private fun getRandomLevelCompleteMedium(): String{
        val array = resources.getStringArray(R.array.level_complete_medium)
        return array[Random.nextInt(array.size)]
    }

    private fun getRandomLevelCompleteHard(): String{
        val array = resources.getStringArray(R.array.level_complete_hard)
        return array[Random.nextInt(array.size)]
    }

    private fun getRandomLevelCompleteExtreme(): String{
        val array = resources.getStringArray(R.array.level_complete_extreme)
        return array[Random.nextInt(array.size)]
    }
}