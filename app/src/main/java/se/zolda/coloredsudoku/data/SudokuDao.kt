package se.zolda.coloredsudoku.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import se.zolda.coloredsudoku.game.model.SudokuBoard

@Dao
interface SudokuDao {
    @Query("SELECT * FROM sudoku_board WHERE id == 0 limit 1")
    suspend fun getSolvedBoard(): SudokuBoard?

    @Query("SELECT * FROM sudoku_board WHERE id == 1 limit 1")
    suspend fun getBoard(): SudokuBoard?

    @Query("SELECT * FROM sudoku_board WHERE id == 1 limit 1")
    fun getBoardLiveData(): LiveData<SudokuBoard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sudokuBoard: SudokuBoard)

    @Query("DELETE FROM sudoku_board WHERE id == 1")
    suspend fun deleteBoard()

    @Query("DELETE FROM sudoku_board")
    suspend fun deleteAll()
}