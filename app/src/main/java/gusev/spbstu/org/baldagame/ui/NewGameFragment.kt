package gusev.spbstu.org.baldagame.ui


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.arellomobile.mvp.MvpAppCompatDialogFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel
import gusev.spbstu.org.baldagame.mvp.presenter.NewGamePresenter
import gusev.spbstu.org.baldagame.mvp.views.NewGameView
import kotlinx.android.synthetic.main.fragment_new_game.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.startActivity


class NewGameFragment : MvpAppCompatDialogFragment(), NewGameView {

    var timeToTurn = "120"

    @InjectPresenter
    lateinit var presenter: NewGamePresenter
    lateinit var customView: View
    var selected: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return customView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        customView = context!!.layoutInflater.inflate(R.layout.fragment_new_game, null)
        val dialog = AlertDialog.Builder(activity)
                .setView(customView)
                .setTitle(R.string.new_game)
                .setPositiveButton("OK") { _, _ ->
                    presenter.startNewGame(mainWord.text.toString())
                    GameFieldModel.clean()
                }
        return dialog.create()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpDifficultySpinner()
        selected = resources.getString(R.string.easy)
        setTimer.setOnClickListener { presenter.setTimerIsClicked() }
        randomWord.setOnClickListener { presenter.randomWordIsClicked() }
        botGame.onCheckedChangeListener = { _, isChecked ->
            secondPlayer.isEnabled = !isChecked
            difficulty.isEnabled = isChecked
        }
    }

    override fun showTimeToTurnSelector() {
        val options = listOf("1:00", "1:30", "2:00", "3:00", "5:00", resources.getString(R.string.without_timer))
        selector(resources.getString(R.string.time_to_turn), options) { _, i ->
            timeToTurn = when (options[i]) {
                options[0] -> "60"
                options[1] -> "90"
                options[2] -> "120"
                options[3] -> "180"
                options[4] -> "300"
                options[5] -> resources.getString(R.string.without_timer)
                else -> "120"
            }
        }
    }

    override fun setUpDifficultySpinner() {
        difficulty.isEnabled = false
        val list = listOf(resources.getString(R.string.easy), resources.getString(R.string.medium), resources.getString(R.string.very_hard))
        val adapter = ArrayAdapter(customView.context, android.R.layout.simple_spinner_dropdown_item, list)
        difficulty.adapter = adapter
        difficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change text of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selected = list[p2]
            }
        }
    }

    override fun startNewGameActivity() {
        startActivity<GameFieldActivity>("mainWord" to mainWord.text.toString(),
                "firstPlayerName" to firstPlayer.text.toString(),
                "secondPlayerName" to secondPlayer.text.toString(),
                "timeToTurn" to timeToTurn,
                "botGame" to botGame.isChecked,
                "difficulty" to selected)
    }

    override fun showToast() {
        longToast(R.string.wrong_word_length)
    }

    override fun setMainWord(word: String) {
        mainWord.setText(word)
    }

}
