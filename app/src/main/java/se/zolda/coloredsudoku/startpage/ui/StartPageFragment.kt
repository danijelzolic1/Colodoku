package se.zolda.coloredsudoku.startpage.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.databinding.FragmentStartPageBinding
import se.zolda.coloredsudoku.util.*
import kotlin.random.Random

@AndroidEntryPoint
class StartPageFragment : Fragment() {
    private lateinit var binding: FragmentStartPageBinding
    private var firstShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentStartPageBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    private fun setupViews() {
        binding.playButton.setOnClickListener {
            binding.playButton.isEnabled = false
            AnimationManager.reverseAlphaAnimation(binding.mainLayout) {
                findNavController().navigate(R.id.action_startPageFragment_to_gameFragment)
            }
        }
        binding.currentLevelTitle.text =
            String.format(getString(R.string.current_level, AppPreferences.currentLevel))
        getRandomColor()?.let { color ->
            binding.currentLevelTitle.setTextColor(
                ContextCompat.getColor(requireContext(), color)
            )
        }
    }
}