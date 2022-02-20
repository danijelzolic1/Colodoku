package se.zolda.coloredsudoku.util

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

    fun getNumberOfColumnsAndRows(spanCount: Int): Pair<Int, Int>{
        return when(spanCount){
            4 ->  Pair(2, 2)
            6 -> Pair(3, 2)
            8 -> Pair(4, 2)
            else -> Pair(3, 3)
        }
    }

    fun getBoxFor(index: Int, rowsPerBox: Int, columnsPerBox: Int): Int{
        val nChunkIndex: Int = index / columnsPerBox
        val row: Int = nChunkIndex / (rowsPerBox * rowsPerBox)
        val column: Int = nChunkIndex % rowsPerBox

        return column + row * rowsPerBox
    }
}