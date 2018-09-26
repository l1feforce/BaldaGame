package gusev.spbstu.org.baldagame.mvp.presenter

import android.app.Activity
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import gusev.spbstu.org.baldagame.MainContract
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.views.GameField
import gusev.spbstu.org.baldagame.mvp.views.NewGame
import kotlinx.android.synthetic.main.add_new_letter_dialog.view.*
import kotlinx.android.synthetic.main.fragment_new_game.view.*
import org.jetbrains.anko.*
import java.util.*

class Presenter() : MainContract.Presenter {
    lateinit var activity: Activity
    lateinit var fragmentActivity: FragmentActivity
    lateinit var fragmentView: View

    var timeToTurn = "120"


    constructor(view: Activity) : this() {
        this.activity = view
    }

    constructor(activity: DialogFragment, view: View) : this() {
        this.fragmentActivity = activity.activity as FragmentActivity
        this.fragmentView = view
    }

    override fun newGameIsClicked(supportFragmentManager: android.support.v4.app.FragmentManager) {
        val newGameFragment = NewGame()
        newGameFragment.show(supportFragmentManager, "newGame")
    }

    override fun settingsIsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exitIsClicked() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTimerIsClicked() {
        val options = listOf("1:00", "1:30", "2:00", "3:00", "5:00")
        fragmentActivity.selector(fragmentActivity.resources.getString(R.string.time_to_turn), options) { _, i ->
            timeToTurn = when (options[i]) {
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
        val input = fragmentActivity.javaClass.classLoader.getResourceAsStream("assets/singular.txt")
        val allWords = input.bufferedReader().use {
            it.readLines()
        }
        val filteredWords = allWords.filter { it.length == 5 }
        val random = Random().nextInt(filteredWords.size)
        val randomWord = filteredWords[random]
        fragmentView.mainWord.setText(randomWord)
    }

    override fun startNewGame() {
        if (fragmentView.mainWord?.text.toString().length != 5) fragmentActivity.longToast(R.string.wrong_word_length)
        else
            fragmentActivity.startActivity<GameField>("mainWord" to fragmentView.mainWord.text.toString(),
                    "firstPlayerName" to fragmentView.firstPlayer.text.toString(),
                    "secondPlayerName" to fragmentView.secondPlayer.text.toString(),
                    "timeToTurn" to timeToTurn)
    }

    override fun addNewLetter(cell: TextView) {
        val builder = AlertDialog.Builder(activity)
        val customView = activity.layoutInflater.inflate(R.layout.add_new_letter_dialog, null)
        builder.setView(customView)
                .setTitle(R.string.adding_letter_alert)
                .setPositiveButton("OK") { _, _ ->
                    if (customView.enteredWord.text.length != 1) {
                        activity.toast(R.string.new_letter_wrong_size_message)
                    }
                    else {
                        cell.text = customView.enteredWord.text.toString().toUpperCase()
                        cell.isClickable = false
                        (activity as GameField).addNewWord(cell)
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    cell.text = ""
                }
                .setCancelable(false)
        builder.create().show()
    }

    override fun addNewWord() {

    }

    override fun onDestroy() {
    }
}