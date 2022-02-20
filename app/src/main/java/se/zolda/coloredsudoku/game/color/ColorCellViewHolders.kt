package se.zolda.coloredsudoku.game.color

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.databinding.FragmentGameColorCellItemBinding
import se.zolda.coloredsudoku.game.model.ColorCell
import se.zolda.coloredsudoku.game.viewmodel.ColorGridListener
import se.zolda.coloredsudoku.util.hide
import se.zolda.coloredsudoku.util.setDrawableTint
import se.zolda.coloredsudoku.util.show

class ColorCellViewHolder(
    private val binding: FragmentGameColorCellItemBinding,
    private val listener: ColorGridListener
) : ColorCellBaseViewHolder<ColorCell>(binding.root) {

    override fun bind(item: ColorCell) {
        when(item.isErase){
            true -> binding.erase.show()
            else -> binding.erase.hide()
        }
        binding.background.background?.setDrawableTint(binding.background.context, item.color)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            listener: ColorGridListener
        ): ColorCellBaseViewHolder<ColorCell> {
            return ColorCellViewHolder(
                FragmentGameColorCellItemBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                listener
            )
        }
    }
}

abstract class ColorCellBaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}