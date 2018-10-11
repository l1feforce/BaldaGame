package gusev.spbstu.org.baldagame.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToUsedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.field
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.isThisWordOkay
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.listOfLetters
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.word
import gusev.spbstu.org.baldagame.mvp.model.Player
import gusev.spbstu.org.baldagame.mvp.views.GameFieldView

@InjectViewState
class GameFieldPresenter() : MvpPresenter<GameFieldView>() {

    lateinit var firstPlayer: Player
    lateinit var secondPlayer: Player
    var setOfWords: MutableList<String> = mutableListOf()

    fun addNewLetter() {
        viewState.showNewLetterDialog()
    }

    override fun onDestroy() {
    }

    fun turnIsMade() {
        if (isThisWordOkay(word)) {
            viewState.apply {
                showLongToastNewWordAdded()
                changeTurn()
                refreshScore()
                cleanEnteredWord()
            }
            addWordToUsedWords(word)
            viewState.winnerChecking()
            viewState.cancelTimerAndStartNew()
        } else {
            viewState.cancelTurn()
            viewState.showSnackCancelTurn()
        }
    }

    fun timeIsOver() {
        viewState.apply {
            showLongToastTimeIsOver()
            cleanBoardBeforeNextTurn()
            changeTurnWhenTimeIsEnd()
        }
    }

    fun getWordForTurn(): Map.Entry<String, Pair<String, String>>? {
        viewState.setLettersForFieldModel()
        val words = setLettersAndFindWords().toMutableMap()
        val newWords = mutableMapOf<String, Pair<String, String>>()
        words.forEach { if (isThisWordOkay(it.key)) newWords[it.key] = it.value }
        return newWords.maxBy { it.key.length }
    }

    private fun setLettersAndFindWords(): Map<String, Pair<String, String>> {
        //word - cellName - letter
        val mapOfWordCell = mutableMapOf<String, Pair<String, String>>()
        field.vertices.forEach { vertexName, vertex ->
            if (vertex.letter.isBlank()) {
                listOfLetters.forEach { newLetter ->
                    vertex.letter = newLetter
                    field.wordsSearch(vertexName).forEach { mapOfWordCell[it] = vertexName to newLetter }
                    vertex.letter = " "
                }
            }
        }
        return mapOfWordCell
    }

    fun isThisCellsNeighbors(firstCell: String, secondCell: String): Boolean {
        return field.bfs(firstCell, secondCell) == 1
    }

    fun makeBotTurn() {
        val wordToTurn = getWordForTurn()
        val letterNumber = wordToTurn!!.value.first.split("cell")[1][0].toString().toInt() * 5 +
                wordToTurn.value.first.split("cell")[1][1].toString().toInt() + 1
        viewState.setLetter(letterNumber, wordToTurn?.key)
        GameFieldModel.word = wordToTurn?.key!!
        turnIsMade()
    }
}