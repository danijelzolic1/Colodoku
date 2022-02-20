package se.zolda.coloredsudoku.levels.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import se.zolda.coloredsudoku.databinding.FragmentLevelsBinding
import se.zolda.coloredsudoku.levels.viewmodel.LevelsViewModel
import se.zolda.coloredsudoku.util.AnimationManager

@AndroidEntryPoint
class LevelsFragment: Fragment() {
    private lateinit var binding: FragmentLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()
    private lateinit var adapter: LevelsGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLevelsBinding.inflate(layoutInflater)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    private fun setupViews() {
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupGrid() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 5)
        binding.grid.layoutManager = gridLayoutManager
        binding.grid.addItemDecoration(LevelItemDecoration(requireContext()))
        adapter = LevelsGridAdapter(viewModel)
        binding.grid.adapter = adapter

    }


    private fun setupViewModel() {
        viewModel.levels.observe(viewLifecycleOwner){
            adapter.update(it)
        }
    }
}