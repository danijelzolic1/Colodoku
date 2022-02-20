package se.zolda.coloredsudoku.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import se.zolda.coloredsudoku.game.model.ColorCell
import se.zolda.coloredsudoku.game.model.SudokuCell
import java.lang.reflect.Type
import java.util.*

class RoomTypeConverter {
    @TypeConverter
    fun fromCellList(list: List<SudokuCell>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<SudokuCell>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toCellList(json: String): List<SudokuCell> {
        val gson = Gson()
        val type = object : TypeToken<List<SudokuCell>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromColorCellList(list: List<ColorCell>): String {
        val gson = Gson()
        val type: Type = object : TypeToken<List<ColorCell>>() {}.type
        return gson.toJson(list, type)
    }

    @TypeConverter
    fun toColorCellList(json: String): List<ColorCell> {
        val gson = Gson()
        val type = object : TypeToken<List<ColorCell>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromDate(date: Date):Long{
        return date.time
    }

    @TypeConverter
    fun toDate(dateLong:Long):Date {
        return Date(dateLong)
    }
}