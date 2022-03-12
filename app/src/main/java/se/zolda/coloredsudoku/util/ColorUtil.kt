package se.zolda.coloredsudoku.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import se.zolda.coloredsudoku.R
import kotlin.random.Random

fun Int.cellBackgroundLight(context: Context, ratio: Float): Int {
    val color = ContextCompat.getColor(context, this)
    return ColorUtils.blendARGB(color, context.getColor(R.color.background), ratio)
}

fun getRandomColor(): Int?{
    return getColorForValue(Random.nextInt(1, 9))
}

fun getColorForValue(value: Int): Int? {
    return when (value) {
        1 -> R.color.sudoku_blue
        2 -> R.color.sudoku_red
        3 -> R.color.sudoku_beige
        4 -> R.color.sudoku_green
        5 -> R.color.sudoku_orange
        6 -> R.color.sudoku_pink
        7 -> R.color.sudoku_purple
        8 -> R.color.sudoku_teal
        9 -> R.color.sudoku_yellow
        else -> null
    }
}

fun getListOfColors(context: Context) = listOf(
    ContextCompat.getColor(
        context,
        R.color.sudoku_blue
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_yellow
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_red
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_green
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_purple
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_teal
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_orange
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_pink
    ),
    ContextCompat.getColor(
        context,
        R.color.sudoku_beige
    )
)