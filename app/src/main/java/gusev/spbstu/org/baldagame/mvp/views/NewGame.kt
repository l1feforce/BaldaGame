package gusev.spbstu.org.baldagame.mvp.views


import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.mvp.presenter.Presenter
import kotlinx.android.synthetic.main.fragment_new_game.*
import kotlinx.android.synthetic.main.fragment_new_game.view.*
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast
import java.util.*




class NewGame : AppCompatDialogFragment() {

    lateinit var presenter: Presenter
    lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return customView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        customView = context!!.layoutInflater.inflate(R.layout.fragment_new_game, null)
        val dialog = AlertDialog.Builder(activity)
                .setView(customView)
                .setTitle(R.string.new_game)
                .setPositiveButton("OK") { _, _ -> presenter.startNewGame() }
        presenter = Presenter(this, customView)
        return dialog.create()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTimer.setOnClickListener {
            presenter.setTimerIsClicked()
        }

        randomWord.setOnClickListener {
            presenter.randomWordIsClicked()
        }

    }


}
