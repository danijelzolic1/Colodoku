package se.zolda.coloredsudoku.game.color

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.data.model.ColorCell
import se.zolda.coloredsudoku.game.viewmodel.ColorGridListener

class ColorGridAdapter(
    private val listener: ColorGridListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val items = mutableListOf<ColorCell>()

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<ColorCell>) {
        this.items.clear()
        this.items.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ColorCellViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as ColorCellViewHolder).bind(item)
        holder.itemView.setOnClickListener {
            listener.onColorClicked(item)
        }
    }

    override fun getItemCount(): Int = items.size

}