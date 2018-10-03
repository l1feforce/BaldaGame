package gusev.spbstu.org.baldagame.mvp.views

import com.arellomobile.mvp.MvpView

interface NewGameView: MvpView {
    fun showSelector()
    fun showToast()
    fun startNewGameActivity()
    fun setMainWord(randomWord: String)

}