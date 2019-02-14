package gusev.spbstu.org.baldagame.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.BotDifficulty
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
            viewState.addWordToScrollView()
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
            skipTurnWhenTimeIsOver()
        }
    }

    private fun getWordForTurn(botDifficulty: BotDifficulty): Map.Entry<String, Pair<String, String>>? {
        viewState.setLettersForFieldModel()
        val words = setLettersAndFindWords().toMutableMap()
        val newWords = mutableMapOf<String, Pair<String, String>>()
        words.forEach { if (isThisWordOkay(it.key)) newWords[it.key] = it.value }

        return when (botDifficulty) {
            BotDifficulty.HARD -> newWords.maxBy { it.key.length }
            BotDifficulty.MEDIUM -> {
                if (firstPlayer.score >= secondPlayer.score + 5) {
                    newWords.filter { it.key.length <= 5 }.maxBy { it.key.length }
                } else newWords.filter { it.key.length <= 4 }.maxBy { it.key.length }
            }
            BotDifficulty.EASY -> {
                if (firstPlayer.score >= secondPlayer.score + 4) {
                    newWords.filter { it.key.length <= 4 }.maxBy { it.key.length }
                } else newWords.filter { it.key.length <= 3 }.maxBy { it.key.length }
            }
        }
    }

    private fun setLettersAndFindWords(): Map<String, Pair<String, String>> {
        //word - cellName - letter
        val mapOfWordCell = mutableMapOf<String, Pair<String, String>>()
        field.vertices.forEach { vertexName, vertex ->
            if (vertex.letter.isBlank()) {
                listOfLetters.forEach { newLetter ->
                    vertex.letter = newLetter
                    field.wordsSearch(vertexName).forEach {
                        mapOfWordCell[it] = vertexName to newLetter
                        println("word is: $it")
                        println("letterCell is: $vertexName")
                    }
                    vertex.letter = " "
                }
            }
        }
        return mapOfWordCell
    }

    fun isThisCellsNeighbors(firstCell: String, secondCell: String): Boolean {
        return field.bfs(firstCell, secondCell) == 1
    }

    fun makeBotTurn(botDifficulty: BotDifficulty) {
        val wordToTurn = getWordForTurn(botDifficulty)
        val letterNumber = wordToTurn!!.value.first[4].toString().toInt() * 5 +
                wordToTurn.value.first[5].toString().toInt()
        viewState.setLetter(letterNumber, wordToTurn.value.second)
        word = wordToTurn.key
        turnIsMade()
        viewState.addNewLetter()
    }
}