package gusev.spbstu.org.baldagame.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import gusev.spbstu.org.baldagame.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)
    }

}


class MainActivityUi : AnkoComponent<MainActivity>  {

    private val customStyleMain = { v: Any ->
        when (v) {
            is Button -> {
                v.textSize = 14f
                v.width = 440
                v.height = 140
                v.gravity = Gravity.CENTER
            }
        }
    }


    fun randomWord(): String {
        val input = this.javaClass.getClassLoader().getResourceAsStream("assets/singular.txt")
        val allWords = input.bufferedReader().use {
            it.readLines()
        }
        val filteredWords = allWords.filter { it.length == 5 }
        val random = Random().nextInt(filteredWords.size)
        return filteredWords[random]
    }

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        verticalLayout {
            button("Just start") {
                onClick {
                    startActivity<GameFieldUI>("mainWord" to "балда",
                            "firstPlayerName" to "Игрок1",
                            "secondPlayerName" to "Игрок2",
                            "timeToTurn" to "10")
                }
            }
            button(R.string.two_players) {
                onClick {
                    alert(R.string.new_game) {
                        customView {
                            verticalLayout {
                                val firstPlayer = editText("Player1") {
                                    setHint(R.string.first_player_name)
                                }.lparams(weight = 1f, width = matchParent)
                                val secondPlayer = editText("Player2") {
                                    setHint(R.string.second_player_name)
                                }.lparams(weight = 1f, width = matchParent)
                                val mainWord = editText("балда") {
                                    setHint(R.string.main_word)
                                }.lparams(weight = 1f, width = matchParent)
                                val randomWord = button(R.string.random_word) {
                                    onClick {
                                        val randomWord = randomWord()
                                        mainWord.setText(randomWord)
                                    }
                                }
                                var timeToTurn = "120"
                                val setTimer = button(R.string.set_timer) {
                                    onClick {
                                        val options = listOf("1:00", "1:30", "2:00", "3:00", "5:00")
                                        selector(resources.getString(R.string.time_to_turn), options) { _, i ->
                                            timeToTurn = when(options[i]) {
                                                options[0] -> "60"
                                                options[1] -> "90"
                                                options[2] -> "120"
                                                options[3] -> "180"
                                                options[4] -> "300"
                                                else -> "120"
                                            }
                                        }
                                    }
                                }.lparams(weight = 1f, width = matchParent)
                                val okButton = button("OK") {
                                    onClick {
                                        if (mainWord.text.toString().length != 5) longToast(R.string.wrong_word_length)
                                        else startActivity<GameFieldUI>("mainWord" to mainWord.text.toString(),
                                                "firstPlayerName" to firstPlayer.text.toString(),
                                                "secondPlayerName" to secondPlayer.text.toString(),
                                                "timeToTurn" to timeToTurn)
                                    }
                                }.lparams(weight = 4f, width = matchParent)
                                padding = dip(16)

                            }
                        }
                    }.show()
                }
            }.lparams {
                width = 640
                height = 160
                topMargin = 680
                gravity = Gravity.CENTER
            }

            button(R.string.settings) {
                onClick {
                }
            }.lparams {
                width = 640
                height = 160
                topMargin = 60
                gravity = Gravity.CENTER
            }

            button(R.string.exit) {
                onClick {
                    System.exit(0)
                }
            }.lparams {
                width = 640
                height = 160
                topMargin = 60
                gravity = Gravity.CENTER
            }
        }.applyRecursively(customStyleMain)
    }
}
