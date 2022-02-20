package se.zolda.coloredsudoku.game.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.databinding.FragmentGameCellItemBinding
import se.zolda.coloredsudoku.databinding.FragmentGameCellNoteItemBinding
import se.zolda.coloredsudoku.game.model.SudokuCell
import se.zolda.coloredsudoku.game.viewmodel.SudokuGridListener
import se.zolda.coloredsudoku.util.*

class SudokuCellViewHolder(
    private val binding: FragmentGameCellItemBinding,
    private val listener: SudokuGridListener
) :
    SudokuCellBaseViewHolder<SudokuCell>(binding.root) {

    override fun bind(item: SudokuCell) {
        handleSelectedItem(item)
        item.color?.let { color ->
            binding.colorView.background?.setDrawableTint(binding.colorView.context, color)
            binding.colorView.show()
            binding.notes.hide()
            binding.notes.removeAllViews()
        } ?: kotlin.run {
            binding.colorView.hide()
            binding.notes.show()

            addNotes(item)
        }

        binding.executePendingBindings()
    }

    private fun addNotes(item: SudokuCell){
        binding.notes.removeAllViews()
        item.notes.forEach { note ->
            val noteBinding = FragmentGameCellNoteItemBinding.inflate(
                LayoutInflater.from(binding.notes.context),
                binding.notes,
                false
            )
            noteBinding.noteBg.background?.setDrawableTint(noteBinding.noteBg.context, note)

            val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ).apply {
                width = 0
                height = 0
                this.leftMargin = 2
                this.rightMargin = 2
            }

            binding.notes.addView(noteBinding.root, layoutParams)
        }
        if(item.notes.isNotEmpty() && item.notes.size < 9) for(i in item.notes.size until 9){
            val noteBinding = FragmentGameCellNoteItemBinding.inflate(
                LayoutInflater.from(binding.notes.context),
                binding.notes,
                false
            )
            noteBinding.noteBg.background = null

            val layoutParams: GridLayout.LayoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ).apply {
                width = 0
                height = 0
                this.leftMargin = 2
                this.rightMargin = 2
            }

            binding.notes.addView(noteBinding.root, layoutParams)
        }
    }

    private fun handleSelectedItem(item: SudokuCell) {
        when {
            item.isSelected -> {
                /*binding.cell.background.setDrawableTintColor(
                    /*item.color?.getLighterColor(
                        binding.root.context,
                        0.7f
                    ) ?: kotlin.run {
                        R.color.sudoku_empty.getLighterColor(
                            binding.root.context,
                            0.3f
                        )
                    }*/
                    item.color?.getLighterColor(
                        binding.root.context,
                        0.7f
                    ) ?: kotlin.run {
                        R.color.sudoku_empty.getLighterColor(
                            binding.root.context,
                            0.3f
                        )
                    }
                )*/
                binding.cell.setCellDrawable(R.drawable.rounded_bg_selected)
            }
            /*item.isSameBoxRowColumn -> {
                binding.cell.setCellDrawable(R.drawable.rounded_bg)
            }*/
            item.color != null && item.canEdit -> {
                binding.cell.setCellDrawable(R.drawable.rounded_bg)
                binding.cell.background.setDrawableTintColor(
                    item.color!!.getLighterColor(
                        binding.cell.context,
                        0.7f
                    )
                )
            }
            else -> {
                binding.cell.background = null
            }
        }

    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: SudokuGridListener
        ): SudokuCellBaseViewHolder<SudokuCell> {
            return SudokuCellViewHolder(
                FragmentGameCellItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                listener
            )
        }
    }
}

abstract class SudokuCellBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}