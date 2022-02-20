package se.zolda.coloredsudoku.levels.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.data.model.LevelScore
import se.zolda.coloredsudoku.databinding.FragmentLevelsListItemBinding
import se.zolda.coloredsudoku.levels.viewmodel.LevelsListener
import se.zolda.coloredsudoku.util.*

class LevelViewHolder(
    private val binding: FragmentLevelsListItemBinding,
    private val listener: LevelsListener
): LevelBaseViewHolder<LevelScore>(binding.root) {
    override fun bind(item: LevelScore) {
        when(item.locked){
            true -> {
                binding.cell.setCellDrawable(R.drawable.level_item_locked_bg)
                binding.level.hide()
                binding.time.hide()
                binding.lockIcon.show()
            }
            else -> {
                getRandomColor()?.let { color ->
                    binding.cell.setCellDrawable(R.drawable.level_item_bg)
                    binding.cell.background.setDrawableTint(
                        binding.cell.context,
                        color
                    )
                }
                binding.level.show()
                binding.time.show()
                binding.lockIcon.hide()
            }
        }
        binding.level.text = item.id.toString()
        binding.time.text = item.time.formatMillisHHmmss()
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: LevelsListener
        ): LevelBaseViewHolder<LevelScore> {
            return LevelViewHolder(
                FragmentLevelsListItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                listener
            )
        }
    }
}

abstract class LevelBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}