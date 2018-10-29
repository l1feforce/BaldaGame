package gusev.spbstu.org.baldagame.mvp.model

class Player(val name: String, var score: Int = 0) {

    fun addScore(wordLength: Int) {
        score += wordLength
    }
}