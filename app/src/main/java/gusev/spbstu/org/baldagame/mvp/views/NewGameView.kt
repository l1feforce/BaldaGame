package gusev.spbstu.org.baldagame.mvp.views

import com.arellomobile.mvp.MvpView

interface NewGameView: MvpView {
    fun showTimeToTurnSelector()
    fun showToast()
    fun startNewGameActivity()
    fun setMainWord(word: String)

    fun setUpDifficultySpinner()
}