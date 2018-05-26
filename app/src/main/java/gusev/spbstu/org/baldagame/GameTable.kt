package gusev.spbstu.org.baldagame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.coreui.R.id.end
import android.view.*
import android.widget.*
import gusev.spbstu.org.baldagame.R.id.*
import kotlinx.coroutines.experimental.delay
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

class GameTable : AppCompatActivity() {

    @SuppressLint("ResourceType")
    private val customStyleForTable = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is TextView -> {
                v.textSize = 40f
                v.width = matchParent
                v.height = wrap_content
                v.gravity = Gravity.CENTER
                v.background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
            }
            is TableRow -> {
                v.gravity = Gravity.CENTER
            }
            is TableLayout -> {
                v.padding = dip(10)
                v.topPadding = dip(300)
                v.gravity = Gravity.BOTTOM
            }
            is LinearLayout -> {
               // v.gravity = Gravity.BOTTOM
            }
        }
    }

    private val customStyleForLinear = { v: Any ->
        when (v) {
            is LinearLayout -> {
                v.gravity = Gravity.TOP
            }
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)


        val firstPlayer = intent.getStringExtra("firstPlayerName") ?: ""
        val secondPlayer = intent.getStringExtra("secondPlayerName") ?: ""
        val mainWord: String = intent.getStringExtra("mainWord") ?: ""
        //if(!File("app/src/main/assets/singular.txt").exists()) throw IOException()
        val allWords = application.assets.open("singular.txt").bufferedReader().use {
            it.readLines()
        }
        val field = GameField(Player(firstPlayer, 0), Player(secondPlayer, 0), true)
        field.staringWord(mainWord)
        field.database = allWords
        verticalLayout {
            textView(firstPlayer).lparams {
                leftPadding = dip(50)
                topPadding = dip(30)
                background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                width = dip(50)
                height = wrapContent
                gravity = Gravity.LEFT
            }
            textView(secondPlayer).lparams() {
                rightPadding = dip(50)
                topPadding = dip(30)
                background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                width = dip(50)
                height = wrapContent
                gravity = Gravity.RIGHT
            }
            tableLayout {
                tableRow {

                    textView(field.table[0][0].toString()) {

                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][1].toString()) {
                        onTouch { v, event ->
                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][2].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][3].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][4].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }
                }.lparams(initWeight = 1f)
                tableRow {
                    textView(field.table[1][0].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][1].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][2].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][3].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][4].toString()) {
                        onTouch { v, event ->

                        }
                    }.lparams {
                        weight = 1f
                    }
                }.lparams(initWeight = 1f) {
                    tableRow {
                        textView(mainWord[0].toString().toUpperCase()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[1].toString().toUpperCase()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[2].toString().toUpperCase()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[3].toString().toUpperCase()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[4].toString().toUpperCase()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                    tableRow {
                        textView(field.table[3][0].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][1].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][2].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][3].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][4].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                    tableRow {
                        textView(field.table[4][0].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][1].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][2].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][3].toString()) {
                            onTouch { v, event ->

                            }
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][4].toString()) {
                            onTouch { v, event ->
                            }
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                }
            }.applyRecursively(customStyleForTable)
        }.applyRecursively(customStyleForLinear)

    }
}