package gusev.spbstu.org.baldagame.mvp.presenter

import android.app.Activity
import android.app.FragmentManager
import android.content.Context
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.View
import gusev.spbstu.org.baldagame.MainContract
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.activities.GameFieldUI
import gusev.spbstu.org.baldagame.mvp.views.GameField
import gusev.spbstu.org.baldagame.mvp.views.NewGame
import kotlinx.android.synthetic.main.fragment_new_game.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.selector

class Presenter(): MainContract.Presenter {
    lateinit var view: Activity
    lateinit var fragmentView: DialogFragment
    constructor(view: Activity) : this() {
        this.view = view
    }

    constructor(fragmentView: DialogFragment) : this() {
        this.fragmentView = fragmentView
    }

    override fun newGameIsClicked(supportFragmentManager: android.support.v4.app.FragmentManager) {
        NewGame().show(supportFragmentManager, "newGame")
    }

    override fun settingsIsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitIsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTimerIsClicked() {
        var timeToTurn = "120"
        val options = listOf("1:00", "1:30", "2:00", "3:00", "5:00")
        view.selector(view.resources.getString(R.string.time_to_turn), options) { _, i ->
            timeToTurn = when(options[i]) {
                options[0] -> "60"
                options[1] -> "90"
                options[2] -> "120"
                options[3] -> "180"
                options[4] -> "300"
                else -> "120"
            }
        }
    }

    override fun randomWordIsClicked() {

    }

    override fun startNewGame() {
        /*if (view.mainWord?.text.toString().length != 5) view.longToast(R.string.wrong_word_length)
        else
            view.startActivity<GameField>("mainWord" to view.mainWord.text.toString(),
                    "firstPlayerName" to view.firstPlayer.text.toString(),
                    "secondPlayerName" to view.secondPlayer.text.toString(),
                    "timeToTurn" to view.timeToTurn) */
    }

    override fun addNewLetter() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addNewWord() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}