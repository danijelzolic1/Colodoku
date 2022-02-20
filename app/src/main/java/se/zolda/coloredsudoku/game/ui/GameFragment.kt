package se.zolda.coloredsudoku.game.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.databinding.FragmentGameBinding
import se.zolda.coloredsudoku.game.color.ColorGridAdapter
import se.zolda.coloredsudoku.data.model.SudokuBoardState
import se.zolda.coloredsudoku.game.ui.dialog.RestartCurrentLevelDialog
import se.zolda.coloredsudoku.game.ui.dialog.RestartCurrentLevelListener
import se.zolda.coloredsudoku.game.viewmodel.GameViewModel
import se.zolda.coloredsudoku.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class GameFragment : Fragment(), RestartCurrentLevelListener {
    private lateinit var binding: FragmentGameBinding
    private lateinit var adapter: GameGridAdapter
    private lateinit var colorAdapter: ColorGridAdapter
    private val viewModel: GameViewModel by viewModels()
    private var restartDialog: RestartCurrentLevelDialog? = null
    private var gameState: SudokuBoardState = SudokuBoardState.SOLVED
    private var firstUpdate = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGameBinding.inflate(layoutInflater)
        setupGrid()
        setupViews()
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

    override fun onPause() {
        super.onPause()
        binding.clock.stop()
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

        }

        binding.levelCompleteButtons.homeButton.setOnClickListener {
            binding.levelCompleteLayout.hide()
            AnimationManager.reverseAlphaAnimation(binding.mainLayout) {
                findNavController().navigateUp()
            }
        }

        binding.levelCompleteButtons.nextButton.setOnClickListener {
            binding.levelCompleteLayout.hide()
            AnimationManager.reverseAlphaAnimation(binding.mainLayout){
                binding.mainLayout.hide()
                binding.mainLayout.alpha = 1.0f
                firstUpdate = true
                viewModel.newGame()
            }
        }

        binding.clock.setOnChronometerTickListener {
            viewModel.updateTime(it.base)
        }
    }

    private fun enableButtons(enable: Boolean){
        binding.actionButtons.helpButton.isEnabled = enable
        binding.actionButtons.noteButton.isEnabled = enable
        binding.actionButtons.restartButton.isEnabled = enable
        binding.back.isEnabled = enable
    }

    private fun setupGrid() {
        val gridLayoutManager = GridLayoutManager(requireContext(), AppPreferences.spanCount)
        binding.grid.layoutManager = gridLayoutManager
        binding.grid.setHasFixedSize(true)
        binding.grid.addItemDecoration(GridItemDecoration(requireContext()))
        adapter = GameGridAdapter(viewModel)
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
        AnimationManager.reverseHalfAlphaAnimation(binding.mainLayout, {
            binding.mainLayout.alpha = 0.5f
        })
        AnimationManager.scaleUp(binding.levelCompleteLayout, {})
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
}