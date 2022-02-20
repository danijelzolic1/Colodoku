package se.zolda.coloredsudoku.util

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "com.zolda.preferences"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    private val SPAN_COUNT = Pair("span_count", 9)
    private val LEVEL = Pair("level", 1)
    private val TIMER = Pair("timer", 0L)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var timer: Long
        get() = preferences.getLong(TIMER.first, TIMER.second)
        set(value) = preferences.edit {
            it.putLong(TIMER.first, value)
        }

    var currentLevel: Int
        get() = preferences.getInt(LEVEL.first, LEVEL.second)
        set(value) = preferences.edit {
            it.putInt(LEVEL.first, value)
        }

    var spanCount: Int
        get() = preferences.getInt(SPAN_COUNT.first, SPAN_COUNT.second)
        set(value) = preferences.edit {
            it.putInt(SPAN_COUNT.first, value)
        }
}