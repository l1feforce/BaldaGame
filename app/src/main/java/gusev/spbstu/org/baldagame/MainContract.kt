package gusev.spbstu.org.baldagame

import android.widget.TextView
import org.w3c.dom.Text

interface MainContract {
    interface View {

    }

    interface MainMenuPresenter {
        fun newGameIsClicked()

        fun settingsIsClicked()

        fun exitIsClicked()
    }

    interface NewGameDialogPresenter {

    }

    interface Presenter {

        fun setTimerIsClicked()

        fun randomWordIsClicked()

        fun newGameIsClicked(supportFragmentManager: android.support.v4.app.FragmentManager)

        fun settingsIsClicked()

        fun exitIsClicked()

        fun startNewGame()

        fun onDestroy()

        fun addNewLetter(cell: TextView)

        fun addNewWord()


    }

    interface Model {
        fun newTurn(isTimerEnd: Boolean): Boolean

        fun addNewLetter(letter: String)

        fun addWordToUsedWords(word: String)

        fun removeLastLetter()

        fun checkWordInDictionary(word: String): Boolean

        fun isItUsedWord(word: String): Boolean


    }
}