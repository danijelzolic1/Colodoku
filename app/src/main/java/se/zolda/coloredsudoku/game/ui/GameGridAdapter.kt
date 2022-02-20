package se.zolda.coloredsudoku.game.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.data.model.SudokuBoard
import se.zolda.coloredsudoku.data.model.SudokuCell
import se.zolda.coloredsudoku.game.viewmodel.SudokuGridListener

class GameGridAdapter(
    private val listener: SudokuGridListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val items = mutableListOf<SudokuCell>()

    @SuppressLint("NotifyDataSetChanged")
    fun update(board: SudokuBoard) {
        if(board.cells.size == items.size){
            val diffCallback = GameDiffCallback(items, board.cells)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            items.clear()
            items.addAll(board.cells)
            diffResult.dispatchUpdatesTo(this)
        } else{
            items.clear()
            items.addAll(board.cells)
            this.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SudokuCellViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as SudokuCellViewHolder).bind(item)
        if(item.canEdit) holder.itemView.setOnClickListener {
            listener.onToggleSelectedCell(item)
        }
    }

    override fun getItemCount(): Int = items.size

}

class GameDiffCallback(private val oldList: List<SudokuCell>, private val newList: List<SudokuCell>) : DiffUtil.Callback() {
    override fun getNewListSize(): Int = newList.size
    override fun getOldListSize(): Int = oldList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}