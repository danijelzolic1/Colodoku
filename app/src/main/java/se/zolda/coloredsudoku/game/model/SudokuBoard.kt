package se.zolda.coloredsudoku.game.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "sudoku_board")
data class SudokuBoard(
    @PrimaryKey var id: Int = 0,
    var cells: List<SudokuCell> = listOf(),
    var colors: List<ColorCell> = listOf(),
    var started: Date = Date(),
    var sudokuBoardState: SudokuBoardState = SudokuBoardState.SOLVED
){
    fun print(){
        cells.forEach { cell ->
            print("SUDOKUCELL [${cell.column}[${cell.row}] = ${cell.value}")
        }
    }
}

enum class SudokuBoardState{
    PLAYING, SOLVED
}