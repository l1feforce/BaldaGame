package gusev.spbstu.org.baldagame.mvp.views

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.presenter.Presenter
import kotlinx.android.synthetic.main.game_field.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.toast

class GameField : AppCompatActivity() {
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_field)
        presenter = Presenter(this)
        makeFullScreen()
        prepareGameFieldForStart(intent.getStringExtra("firstPlayerName") ?: "",
                intent.getStringExtra("secondPlayerName") ?: "",
                intent.getStringExtra("mainWord") ?: "балда",
                intent.getStringExtra("timeToTurn") ?: "")

        addNewLetter()
    }

    fun addNewLetter() {
        gameField.forEachChild {
            it as TextView
            if (it.text.toString().isBlank()) {
                it.setOnClickListener { _ ->
                    presenter.addNewLetter(it)
                    makeFullScreen()
                }
            } else it.setOnClickListener(null)
        }
    }

    fun addNewWord(enteredLettersCell: TextView) {
        val usedViews = mutableListOf<TextView>()
        gameField.forEachChild {cell ->
            cell.setOnTouchListener { view, event ->
                val viewCoordinates = IntArray(2)
                view.getLocationOnScreen(viewCoordinates)
                if (event.x.toInt() > viewCoordinates[0] && event.x.toInt() < viewCoordinates[0] + cell.width &&
                        event.y.toInt() > viewCoordinates[1] && event.y.toInt() < viewCoordinates[1] + cell.height) {
                    cell as TextView
                    toast("huy")
                    if (!usedViews.contains(cell)) {
                        enteredWord.text = "${enteredWord.text}${cell.text}"
                        usedViews.add(cell)
                    }
                    if (event.action == MotionEvent.ACTION_UP) {
                        gameField.setOnTouchListener(null)
                        if (usedViews.contains(enteredLettersCell)) {
                            toast("sdf")
                        } else {
                            enteredLettersCell.text = ""
                            enteredLettersCell.isClickable = true
                            toast(R.string.must_use_new_letter_message)
                        }
                        usedViews.removeAll(usedViews)
                        addNewLetter()
                    }
                }
                true
            }
        }
    }

    fun prepareGameFieldForStart(firstPlayerName: String, secondPlayerName: String,
                                 mainWord: String, timeToTurn: String) {
        val mainWordCells = listOf(cell20, cell21, cell22, cell23, cell24)
        mainWordCells.forEachIndexed { index, cell ->
            cell.text = mainWord[index].toString().toUpperCase()
        }
        this.firstPlayerName.text = firstPlayerName
        this.secondPlayerName.text = secondPlayerName
    }

    fun makeFullScreen() {
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
    }
}
