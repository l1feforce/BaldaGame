package gusev.spbstu.org.baldagame

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.widget.TableLayout
import org.jetbrains.anko.*


class Point(val x: Int, val y: Int)

class Player(val name: String, var score: Int) {
    fun addScore(length: Int) {
        score += length
    }
}


data class GameField(val firstPlayer: Player,
                     val secondPlayer: Player, var turn: Boolean) {
    var table = mutableListOf("     ", "     ", "     ", "     ", "     ")
    var database = listOf("")
    var usedWords = listOf("")

    fun newTurn(position: Point, letter: Char, word: String): Boolean {
        if (!usedWords.contains(word)) {
            newWord(position, word[word.length - 1])
            if (turn) firstPlayer.addScore(word.length)
            else secondPlayer.addScore(word.length)
            turn = !turn
            return true
        }
        else return false
    }


    fun dbChecking(word: String): Boolean {
        return database.contains(word)
    }

    fun start(word: String) {
        table[2] = word
    }

    fun newWord(position: Point, letter: Char): MutableList<String> {
        table[position.x] = table[position.x].mapIndexed { index, it ->
            if (index == position.y) letter
            else it
        }.joinToString("")
        return table
    }

    fun whichPlayerWin(): Player = if (firstPlayer.score > secondPlayer.score) firstPlayer else secondPlayer

}