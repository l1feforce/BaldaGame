package gusev.spbstu.org.baldagame.ui

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.transition.Fade
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.view.get
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.transitionseverywhere.ChangeText
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.action
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToDictionary
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToUsedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.field
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.itWasFirstPlayerTurn
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.usedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.word
import gusev.spbstu.org.baldagame.mvp.model.Player
import gusev.spbstu.org.baldagame.mvp.presenter.GameFieldPresenter
import gusev.spbstu.org.baldagame.mvp.views.GameFieldView
import gusev.spbstu.org.baldagame.snack
import kotlinx.android.synthetic.main.add_new_letter_dialog.view.*
import kotlinx.android.synthetic.main.game_field.*
import org.jetbrains.anko.*
import kotlin.collections.set

class GameFieldActivity : MvpAppCompatActivity(), GameFieldView {


    @InjectPresenter
    lateinit var presenter: GameFieldPresenter

    var timer: CountDownTimer? = null
    var selectedCell: TextView? = null
    private var count = 0
    var listOfCellsCoors = mutableMapOf<TextView, String>()
    var botGame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_field)
        makeFullScreen()
        prepareGameFieldForStart(intent.getStringExtra("firstPlayerName") ?: "",
                intent.getStringExtra("secondPlayerName") ?: "",
                intent.getStringExtra("mainWord") ?: "балда",
                intent.getStringExtra("timeToTurn") ?: "")


        botGame = intent.extras.getBoolean("botGame")
        addNewLetter()
    }


    override fun onBackPressed() {
        count++
        if (count == 1) toast(R.string.press_back_again)
        else {
            finish()
            moveTaskToBack(true)
        }
    }

    override fun addNewLetter() {
        if (botGame && !itWasFirstPlayerTurn) {
            presenter.makeBotTurn()
        } else {
            gameField.forEachChild {
                it as TextView
                if (it.text.toString().isBlank()) {
                    it.setOnClickListener { _ ->
                        selectedCell = it
                        presenter.addNewLetter()
                    }
                } else it.setOnClickListener(null)
            }
        }
    }

    private fun findNearestCell(event: MotionEvent): TextView {
        var min = cell00
        gameField.forEachChild {
            val viewCoordinates = IntArray(2)
            it.getLocationOnScreen(viewCoordinates)
            listOfCellsCoors[it as TextView] = "${viewCoordinates[0]}:${viewCoordinates[1]}"
        }
        listOfCellsCoors.forEach { textView, s ->
            if (event.x.toInt() > s.split(":")[0].toInt() + 10 && event.x.toInt() < s.split(":")[0].toInt() + textView.width - 10 &&
                    event.y.toInt() > s.split(":")[1].toInt() + 10 && event.y.toInt() < s.split(":")[1].toInt() + textView.height - 10) {
                min = textView
            }
        }
        return min
    }

    override fun addNewWord() {
        val usedViews = mutableSetOf<TextView>()
        var lastUsedCell: TextView? = null


        mainLayout.setOnTouchListener { _, event ->
            val cell = findNearestCell(event)

            var isThoseCellsIsNeighbors = presenter.isThisCellsNeighbors(resources.getResourceEntryName(cell.id), resources.getResourceEntryName(lastUsedCell?.id
                    ?: cell.id))
            if (lastUsedCell == null) isThoseCellsIsNeighbors = true

            if (isThoseCellsIsNeighbors && cell.text.isNotBlank()) {
                cell.setTextColor(Color.parseColor("#D4145A"))
                if (!usedViews.contains(cell)) {
                    enteredWord.text = "${enteredWord.text}${cell.text}"
                    lastUsedCell = cell
                }
                usedViews.add(cell)
            }

            doneButton.setOnClickListener {
                if (usedViews.contains(selectedCell)) {
                    word = enteredWord.text.toString()
                    presenter.turnIsMade()
                    selectedCell = null
                    lastUsedCell = null
                } else {
                    cancelTurn()
                    toast(R.string.must_use_new_letter_message)
                }
                setDefaultTextColor()
                usedViews.removeAll(usedViews)
                listOfCellsCoors.clear()
                addNewLetter()
            }

            cancelButton.setOnClickListener {
                cancelTurn()
                setDefaultTextColor()
                usedViews.removeAll(usedViews)
                listOfCellsCoors.clear()
                addNewLetter()
            }
            true
        }

    }

    override fun winnerChecking() {
        var isFieldFull = true
        gameField.forEachChild {
            it as TextView
            if (it.text.isBlank()) isFieldFull = false
        }
        if (isFieldFull) {
            when {
                presenter.firstPlayer.score > presenter.secondPlayer.score -> winningDialog(presenter.firstPlayer)
                presenter.secondPlayer.score > presenter.firstPlayer.score -> winningDialog(presenter.secondPlayer)
                else -> winningDialog(null)
            }
        }

    }

    override fun winningDialog(player: Player?) {
        val whoWin = when (player) {
            null -> resources.getString(R.string.draw)
            else -> "${player.name}${resources.getString(R.string.winner_message)}"
        }
        val dialog = AlertDialog.Builder(this)
                .setTitle(whoWin)
                .setPositiveButton("OK") { _, _ ->
                    val newGameFragment = NewGameFragment()
                    newGameFragment.show(supportFragmentManager, "newGame")
                }
                .setNegativeButton(R.string.no) { _, _ -> startActivity<MainMenuActivity>() }
                .setMessage(R.string.want_to_play_again)
                .setCancelable(false)
        dialog.create().show()
    }

    override fun prepareGameFieldForStart(firstPlayerName: String, secondPlayerName: String,
                                          mainWord: String, timeToTurn: String) {
        val mainWordCells = listOf(cell20, cell21, cell22, cell23, cell24)
        mainWordCells.forEachIndexed { index, cell ->
            cell.text = mainWord[index].toString().toUpperCase()
        }
        addWordToUsedWords(mainWord)

        this.firstPlayerName.text = firstPlayerName
        this.secondPlayerName.text = secondPlayerName
        presenter.firstPlayer = Player(firstPlayerName, 0)
        presenter.secondPlayer = Player(secondPlayerName, 0)
        setFirstPlayerTurn()

        if (timeToTurn == resources.getString(R.string.without_timer)) {
            timeRemains.visibility = View.GONE
        } else {
            startTimer(timeToTurn.toLong() * 1000)
        }

    }

    override fun setLettersForFieldModel() {
        gameField.forEachChild {
            val cellName = resources.getResourceEntryName(it.id)
            field[cellName].letter = (it as TextView).text.toString().toLowerCase()
        }
    }

    override fun setLetter(letterNumber: Int, letter: String?) {
        val cell = gameField[letterNumber]
        com.transitionseverywhere.TransitionManager.beginDelayedTransition(gameField,
                ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
        (cell as TextView).text = letter?.toUpperCase()
    }


    override fun addWordToScrollView() {
        val newTextView = TextView(this)
        val fade = Fade()
        fade.duration = 700
        TransitionManager.beginDelayedTransition(firstRow, fade)
        TransitionManager.beginDelayedTransition(secondRow, fade)
        newTextView.apply {
            text = "${word.toUpperCase()} - ${word.length}"
            topPadding = dip(2)
            leftPadding = dip(20)
            setTextColor(Color.parseColor("#FFFFFF"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
        if (!itWasFirstPlayerTurn) firstRow.addView(newTextView)
        else {
            newTextView.leftPadding = dip(64)
            secondRow.addView(newTextView)
        }
    }

    override fun skipTurnWhenTimeIsOver() {
        val newTextView = TextView(this)
        newTextView.apply {
            text = "-"
            topPadding = dip(2)
            leftPadding = dip(20)
            setTextColor(Color.parseColor("#FFFFFF"))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
        if (!itWasFirstPlayerTurn) firstRow.addView(newTextView)
        else {
            newTextView.leftPadding = dip(64)
            secondRow.addView(newTextView)
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (timeRemains.text.toString() != "123") {
            timer?.cancel()
            val millisRemains = timeRemains.text.split(":")
            val seconds = millisRemains[1].toLong() * 1000
            val minutes = millisRemains[0].toLong() * 60 * 1000
            startTimer(seconds + minutes)
        }
    }

    override fun startTimer(timeToTurnInMillis: Long) {
        timer = object : CountDownTimer(timeToTurnInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished + 1000) / 1000 % 60
                val minutes = (millisUntilFinished + 1000) / 1000 / 60
                timeRemains.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                presenter.timeIsOver()
                timer?.cancel()
                startTimer(intent.getStringExtra("timeToTurn").toLong() * 1000)
                addNewLetter()
            }
        }.start()
    }

    override fun cancelTimerAndStartNew() {
        if (timer != null) {
            timer?.cancel()
            startTimer(intent.getStringExtra("timeToTurn").toLong() * 1000)
        }
    }

    override fun showLongToastTimeIsOver() {
        toast(R.string.time_is_over)
    }

    override fun changeTurn() {
        if (itWasFirstPlayerTurn) {
            presenter.firstPlayer.addScore(word.length)
            setSecondPlayerTurn()
        } else {
            presenter.secondPlayer.addScore(word.length)
            setFirstPlayerTurn()
        }
        GameFieldModel.changeTurn()
    }

    override fun changeTurnWhenTimeIsEnd() {
        if (itWasFirstPlayerTurn) {
            setSecondPlayerTurn()
        } else {
            setFirstPlayerTurn()
        }
        GameFieldModel.changeTurn()
    }

    override fun showNewLetterDialog() {
        val builder = AlertDialog.Builder(this)
        val customView = layoutInflater.inflate(R.layout.add_new_letter_dialog, null)
        builder.setView(customView)
                .setTitle(R.string.adding_letter_alert)
                .setPositiveButton("OK") { _, _ ->
                    makeFullScreen()
                    if (customView.enteredLetter.text.length != 1) {
                        toast(R.string.new_letter_wrong_size_message)
                    } else {
                        selectedCell?.text = customView.enteredLetter.text.toString().toUpperCase()
                        makeFieldNotClickable()
                        addNewWord()
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    makeFullScreen()
                }
                .setCancelable(false)
        builder.create().show()
    }

    override fun showLongToastNewWordAdded() {
        longToast("${resources.getString(R.string.new_word_added_message)}: ${enteredWord.text}")
    }

    override fun setFirstPlayerTurn() {
        firstPlayerName.setTextColor(Color.parseColor("#FFFBB03B"))
        secondPlayerName.setTextColor(Color.parseColor("#FFFFFF"))
    }

    override fun setSecondPlayerTurn() {
        firstPlayerName.setTextColor(Color.parseColor("#FFFFFF"))
        secondPlayerName.setTextColor(Color.parseColor("#FFFBB03B"))
    }

    override fun makeFieldNotClickable() {
        gameField.forEachChild {
            it.isClickable = false
        }
    }

    override fun setDefaultTextColor() {
        gameField.forEachChild {
            (it as TextView).setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun cancelTurn() {
        selectedCell?.text = ""
        selectedCell?.isClickable = false
        cleanEnteredWord()
    }

    override fun cleanBoardBeforeNextTurn() {
        selectedCell?.text = ""
        selectedCell?.isClickable = true
        cleanEnteredWord()
        setDefaultTextColor()
    }

    override fun cleanEnteredWord() {
        enteredWord.text = ""
    }

    override fun showSnackCancelTurn() {
        mainLayout.snack(resources.getString(R.string.wrong_word_message) + ": $word") {
            action("Add word") {
                addWordToDictionary(word)
            }
        }
    }

    override fun refreshScore() {
        scoreFirstPlayer.text = presenter.firstPlayer.score.toString()
        scoreSecondPlayer.text = presenter.secondPlayer.score.toString()
    }

    override fun makeFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        usedWords.removeAll(usedWords)
    }
}