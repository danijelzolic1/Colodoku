package se.zolda.coloredsudoku.util

import kotlin.math.floor
import kotlin.math.sqrt

class InvalidSizeError : Exception("Requested size is invalid (4-n)")
class InvalidLevelError : Exception("Level isnt in range 0-64")

class Generator(private val size: Int, private val level: Int) {
    private var sqrSize: Int
    private val board: Array<Array<Int>>
    private val solvedBoard: Array<Array<Int>>

    init {
        if (size < 4) throw InvalidSizeError()
        if (level !in 0..64) throw InvalidLevelError()

        sqrSize = sqrt(size.toDouble()).toInt()
        board = Array(size) { Array(size) {0}}
        solvedBoard = Array(size) { Array(size) {0}}
    }

    fun build() : Pair<Array<Array<Int>>, Array<Array<Int>>> {
        fillDiagonal()
        fillRemaining()
        setDifficulty()
        return Pair(solvedBoard, board)
    }

    private fun fillDiagonal() {
        for (i in 0 until size step sqrSize) {
            fillBox(i,i)
        }
    }

    private fun fillBox(row: Int, col: Int) {
        var num: Int
        for (i in 0 until sqrSize) {
            for (j in 0 until sqrSize) {
                do {
                    num = randomNum(size)
                } while(!canFillBox(row, col, num))

                board[row + i][col + j] = num
                solvedBoard[row + i][col + j] = num
            }
        }
    }

    private fun canFillBox(rowStart: Int, colStart: Int, num: Int) : Boolean {
        for (i in 0 until sqrSize) {
            for (j in 0 until sqrSize) {
                if (board[rowStart + i][colStart + j] == num) return false
            }
        }

        return true
    }

    private fun randomNum(max: Int) : Int {
        return floor((Math.random()*max+1)).toInt()
    }

    private fun canFill(i: Int, j: Int, num: Int): Boolean {
        return canFillRow(i, num) &&
                canFillCol(j, num) &&
                canFillBox(i - i % sqrSize, j - j % sqrSize, num)
    }

    private fun canFillRow(i: Int, num: Int): Boolean {
        for (j in 0 until size)
            if (board[i][j] == num)
                return false
        return true
    }

    private fun canFillCol(j: Int, num: Int): Boolean {
        for (i in 0 until size)
            if (board[i][j] == num)
                return false
        return true
    }

    private fun fillRemaining(argI: Int = 0, argJ: Int = sqrSize) : Boolean {
        var i = argI
        var j = argJ

        if (j >= size && i < size - 1) {
            i += 1
            j = 0
        }
        if (i >= size && j >= size)
            return true

        if (i < sqrSize) {
            if (j < sqrSize)
                j = sqrSize
        } else if (i < size - sqrSize) {
            if (j == (i / sqrSize) * sqrSize)
                j += sqrSize
        } else {
            if (j == size - sqrSize) {
                i += 1
                j = 0
                if (i >= size)
                    return true
            }
        }

        (1..size).shuffled().forEach { num ->
            if (canFill(i, j, num)) {
                board[i][j] = num
                solvedBoard[i][j] = num
                if (fillRemaining(i, j + 1))
                    return true

                board[i][j] = 0
                solvedBoard[i][j] = 0
            }
        }
        return false
    }

    private fun setDifficulty() {
        var count = level
        while (count != 0) {
            val cellId = randomNum(size * size) - 1
            val i = cellId / size
            val j = cellId % size

            if (board[i][j] != 0) {
                count--
                board[i][j] = 0
            }
        }
    }
}