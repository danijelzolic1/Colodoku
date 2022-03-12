package se.zolda.coloredsudoku.util

import se.zolda.coloredsudoku.R

object GridUtil {
    fun getColumnGridLineIndexes(spanCount: Int): List<Int>{
        return when(spanCount){
            4 ->  listOf(1)
            6 -> listOf(2)
            8 -> listOf(3)
            else -> listOf(2, 5)
        }
    }

    fun getRowGridLineIndexes(spanCount: Int): List<Int>{
        return when(spanCount){
            4 ->  listOf(1)
            6 -> listOf(1, 3)
            8 -> listOf(1, 3, 5)
            else -> listOf(2, 5)
        }
    }

    fun getNumberOfEmptyCells(): Int{
        return when (val level = AppPreferences.currentLevel) {
            in 1..5 -> level
            in 6..10 -> 5
            in 11..15 -> 10
            in 16..20 -> 15
            in 21..25 -> 20
            in 26..30 -> 25
            in 31..35 -> 30
            in 36..40 -> 35
            in 41..45 -> 40
            in 46..50 -> 45
            in 51..55 -> 50
            in 56..60 -> 55
            in 61..65 -> 60
            else -> 64
        }
    }

    fun getBoxFor(index: Int, rowsPerBox: Int, columnsPerBox: Int): Int{
        val nChunkIndex: Int = index / columnsPerBox
        val row: Int = nChunkIndex / (rowsPerBox * rowsPerBox)
        val column: Int = nChunkIndex % rowsPerBox

        return column + row * rowsPerBox
    }
}