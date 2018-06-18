package gusev.spbstu.org.baldagame

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game_field_ui.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class GameFieldUI : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_field_ui)
        val textViews = listOf(listOf(cell00, cell01, cell02, cell03, cell04),
                listOf(cell10, cell11, cell12, cell13, cell14),
                listOf(cell20, cell21, cell22, cell23, cell24),
                listOf(cell30, cell31, cell32, cell33, cell34),
                listOf(cell40, cell41, cell42, cell43, cell44))
        val field = gamePreparing(textViews)
        fullScreenRefresh()
        addingNewLetter(field)
    }

    fun addingNewLetter(field: GameField) {
        gameField.forEachChild {
            val row = it as TableRow
            row.forEachChild {
                val textView = it as TextView
                if (textView.text.isBlank()) {
                    textView.setOnClickListener {
                        val text = it as TextView
                        field.lastLetter = text.text.toString()
                        alert("Enter letter") {
                            customView {
                                linearLayout {
                                    val letter = editText() {}.lparams {
                                        padding = dip(16)
                                        weight = 1f
                                        width = matchParent
                                    }
                                    padding = dip(16)
                                    positiveButton("OK") {
                                        if (letter.text.length == 1) {
                                            text.text = letter.text.toString().toUpperCase()
                                            field.lastLetter = letter.text.toString()
                                            field.lastLetterView = text
                                            fieldIsClickable(false)
                                            fullScreenRefresh()
                                            swipeTracking(field)
                                        } else toast("Enter one letter")
                                    }
                                    negativeButton("Cancel") {
                                        setFieldBackground()
                                        fullScreenRefresh()
                                    }
                                }
                            }
                        }.show()
                        it.setBackgroundColor(Color.parseColor("#DF0101"))
                    }
                }
            }
        }
    }


    fun swipeTracking(field: GameField) {
        val usedViews = mutableListOf<TextView>()
        coorsTable.setOnTouchListener { view, event ->
            gameField.forEachChild {
                val row = it as TableRow
                row.forEachChild {
                    val coor = IntArray(2)
                    it.getLocationOnScreen(coor)
                    if (event.x.toInt() > coor[0] && event.x.toInt() < coor[0] + it.width &&
                            event.y.toInt() > coor[1] && event.y.toInt() < coor[1] + it.height) {
                        it as TextView
                        if (!usedViews.contains(it)) field.word.append(it.text)
                        usedViews.add(it)
                        it.setBackgroundColor(Color.parseColor("#DF0101"))
                        if (event.action == MotionEvent.ACTION_UP && field.word.isNotBlank()) {
                            coorsTable.setOnTouchListener(null)
                            setFieldBackground()
                            if (usedViews.contains(field.lastLetterView)) {
                                afterPlayerTurn(field.newTurn(), field)
                            } else {
                                field.lastLetterView?.text = ""
                                field.lastLetterView?.isClickable = true
                                toast("You must use entered letter")
                            }
                            field.word = StringBuilder("")
                            usedViews.removeAll(usedViews)
                            addingNewLetter(field)
                        }
                    }
                }
            }
            true
        }
    }

    fun afterPlayerTurn(isTurnOkey: Boolean, field: GameField) {
        if (isTurnOkey) {
            longToast("New word added:\nFirstPlayer score:${field.firstPlayer.score}\n" +
                    "SecondPlayer score:${field.secondPlayer.score}" +
                    "\nYour word: ${field.word}")
            val newTextView = TextView(this)
            newTextView.leftPadding = dip(10)
            newTextView.topPadding = dip(5)
            val textViewText = field.word.toString() + "    ${field.word.length}"
            newTextView.text = textViewText
            if (!field.turn) firstPlayerScore.addView(newTextView)
            else secondPlayerScore.addView(newTextView)
            field.usedWords.add(field.word.toString().toLowerCase())
            scoreRefresh(field)
            winnerChecking(field)
        } else {
            field.lastLetterView?.text = ""
            toast("This word was used or don't exist" +
                    "\nYour word: ${field.word}")
        }
    }

    fun setFieldBackground() {
        gameField.forEachChild {
            val row = it as TableRow
            row.forEachChild {
                it.setBackgroundResource(R.drawable.my_border)
            }
        }
    }

    fun fieldIsClickable(flag: Boolean) {
        gameField.forEachChild {
            val row = it as TableRow
            row.forEachChild {
                it.isClickable = flag
            }
        }
    }

    fun gamePreparing(textViews: List<List<TextView>>): GameField {
        val firstPlayer = intent.getStringExtra("firstPlayerName") ?: ""
        val secondPlayer = intent.getStringExtra("secondPlayerName") ?: ""
        val mainWord: String = intent.getStringExtra("mainWord") ?: ""
        val allWords = application.assets.open("singular.txt").bufferedReader().use {
            it.readLines()
        }
        val field = GameField(Player(firstPlayer, 0), Player(secondPlayer, 0), true)
        field.usedWords.add(mainWord.toLowerCase())
        field.start(mainWord)
        field.database = allWords
        fieldRefresh(field, textViews)
        scoreRefresh(field)
        return field
    }

    fun fieldRefresh(field: GameField, textViews: List<List<TextView>>) {
        textViews.forEachIndexed { i, it ->
            it.forEachIndexed { k, textView ->
                textView.text = field.table[i][k].toString()
            }
        }
    }

    fun scoreRefresh(field: GameField) {
        firstPlayerName.text = "${field.firstPlayer.name} - ${field.firstPlayer.score}"
        secondPlayerName.text = "${field.secondPlayer.name} - ${field.secondPlayer.score}"
    }

    fun winnerChecking(field: GameField) {
        var isFieldFull = true
        gameField.forEachChild {
            it as TableRow
            it.forEachChild {
                it as TextView
                if (it.text.isBlank()) isFieldFull = false
            }
        }
        if (isFieldFull) {
            when {
                field.firstPlayer.score > field.secondPlayer.score -> gameFinish(field.firstPlayer)
                field.secondPlayer.score > field.firstPlayer.score -> gameFinish(field.secondPlayer)
                else -> gameFinish(null)
            }
        }
    }

    fun gameFinish(player: Player?) {
        if (player == null){
            alert("Drow! Want to play again?") {
                positiveButton("Yes") {
                    startNewGame()
                }
                negativeButton("No") {
                    System.exit(0)
                }
            }.show()
        }
        else {
            alert("${player.name} win! His score: ${player.score}" +
                    "\nWant to play againg?") {
                positiveButton("Yes") {
                    startNewGame()
                }
                negativeButton("No") {
                    System.exit(0)
                }
            }.show()
        }
    }

    fun startNewGame(){
        alert {
            title = "New game"
            customView {
                verticalLayout {
                    val firstPlayer = editText() {
                        setHint("First player name")
                    }.lparams(weight = 1f, width = matchParent)
                    val secondPlayer = editText() {
                        setHint("Second player name")
                    }.lparams(weight = 1f, width = matchParent)
                    val mainWord = editText() {
                        setHint("Enter main word")
                    }.lparams(weight = 1f, width = matchParent)
                    val okButton = button("OK") {
                        onClick {
                            if (mainWord.text.toString().length != 5) longToast("Error, enter word with lenght 5")
                            else startActivity<GameFieldUI>("mainWord" to mainWord.text.toString(),
                                    "firstPlayerName" to firstPlayer.text.toString(),
                                    "secondPlayerName" to secondPlayer.text.toString())
                        }
                    }.lparams(weight = 4f, width = matchParent)
                    padding = dip(16)

                }
            }
        }.show()
    }

    fun fullScreenRefresh() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}