package gusev.spbstu.org.baldagame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import gusev.spbstu.org.baldagame.R.id.center
import gusev.spbstu.org.baldagame.R.id.wrap_content
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch


class GameTable : AppCompatActivity() {

    @SuppressLint("ResourceType")
    private val customStyle = { v: Any ->
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
                v.topPadding = dip(230)
                //v.gravity = Gravity.TOP
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
        val allWords = application.assets.open("singular.txt").bufferedReader().use {
            it.readLines()
        }
        val field = GameField(Player(firstPlayer, 0), Player(secondPlayer, 0), true)
        field.start(mainWord)
        field.database = allWords
        verticalLayout {
            linearLayout {
                textView(firstPlayer) {
                    textSize = 20f
                    height = dip(30)
                    width = wrap_content
                    background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                    gravity = Gravity.CENTER
                }.lparams {
                    margin = dip(20)
                    bottomMargin = dip(5)
                    weight = 1f
                }
                textView(secondPlayer) {
                    textSize = 20f
                    height = dip(30)
                    width = wrap_content
                    background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                    gravity = Gravity.CENTER
                }.lparams {
                    margin = dip(20)
                    bottomMargin = dip(5)
                    weight = 1f
                }
            }

            linearLayout {
                val firstPlayerWords = textView(firstPlayer) {
                    textSize = 16f
                    height = dip(20)
                    width = wrap_content
                    background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                    gravity = Gravity.START
                }.lparams {
                    margin = dip(10)
                    weight = 1f
                }
                val secondPlayerWords = textView(secondPlayer) {
                    textSize = 16f
                    height = dip(20)
                    width = wrap_content
                    background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
                    gravity = Gravity.START
                }.lparams {
                    margin = dip(10)
                    weight = 1f
                }
            }
            val gameField = tableLayout {
                tableRow {
                    textView(field.table[0][0].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][1].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][2].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][3].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[0][4].toString()) {
                    }.lparams {
                        weight = 1f
                    }
                }.lparams(initWeight = 1f)
                tableRow {
                    textView(field.table[1][0].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][1].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][2].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][3].toString()) {
                    }.lparams {
                        weight = 1f
                    }

                    textView(field.table[1][4].toString()) {
                    }.lparams {
                        weight = 1f
                    }
                }.lparams(initWeight = 1f)
                    tableRow {
                        textView(mainWord[0].toString().toUpperCase()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[1].toString().toUpperCase()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[2].toString().toUpperCase()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[3].toString().toUpperCase()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(mainWord[4].toString().toUpperCase()) {
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                    tableRow {
                        textView(field.table[3][0].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][1].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][2].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][3].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[3][4].toString()) {
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                    tableRow {
                        textView(field.table[4][0].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][1].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][2].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][3].toString()) {
                        }.lparams {
                            weight = 1f
                        }

                        textView(field.table[4][4].toString()) {
                        }.lparams {
                            weight = 1f
                        }
                    }.lparams(initWeight = 1f)
                background = Drawable.createFromXml(resources, resources.getXml(R.drawable.my_border))
            }.applyRecursively(customStyle)
            }
        }
    }
