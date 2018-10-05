package gusev.spbstu.org.baldagame.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToUsedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.field
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.isThisWordOkay
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

    fun isThisCellsNeighbors(firstCell: String, secondCell: String): Boolean {
        return field.bfs(firstCell, secondCell) == 1
    }

}