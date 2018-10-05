package gusev.spbstu.org.baldagame.ui

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.action
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToDictionary
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.addWordToUsedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.usedWords
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.word
import gusev.spbstu.org.baldagame.mvp.model.Player
import gusev.spbstu.org.baldagame.mvp.presenter.GameFieldPresenter
import gusev.spbstu.org.baldagame.mvp.views.GameFieldView
import gusev.spbstu.org.baldagame.snack
import kotlinx.android.synthetic.main.add_new_letter_dialog.view.*
import kotlinx.android.synthetic.main.game_field.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.lang.Math.pow
import java.lang.Math.sqrt

class GameFieldActivity : MvpAppCompatActivity(), GameFieldView {


    @InjectPresenter
    lateinit var presenter: GameFieldPresenter

    var timer: CountDownTimer? = null
    var selectedCell: TextView? = null
    private var count = 0
    var listOfCellsCoords = mutableMapOf<TextView, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_field)
        makeFullScreen()
        prepareGameFieldForStart(intent.getStringExtra("firstPlayerName") ?: "",
                intent.getStringExtra("secondPlayerName") ?: "",
                intent.getStringExtra("mainWord") ?: "балда",
                intent.getStringExtra("timeToTurn") ?: "")



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

    private fun findNearestCell(event: MotionEvent): TextView {
        var min: Pair<TextView, Int> = cell00 to 1000
        gameField.forEachChild {
            val viewCoordinates = IntArray(2)
            it.getLocationOnScreen(viewCoordinates)
            listOfCellsCoords[it as TextView] = "${viewCoordinates[0]}:${viewCoordinates[1]}"
        }
        listOfCellsCoords.forEach { textView, s ->
            val distance = sqrt(pow((event.x.toInt() - s.split(":")[0].toInt()).toDouble(), 2.0) +
                    pow((event.y.toInt() - s.split(":")[1].toInt()).toDouble(), 2.0))
            if (distance < min.second) min = textView to distance.toInt()
        }
        return min.first
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
                listOfCellsCoords.clear()
                addNewLetter()
            }

            cancelButton.setOnClickListener {
                cancelTurn()
                setDefaultTextColor()
                usedViews.removeAll(usedViews)
                listOfCellsCoords.clear()
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
        if (GameFieldModel.itWasFirstPlayerTurn) {
            presenter.firstPlayer.addScore(word.length)
            setSecondPlayerTurn()
        } else {
            presenter.secondPlayer.addScore(word.length)
            setFirstPlayerTurn()
        }
        GameFieldModel.changeTurn()
    }

    override fun changeTurnWhenTimeIsEnd() {
        if (GameFieldModel.itWasFirstPlayerTurn) {
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
        GameFieldModel.usedWords.removeAll(usedWords)
    }
}