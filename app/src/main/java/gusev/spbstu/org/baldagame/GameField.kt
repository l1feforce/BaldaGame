package gusev.spbstu.org.baldagame


import android.app.Activity
import android.content.Context
import android.widget.TextView

import kotlinx.android.synthetic.main.game_field.*


class Player(val name: String, var score: Int) {
    init {
        score = 0
    }

    fun addScore(length: Int) {
        score += length
    }
}


class GameField(val firstPlayer: Player,
                val secondPlayer: Player, var turn: Boolean,val context: Context) {
    lateinit var table: List<List<TextView>>
    var database = listOf("")
    val usedWords = mutableListOf("")
    var lastLetterTextView: TextView? = null
    var word = StringBuilder("")
    var timeToTurn = 0L

    fun newTurn(isTimerEnd: Boolean): Boolean {
        if (!usedWordsChecking(word.toString()) && checkWordInDictionary(word.toString()) || isTimerEnd) {
            if (!usedWordsChecking(word.toString())) {
                if (turn) firstPlayer.addScore(word.length)
                else secondPlayer.addScore(word.length)
            }
            turn = !turn
            return true
        } else {
            return false
        }
    }

    fun addNewLetter(letter: String){
        lastLetterTextView?.text = letter.toUpperCase()
    }

    fun addWordToUsedWords(word: String) {
        usedWords.add(word.toLowerCase())
    }

    fun removeLetter(){
        lastLetterTextView?.text = ""
        lastLetterTextView?.isClickable = true
    }

    fun checkWordInDictionary(word: String): Boolean {
        return database.contains(word.toLowerCase())
    }

    private fun usedWordsChecking(word: String): Boolean {
        return usedWords.contains(word.toLowerCase())
    }

    fun tableInit(){
        context as Activity
        table = listOf(listOf(context.cell00, context.cell01, context.cell02, context.cell03, context.cell04),
                listOf(context.cell10, context.cell11, context.cell12, context.cell13, context.cell14),
                listOf(context.cell20, context.cell21, context.cell22, context.cell23, context.cell24),
                listOf(context.cell30, context.cell31, context.cell32, context.cell33, context.cell34),
                listOf(context.cell40, context.cell41, context.cell42, context.cell43, context.cell44))
    }

    fun start(word: String) {
        table[2].forEachIndexed { index, it ->
          it.text = word[index].toString().toUpperCase()
        }
    }
}
