package se.zolda.coloredsudoku.game.viewmodel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import se.zolda.coloredsudoku.R
import se.zolda.coloredsudoku.data.LevelScoreDao
import se.zolda.coloredsudoku.data.SudokuDao
import se.zolda.coloredsudoku.data.model.*
import se.zolda.coloredsudoku.game.ColorEnum
import se.zolda.coloredsudoku.util.*
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val sudokuDao: SudokuDao,
    private val levelScoreDao: LevelScoreDao
) : ViewModel(), SudokuGridListener, ColorGridListener {
    //private var spanCount = AppPreferences.spanCount
    //private val numberOfColRow = GridUtil.getNumberOfColumnsAndRows(spanCount)
    private val _noteSelected = MutableLiveData(false)
    val noteSelected: LiveData<Boolean> get() = _noteSelected
    private val _board = MutableLiveData<SudokuBoard>()
    val board: LiveData<SudokuBoard> get() = _board

    init {
        initGame()
    }

    private fun initGame() {
        viewModelScope.launch(Dispatchers.IO) {
            //sudokuDao.deleteAll() //TODO used for testing
            sudokuDao.getBoard()?.let {
                it.cells.forEach { cell ->
                    cell.color = getColorForValue(cell.value)
                }
                it.colors.forEach { color ->
                    color.color =
                        if (color.isErase) R.color.sudoku_erase else getColorForValue(color.id)!!
                }
                updateBoard(it)
            } ?: kotlin.run {
                createNewGame()
            }
        }
    }

    fun newGame(){
        viewModelScope.launch(Dispatchers.IO){
            createNewGame()
        }
    }

    private suspend fun createNewGame() {
        val span = AppPreferences.spanCount
        val generatedBoards = Generator(span).build()
        val solvedBoard = generatedBoards.first
        val gameBoard = generatedBoards.second
        val items = mutableListOf<SudokuCell>()
        val solvedItems = mutableListOf<SudokuCell>()
        for (i in 0 until span) {
            for (j in 0 until span) {
                val value = gameBoard[i][j]
                val solvedValue = solvedBoard[i][j]
                items.add(
                    SudokuCell(
                        id = i * span + j,
                        getColorForValue(value),
                        column = i,
                        row = j,
                        value = value,
                        notes = listOf(),
                        canEdit = value == 0
                    )
                )
                solvedItems.add(
                    SudokuCell(
                        value = solvedValue,
                    )
                )
            }
        }
        val colors = mutableListOf<ColorCell>()
        ColorEnum.values().forEach { color ->
            colors.add(
                ColorCell(
                    color.numberValue,
                    getColorForValue(color.numberValue)!!
                )
            )
        }
        colors.add(
            ColorCell(
                0,
                R.color.sudoku_erase,
                true
            )
        )
        val sudokuBoard = SudokuBoard(
            id = 1,
            cells = items,
            colors = colors,
            sudokuBoardState = SudokuBoardState.PLAYING
        )
        val solvedSudokuBoard = SudokuBoard(
            id = 0,
            cells = solvedItems
        )
        sudokuDao.insert(sudokuBoard)
        sudokuDao.insert(solvedSudokuBoard)
        updateBoard(sudokuBoard)
    }

    private suspend fun checkSolution(gameBoard: SudokuBoard) {
        gameBoard.cells.filter { it.value == 0 }.let { list ->
            if (list.isNotEmpty()) {
                sudokuDao.insert(gameBoard)
                return
            }
        }
        sudokuDao.getSolvedBoard()?.let { solvedBoard ->
            gameBoard.cells.mapIndexed { index, sudokuCell ->
                val solved = solvedBoard.cells[index]
                if (solved.value != sudokuCell.value) {
                    sudokuDao.insert(gameBoard)
                    return
                }
            }
            onLevelCompleted(gameBoard.copy(sudokuBoardState = SudokuBoardState.SOLVED))
        }
    }

    private suspend fun saveScore(){
        val level = AppPreferences.currentLevel
        val time = AppPreferences.timer
        val score = LevelScore(
            id = level,
            time = time
        )
        levelScoreDao.getBoard(AppPreferences.currentLevel)?.let {
            levelScoreDao.deleteLevel(AppPreferences.currentLevel)
        }
        levelScoreDao.insert(score)
    }

    private suspend fun onLevelCompleted(gameBoard: SudokuBoard){
        saveScore()
        AppPreferences.currentLevel = AppPreferences.currentLevel + 1
        AppPreferences.timer = 0
        sudokuDao.deleteAll()
        updateBoard(gameBoard)
    }

    fun updateTime(time: Long){
        viewModelScope.launch(Dispatchers.IO){
            AppPreferences.timer = SystemClock.elapsedRealtime() - time
        }
    }

    fun restartLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            _board.value?.let { gameBoard ->
                val mutableList = gameBoard.cells.toMutableList()
                gameBoard.cells.filter { it.canEdit }.let { list ->
                    list.forEach {
                        mutableList[gameBoard.cells.indexOf(it)] = it.copy(
                            value = 0,
                            color = null,
                            notes = listOf(),
                            isSelected = false
                        )
                    }
                }
                gameBoard.copy(cells = mutableList).let { copied ->
                    sudokuDao.deleteBoard()
                    sudokuDao.insert(copied)
                    updateBoard(copied)
                }
            }
        }
    }

    private suspend fun updateBoard(gameBoard: SudokuBoard) = withContext(Dispatchers.Main){
        _board.value = gameBoard
    }

    private var toggleJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun onToggleSelectedCell(sudokuCell: SudokuCell) {
        viewModelScope.launch(Dispatchers.IO) {
            _board.value?.let { gameBoard ->
                val mutableList = gameBoard.cells.toMutableList()
                gameBoard.cells.find { it.isSelected }?.let { cell ->
                    val newCell = cell.copy(
                        isSelected = false
                    )
                    mutableList[gameBoard.cells.indexOf(cell)] = newCell
                }
                gameBoard.cells.find { it.id == sudokuCell.id }?.let { cell ->
                    val newCell = cell.copy(
                        isSelected = !cell.isSelected
                    )
                    mutableList[gameBoard.cells.indexOf(cell)] = newCell
                }

                updateBoard(gameBoard.copy(cells = mutableList))
            }
        }.also { toggleJob = it }
    }

    fun toggleNoteSelected() {
        _noteSelected.value = !(_noteSelected.value ?: true)
    }

    private var toggleColorJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun onColorClicked(colorCell: ColorCell) {
        viewModelScope.launch(Dispatchers.IO) {
            _board.value?.let { gameBoard ->
                gameBoard.cells.find { it.isSelected }?.let { selected ->
                    val mutableList = gameBoard.cells.toMutableList()
                    var checkSolution = false
                    val newCell: SudokuCell
                    if (colorCell.isErase) {
                        newCell = selected.copy(
                            notes = listOf(),
                            color = null,
                            value = 0
                        )
                    } else {
                        when (_noteSelected.value) {
                            true -> {
                                newCell = if (selected.notes.contains(colorCell.color)) {
                                    selected.copy(
                                        notes = selected.notes.filter { it != colorCell.color },
                                        color = null,
                                        value = 0
                                    )
                                } else {
                                    selected.copy(
                                        notes = selected.notes.toMutableList().apply {
                                            add(colorCell.color)
                                        },
                                        color = null,
                                        value = 0
                                    )
                                }
                            }
                            else -> {
                                if(selected.value == colorCell.id){
                                    newCell = selected.copy(
                                        notes = listOf(),
                                        color = null,
                                        value = 0
                                    )
                                }else {
                                    checkSolution = true
                                    newCell = selected.copy(
                                        notes = listOf(),
                                        color = colorCell.color,
                                        value = colorCell.id
                                    )
                                }
                            }
                        }

                    }
                    mutableList[gameBoard.cells.indexOf(selected)] = newCell
                    gameBoard.copy(cells = mutableList).let { copied ->
                        if(checkSolution) checkSolution(copied)
                        updateBoard(copied)
                    }

                }
            }
        }.also { toggleColorJob = it }
    }
}

interface SudokuGridListener {
    fun onToggleSelectedCell(sudokuCell: SudokuCell)
}

interface ColorGridListener {
    fun onColorClicked(colorCell: ColorCell)
}