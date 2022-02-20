package se.zolda.coloredsudoku.game.model

import androidx.annotation.ColorRes
import se.zolda.coloredsudoku.R

data class ColorCell(
    val id: Int = 0,
    @ColorRes var color: Int = R.color.sudoku_blue,
    val isErase: Boolean = false
)