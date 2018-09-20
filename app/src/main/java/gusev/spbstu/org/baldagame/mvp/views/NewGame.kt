package gusev.spbstu.org.baldagame.mvp.views


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.View
import gusev.spbstu.org.baldagame.MainContract
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.activities.GameFieldUI
import gusev.spbstu.org.baldagame.mvp.presenter.Presenter
import kotlinx.android.synthetic.main.fragment_new_game.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startActivity
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class NewGame : DialogFragment() {
    lateinit var presenter: Presenter

    fun randomWord(): String {
        val input = this.javaClass.classLoader.getResourceAsStream("assets/singular.txt")
        val allWords = input.bufferedReader().use {
            it.readLines()
        }
        val filteredWords = allWords.filter { it.length == 5 }
        val random = Random().nextInt(filteredWords.size)
        return filteredWords[random]
    }

    fun startNewGame() {
        /*
        if (mainWord?.text.toString().length != 5) longToast(R.string.wrong_word_length)
        else
            startActivity<GameFieldUI>("mainWord" to mainWord.text.toString(),
                    "firstPlayerName" to firstPlayer.text.toString(),
                    "secondPlayerName" to secondPlayer.text.toString(),
                    "timeToTurn" to timeToTurn)
                    */
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        presenter = Presenter(this)
        val dialog = AlertDialog.Builder(activity)
                .setView(layoutInflater.inflate(R.layout.fragment_new_game, null))
                .setTitle(R.string.new_game)
                .setPositiveButton("OK") { _, _ -> presenter.startNewGame() }
        return dialog.create()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTimer.setOnClickListener {

        }

        randomWord.setOnClickListener {
            val randomWord = randomWord()
            mainWord.setText(randomWord)
        }


    }


}
