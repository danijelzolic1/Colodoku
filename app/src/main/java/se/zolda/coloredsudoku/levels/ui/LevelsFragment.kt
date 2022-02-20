package se.zolda.coloredsudoku.levels.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import se.zolda.coloredsudoku.databinding.FragmentLevelsBinding
import se.zolda.coloredsudoku.levels.viewmodel.LevelsViewModel

@AndroidEntryPoint
class LevelsFragment: Fragment() {
    private lateinit var binding: FragmentLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLevelsBinding.inflate(layoutInflater)
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
        viewModel.test()
    }
}