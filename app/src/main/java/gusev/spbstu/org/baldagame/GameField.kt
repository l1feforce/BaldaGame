package gusev.spbstu.org.baldagame

import android.content.Context
import android.database.sqlite.SQLiteDatabase

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

    fun newTurn(position: Point, letter: Char, word: String) {
        newWord(position, word[word.length - 1])
        if (turn) firstPlayer.addScore(word.length)
        else secondPlayer.addScore(word.length)
        turn = !turn
    }

    fun dbChecking(word: String): Boolean {
        return database.contains(word)
    }

    fun staringWord(word: String) {
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