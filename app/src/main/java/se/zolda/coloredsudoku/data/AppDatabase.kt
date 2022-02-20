package se.zolda.coloredsudoku.data

import android.content.Context
import androidx.room.*
import se.zolda.coloredsudoku.game.model.SudokuBoard

@Database(entities = [SudokuBoard::class], version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sudokuDao() : SudokuDao

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