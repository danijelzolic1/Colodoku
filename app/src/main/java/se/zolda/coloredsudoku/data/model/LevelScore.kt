package se.zolda.coloredsudoku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "level_score")
data class LevelScore(
    @PrimaryKey val id: Int = 0,
    val time: Long = 0
)