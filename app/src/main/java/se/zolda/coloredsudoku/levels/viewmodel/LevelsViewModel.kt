package se.zolda.coloredsudoku.levels.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import se.zolda.coloredsudoku.data.LevelScoreDao
import se.zolda.coloredsudoku.data.model.LevelScore
import se.zolda.coloredsudoku.util.AppPreferences
import se.zolda.coloredsudoku.util.Constants
import se.zolda.coloredsudoku.util.formatMillisHHmmss
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LevelsViewModel @Inject constructor(
    private val levelScoreDao: LevelScoreDao
): ViewModel(), LevelsListener {

    private val _levels = levelScoreDao.all()
    val levels: LiveData<List<LevelScore>> get() = _levels

    /*val levels : MediatorLiveData<List<LevelScore>> = MediatorLiveData<List<LevelScore>>().also {
        it.addSource(_levels){ list ->
            viewModelScope.launch(Dispatchers.IO){
                it.postValue(fillRemainingCells(list))
            }
        }
    }
    private fun fillRemainingCells(list: List<LevelScore>): List<LevelScore>{
        if(list.size == Constants.MAX_LEVEL) return list
        return list.toMutableList().let { mutableList ->
            for (i in (if(list.isEmpty()) 1 else list.size+1)..Constants.MAX_LEVEL){
                mutableList.add(
                    LevelScore(
                        id = i,
                        time = i * Random.nextLong(10000, 60000),
                        locked = false
                    )
                )
            }
            mutableList
        }
    }*/

    override fun onLevelClicked(level: Int) {

    }
}

interface LevelsListener{
    fun onLevelClicked(level: Int)
}