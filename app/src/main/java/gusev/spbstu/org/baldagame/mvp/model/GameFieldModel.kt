package gusev.spbstu.org.baldagame.mvp.model

import android.content.SharedPreferences

object GameFieldModel {
    private val input = javaClass.classLoader.getResourceAsStream("assets/singular.txt")
    var dictionary = PrefixTree()
    var invDictionary = PrefixTree()
    val listOfLetters = listOf("а", "б", "в", "г", "д", "е", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п",
            "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я")
    val usedWords = mutableListOf("")
    var word = ""
    var itWasFirstPlayerTurn = true
    val field = Graph()
    lateinit var prefs: SharedPreferences
    init {
        //fields graph creating
        fieldInitialization()
    }


    fun addWordToUsedWords(word: String) {
        usedWords.add(word.toLowerCase())
    }

    fun isThisWordOkay(word: String): Boolean {
        return dictionary.contains(word.toLowerCase()) && !containsInUsedWords(word.toLowerCase())
    }

    fun addWordToDictionary(word: String) {
        //TODO()
    }

    private fun containsInUsedWords(word: String): Boolean {
        return usedWords.contains(word.toLowerCase())
    }

    fun changeTurn() {
        itWasFirstPlayerTurn = !itWasFirstPlayerTurn
    }

    fun dictionariesInit() {
                input.bufferedReader().use { it.readLines() }.forEach { word ->
                    dictionary.insert(word)
                    word.forEachIndexed { index, _ ->
                        invDictionary.insert(word.substring(0, word.length - index).reversed())
                    }
                }
    }

    private fun fieldInitialization() {
        for (x in 0..4) {
            for (y in 0..4) {
                field.addVertex("cell$y$x", " ")
            }
        }

        for (x in 0..4) {
            for (y in 0..4) {
                //upper cell
                if (y - 1 >= 0) field.connect("cell$y$x", "cell${y - 1}$x")
                //right cell
                if (x + 1 <= 4) field.connect("cell$y$x", "cell$y${x + 1}")
                //bottom cell
                if (y + 1 <= 4) field.connect("cell$y$x", "cell${y + 1}$x")
                //left cell
                if (x - 1 >= 0) field.connect("cell$y$x", "cell$y${x - 1}")
            }
        }
    }
}