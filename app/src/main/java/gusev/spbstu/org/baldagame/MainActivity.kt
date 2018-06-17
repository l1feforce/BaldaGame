package gusev.spbstu.org.baldagame

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUi().setContentView(this)
    }
}


class MainActivityUi : AnkoComponent<MainActivity> {

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

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        verticalLayout {
            button("Just start") {
                onClick {
                    startActivity<GameFieldUI>("mainWord" to "балда",
                            "firstPlayerName" to "Игрок1",
                            "secondPlayerName" to "Игрок2")
                }
            }
            button("2 players") {
                onClick {
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
            }.lparams {
                width = 640
                height = 160
                topMargin = 680
                gravity = Gravity.CENTER
            }

            button("Settings") {
                onClick {
                }
            }.lparams {
                width = 640
                height = 160
                topMargin = 60
                gravity = Gravity.CENTER
            }

            button("Exit") {
                onClick {
                    System.exit(0);
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
