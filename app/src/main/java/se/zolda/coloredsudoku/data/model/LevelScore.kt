package se.zolda.coloredsudoku.data.model

import androidx.room.PrimaryKey

data class LevelScore(
    @PrimaryKey val id: Int = 0,
    val time: Long = 0
)