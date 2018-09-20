package gusev.spbstu.org.baldagame.mvp.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import gusev.spbstu.org.baldagame.MainContract
import gusev.spbstu.org.baldagame.R
import gusev.spbstu.org.baldagame.activities.GameFieldUI
import gusev.spbstu.org.baldagame.mvp.presenter.Presenter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainMenu : AppCompatActivity() {
    lateinit var presenter:MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = Presenter(this)

        newGame.setOnClickListener {
            presenter.newGameIsClicked(supportFragmentManager)
        }
    }

}
