package gusev.spbstu.org.baldagame

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import gusev.spbstu.org.baldagame.R.id.*
import kotlinx.android.synthetic.main.activity_game_field_ui.*
import org.jetbrains.anko.*
import org.w3c.dom.Text


class Point(val x: Int, val y: Int)

class Player(val name: String, var score: Int) {
    init {
        score = 0
    }

    fun addScore(length: Int) {
        score += length
    }
}


data class GameField(val firstPlayer: Player,
                     val secondPlayer: Player, var turn: Boolean) {
    var table = mutableListOf("     ", "     ", "     ", "     ", "     ")
    var database = listOf("")
    val usedWords = mutableListOf("")
    var lastLetter = ""
    var lastLetterView: TextView? = null
    var word = StringBuilder("")

    fun newTurn(): Boolean {
        if (!usedWordsChecking(word.toString()) && dbChecking(word.toString())) {
            if (turn) firstPlayer.addScore(word.length)
            else secondPlayer.addScore(word.length)
            turn = !turn
            return true
        } else {
            return false
        }
    }


    fun dbChecking(word: String): Boolean {
        return database.contains(word.toLowerCase())
    }

    fun usedWordsChecking(word: String): Boolean {
        return usedWords.contains(word.toLowerCase())
    }

    fun start(word: String) {
        table[2] = word.toUpperCase()
    }
}
