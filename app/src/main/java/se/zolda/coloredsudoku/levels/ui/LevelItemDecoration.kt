package se.zolda.coloredsudoku.levels.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.util.Constants
import se.zolda.coloredsudoku.util.physicalScreenRectPx
import kotlin.math.ceil

class LevelItemDecoration(
    private val context: Context
) : RecyclerView.ItemDecoration() {

    private val displayHeight = context.physicalScreenRectPx.height()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val position = params.viewAdapterPosition
        val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val row = (position - column) / spanCount

        if (position < state.itemCount) { // left, top, right, bottom
            outRect[0, if(row == 0) (displayHeight*0.15).toInt() else 0, 0] =
                if(row == ceil(Constants.MAX_LEVEL.toDouble()/spanCount - 1).toInt()) (displayHeight*0.15).toInt() else 0
        }
    }
}