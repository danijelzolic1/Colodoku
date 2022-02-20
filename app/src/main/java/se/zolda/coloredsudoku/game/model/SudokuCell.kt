package se.zolda.coloredsudoku.game.model

import androidx.annotation.ColorRes
import se.zolda.coloredsudoku.R

data class SudokuCell(
    val id : Int = 0,
    @ColorRes var color: Int? = R.color.sudoku_blue,
    var isSelected : Boolean = false,
    var isSameBoxRowColumn : Boolean = false,
    val column: Int = 0,
    val row: Int = 0,
    var value: Int = 0,
    var notes: List<Int> = listOf(),
    val canEdit: Boolean = false
)
