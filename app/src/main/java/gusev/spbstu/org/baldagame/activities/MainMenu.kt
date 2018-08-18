package gusev.spbstu.org.baldagame.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import gusev.spbstu.org.baldagame.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startNewGame()
    }

    fun startNewGame() {
        newGame.setOnClickListener {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.layoutForNewGame, NewGame(), "newGame")
                    .commit()
        }
    }

}
