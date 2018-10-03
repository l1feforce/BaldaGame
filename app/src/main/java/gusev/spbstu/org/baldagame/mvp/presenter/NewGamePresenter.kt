package gusev.spbstu.org.baldagame.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import gusev.spbstu.org.baldagame.mvp.views.NewGameView
import java.util.*

@InjectViewState
class NewGamePresenter: MvpPresenter<NewGameView>() {


    fun setTimerIsClicked() {
        viewState.showSelector()
    }

    fun startNewGame(mainWord: String) {
        if (mainWord.length != 5) viewState.showToast()
        else viewState.startNewGameActivity()
    }

    fun randomWordIsClicked() {
        val input = viewState.javaClass.classLoader.getResourceAsStream("assets/singular.txt")
        val allWords = input.bufferedReader().use {
            it.readLines()
        }
        val filteredWords = allWords.filter { it.length == 5 }
        val random = Random().nextInt(filteredWords.size)
        val randomWord = filteredWords[random]
        viewState.setMainWord(randomWord)
    }

}