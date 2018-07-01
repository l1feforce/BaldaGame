package gusev.spbstu.org.baldagame

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_game_field_ui.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class GameFieldUI : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    lateinit var field: GameField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_field_ui)
        val textViews = listOf(listOf(cell00, cell01, cell02, cell03, cell04),
                listOf(cell10, cell11, cell12, cell13, cell14),
                listOf(cell20, cell21, cell22, cell23, cell24),
                listOf(cell30, cell31, cell32, cell33, cell34),
                listOf(cell40, cell41, cell42, cell43, cell44))
        gamePreparing(textViews)
        makeFullScreen()
        addingNewLetter()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onResume() {
        super.onResume()
        makeFullScreen()
        if (timeRemains.text.toString() != "123") {
            timer.cancel()
            val millisRemains = timeRemains.text.split(":")
            val seconds = millisRemains[1].toLong() * 1000
            val minutes = millisRemains[0].toLong() * 60 * 1000
            timer = startTimer(seconds + minutes)
            timer.start()
        }
    }

    fun startTimer(millisRemains: Long): CountDownTimer {
        timer = object : CountDownTimer(millisRemains, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished + 1000) / 1000 % 60
                val minutes = (millisUntilFinished + 1000) / 1000 / 60
                timeRemains.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timeIsOver()
                timer.cancel()
                timer = startTimer(field.timeToTurn * 1000).start()
                addingNewLetter()
            }

        }
        return timer
    }

    fun addingNewLetter() {
        makeFullScreen()
        gameField.forEachChild {
            it as TableRow
            it.forEachChild {
                it as TextView
                if (it.text.isBlank()) {
                    it.setOnClickListener {
                        it as TextView
                        field.lastLetterTextView = it
                        alert(R.string.adding_letter_alert) {
                            customView {
                                linearLayout {
                                    val editTextForLetter = editText() {}.lparams {
                                        padding = dip(16)
                                        weight = 1f
                                        width = matchParent
                                    }
                                    padding = dip(16)
                                    positiveButton("OK") {
                                        if (editTextForLetter.text.length == 1) {
                                            field.addNewLetter(editTextForLetter.text.toString())
                                            fieldIsClickable(false)
                                            swipeTracking()
                                        } else toast(R.string.new_letter_wrong_size_message)
                                        makeFullScreen()
                                    }
                                    negativeButton(R.string.negative_button_alert) {
                                        setDefaultFieldBackground()
                                        makeFullScreen()
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


    fun swipeTracking() {
        val usedViews = mutableListOf<TextView>()
        coorsTable.setOnTouchListener { view, event ->
            gameField.forEachChild {
                val row = it as TableRow
                row.forEachChild {
                    val viewCoordinates = IntArray(2)
                    it.getLocationOnScreen(viewCoordinates)
                    if (event.x.toInt() > viewCoordinates[0] && event.x.toInt() < viewCoordinates[0] + it.width &&
                            event.y.toInt() > viewCoordinates[1] && event.y.toInt() < viewCoordinates[1] + it.height) {
                        it as TextView
                        if (!usedViews.contains(it)) field.word.append(it.text)
                        usedViews.add(it)
                        it.setBackgroundColor(Color.parseColor("#DF0101"))
                        if (event.action == MotionEvent.ACTION_UP) {
                            coorsTable.setOnTouchListener(null)
                            setDefaultFieldBackground()
                            if (usedViews.contains(field.lastLetterTextView)) {
                                afterPlayerTurn(field.newTurn(false))
                            } else {
                                field.lastLetterTextView?.text = ""
                                field.lastLetterTextView?.isClickable = true
                                toast(R.string.must_use_new_letter_message)
                            }
                            field.word = StringBuilder("")
                            usedViews.removeAll(usedViews)
                            addingNewLetter()
                        }
                    }
                }
            }
            true
        }
    }

    fun afterPlayerTurn(isTurnOkay: Boolean) {
        if (isTurnOkay) {
            longToast("${resources.getString(R.string.new_word_added_message)}: ${field.word}")
            addingNewWordToScore(false)
            if (!field.turn) {
                firstPlayerName.setBackgroundResource(R.drawable.my_border)
                secondPlayerName.setBackgroundColor(Color.parseColor("#DF0101"))
            } else {
                firstPlayerName.setBackgroundColor(Color.parseColor("#DF0101"))
                secondPlayerName.setBackgroundResource(R.drawable.my_border)
            }
            field.usedWords.add(field.word.toString().toLowerCase())
            scoreRefresh()
            timer.cancel()
            timer = startTimer(field.timeToTurn * 1000).start()
            winnerChecking()
        } else {
            field.lastLetterTextView?.text = ""
            //toast(resources.getString(R.string.wrong_word_message)+": ${field.word}")
            coorsTable.snack(resources.getString(R.string.wrong_word_message)+": ${field.word}") {
                action("Add word") {
                    try {
                        val outputStream: FileOutputStream = openFileOutput("assets/singular.txt", 0)
                        val writer = OutputStreamWriter(outputStream)
                        writer.write("\nбаа")
                        writer.close()
                    }
                    catch (e: Exception) {
                        toast("${e.javaClass}")
                    }
                }
            }
        }
        field.lastLetterTextView = null
    }

    fun addingNewWordToScore(isTimerEnd: Boolean){
        val newTextView = TextView(this)
        newTextView.leftPadding = dip(10)
        newTextView.topPadding = dip(2)
        newTextView.text = if (!isTimerEnd) field.word.toString() + "    ${field.word.length}"
        else "-"
        if (!field.turn && !isTimerEnd) firstPlayerWords.addView(newTextView)
        else secondPlayerWords.addView(newTextView)
    }

    fun timeIsOver() {
        longToast(R.string.time_is_over)
        addingNewWordToScore(true)
        field.newTurn(true)
        if (!field.turn) {
            firstPlayerName.setBackgroundResource(R.drawable.my_border)
            secondPlayerName.setBackgroundColor(Color.parseColor("#DF0101"))
        } else {
            firstPlayerName.setBackgroundColor(Color.parseColor("#DF0101"))
            secondPlayerName.setBackgroundResource(R.drawable.my_border)
        }
        scoreRefresh()
        coorsTable.setOnTouchListener(null)
        setDefaultFieldBackground()
        field.lastLetterTextView?.text = ""
        field.lastLetterTextView?.isClickable = true
    }

    fun setDefaultFieldBackground() {
        gameField.forEachChild {
            val row = it as TableRow
            row.forEachChild {
                it.setBackgroundResource(R.drawable.my_border)
            }
        }
    }

    fun fieldIsClickable(flag: Boolean) {
        gameField.forEachChild {
            it as TableRow
            it.forEachChild {
                it.isClickable = flag
            }
        }
    }

    fun gamePreparing(textViews: List<List<TextView>>) {
        val firstPlayer = intent.getStringExtra("firstPlayerName") ?: ""
        val secondPlayer = intent.getStringExtra("secondPlayerName") ?: ""
        val mainWord: String = intent.getStringExtra("mainWord") ?: ""
        val timeToTurn = intent.getStringExtra("timeToTurn") ?: ""
        val allWords = application.assets.open("singular.txt").bufferedReader().use {
            it.readLines()
        }
        field = GameField(Player(firstPlayer, 0), Player(secondPlayer, 0), true,this)
        field.usedWords.add(mainWord.toLowerCase())
        field.tableInit()
        field.start(mainWord)
        field.timeToTurn = timeToTurn.toLong()
        field.database = allWords
        timer = startTimer(field.timeToTurn * 1000).start()
        scoreRefresh()
        firstPlayerName.text = firstPlayer
        firstPlayerName.setBackgroundColor(Color.parseColor("#DF0101"))
        secondPlayerName.text = secondPlayer
    }


    fun scoreRefresh() {
        firstPlayerScore.text = field.firstPlayer.score.toString()
        secondPlayerScore.text = field.secondPlayer.score.toString()
    }

    fun winnerChecking() {
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
        if (player == null) {
            alert("${R.string.draw}\n${R.string.want_to_play_again}") {
                positiveButton(R.string.yes) {
                    startNewGame()
                }
                negativeButton(R.string.no) {
                    System.exit(0)
                }
            }.show()
        } else {
            alert("${player.name}${R.string.winner_message}${player.score}" +
                    "\n${R.string.want_to_play_again}") {
                positiveButton(R.string.yes) {
                    startNewGame()
                }
                negativeButton(R.string.no) {
                    System.exit(0)
                }
            }.show()
        }
    }

    fun startNewGame() {
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

    fun makeFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}
