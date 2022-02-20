package se.zolda.coloredsudoku.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import se.zolda.coloredsudoku.data.model.LevelScore
import se.zolda.coloredsudoku.data.model.SudokuBoard

@Database(entities = [SudokuBoard::class, LevelScore::class], version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sudokuDao() : SudokuDao
    abstract fun levelScoreDao() : LevelScoreDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "zolda_coloku")
                .fallbackToDestructiveMigration()
                .build()
    }

}