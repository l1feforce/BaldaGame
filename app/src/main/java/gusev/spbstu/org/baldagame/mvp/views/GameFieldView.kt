package gusev.spbstu.org.baldagame.mvp.views

import com.arellomobile.mvp.MvpView
import gusev.spbstu.org.baldagame.mvp.model.Player

interface GameFieldView : MvpView {

    fun addNewLetter()


    fun prepareGameFieldForStart(firstPlayerName: String, secondPlayerName: String,
                                 mainWord: String, timeToTurn: String)

    fun makeFullScreen()
    fun makeFieldNotClickable()
    fun setDefaultTextColor()
    fun setFirstPlayerTurn()
    fun setSecondPlayerTurn()
    fun showLongToastNewWordAdded()
    fun refreshScore()
    fun showSnackCancelTurn()
    fun cleanEnteredWord()
    fun startTimer(timeToTurnInMillis: Long)
    fun showLongToastTimeIsOver()
    fun changeTurn()
    fun addNewWord()
    fun cancelTurn()
    fun showNewLetterDialog()
    fun cleanBoardBeforeNextTurn()
    fun cancelTimerAndStartNew()
    fun changeTurnWhenTimeIsEnd()
    fun winningDialog(player: Player?)
    fun winnerChecking()
    fun setLettersForFieldModel()
    fun setLetter(letterNumber: Int, letter: String?)
    fun addWordToScrollView()
    fun skipTurnWhenTimeIsOver()
    fun resumeAfterPause()
}