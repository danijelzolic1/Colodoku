package se.zolda.coloredsudoku.levels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import se.zolda.coloredsudoku.data.LevelScoreDao
import se.zolda.coloredsudoku.util.formatMillisHHmmss
import javax.inject.Inject

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val levelScoreDao: LevelScoreDao
): ViewModel() {

    fun test(){
        viewModelScope.launch(Dispatchers.IO){
            levelScoreDao.getAll()?.let { list ->
                list.forEach { score ->
                    Log.d("WTFWTF", "SCORE: ${score.id} = ${score.time.formatMillisHHmmss()}")
                }
            }
        }
    }
}