package se.zolda.coloredsudoku.game.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.util.GridUtil
import kotlin.math.floor


class GridItemDecoration(
    private val context: Context
) : RecyclerView.ItemDecoration() {

    private val extraOffsetMultiplier = 8
    private var boxLine: Paint = Paint()
    private var gridLine: Paint = Paint()

    init {
        boxLine.color = ContextCompat.getColor(context, R.color.box_stroke)
        var thickness = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f, context.resources.displayMetrics
        )
        boxLine.strokeWidth = thickness

        gridLine.color = ContextCompat.getColor(context, R.color.grid_stroke)
        thickness = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f, context.resources.displayMetrics
        )
        gridLine.strokeWidth = thickness
    }

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
            outRect[0, 0, gridLine.strokeWidth.toInt()] = gridLine.strokeWidth.toInt()
        }

    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val offset = gridLine.strokeWidth / 2
        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            val params = view.layoutParams as RecyclerView.LayoutParams
            val position = params.viewAdapterPosition
            val column = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
            val row = floor((position/spanCount).toFloat()).toInt()

            val gridColumns = GridUtil.getColumnGridLineIndexes(spanCount)
            val rowColumns = GridUtil.getRowGridLineIndexes(spanCount)
            if(gridColumns.contains(column)){
                drawEndGridStroke(view, canvas, offset, column, spanCount)
                if(rowColumns.contains(row)){
                    drawBottomGridStroke(view, canvas, offset, row, spanCount)
                } else drawBottomBoxStroke(view, canvas, offset, row, spanCount)
            } else{
                drawEndBoxStroke(view, canvas, offset, column, spanCount)
                if(rowColumns.contains(row)){
                    drawBottomGridStroke(view, canvas, offset, row, spanCount)
                } else drawBottomBoxStroke(view, canvas, offset, row, spanCount)
            }
        }
    }

    private fun drawEndGridStroke(
        view: View,
        canvas: Canvas,
        offset: Float,
        column: Int,
        spanCount: Int
    ) {
        if (column != spanCount - 1) {
            canvas.drawLine(
                view.right.toFloat() + offset,
                view.top.toFloat() - offset,
                view.right.toFloat() + offset,
                view.bottom.toFloat() + offset, gridLine,
            )
        }
    }

    private fun drawEndBoxStroke(
        view: View,
        canvas: Canvas,
        offset: Float,
        column: Int,
        spanCount: Int
    ) {
        if (column != spanCount - 1) {
            canvas.drawLine(
                view.right.toFloat() + offset,
                view.top.toFloat() + offset*extraOffsetMultiplier,
                view.right.toFloat() + offset,
                view.bottom.toFloat() - offset*extraOffsetMultiplier, boxLine
            )
        }
    }

    private fun drawBottomGridStroke(
        view: View,
        canvas: Canvas,
        offset: Float,
        row: Int,
        spanCount: Int
    ) {
        if (row != spanCount - 1) {
            canvas.drawLine(
                view.left.toFloat() - offset,
                view.bottom.toFloat() + offset,
                view.right.toFloat() + offset,
                view.bottom.toFloat() + offset, gridLine
            )
        }
    }

    private fun drawBottomBoxStroke(
        view: View,
        canvas: Canvas,
        offset: Float,
        row: Int,
        spanCount: Int
    ) {
        if (row != spanCount - 1) {
            canvas.drawLine(
                view.left.toFloat() + offset*extraOffsetMultiplier,
                view.bottom.toFloat() + offset,
                view.right.toFloat() - offset*extraOffsetMultiplier,
                view.bottom.toFloat() + offset, boxLine
            )
        }
    }
}

enum class GridLinePosition {
    END, BOTTOM
}