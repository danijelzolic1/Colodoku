package se.zolda.coloredsudoku.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import se.zolda.coloredsudoku.data.model.LevelScore

@Dao
interface LevelScoreDao {
    @Query("SELECT * FROM level_score WHERE id == :level")
    suspend fun getBoard(level: Int): LevelScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(levelScore: LevelScore)

    @Query("DELETE FROM level_score WHERE id == :level")
    suspend fun deleteLevel(level: Int)

    @Query("DELETE FROM level_score")
    suspend fun deleteAll()
}