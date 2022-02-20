package se.zolda.coloredsudoku.levels.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.data.model.LevelScore
import se.zolda.coloredsudoku.levels.viewmodel.LevelsListener

class LevelsGridAdapter(
    private val listener: LevelsListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<LevelScore>()

    @SuppressLint("NotifyDataSetChanged")
    fun update(list: List<LevelScore>){
        items.clear()
        items.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LevelViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as LevelViewHolder).bind(item)
        holder.itemView.setOnClickListener {
            listener.onLevelClicked(item.id)
        }
    }

    override fun getItemCount(): Int = items.size

}