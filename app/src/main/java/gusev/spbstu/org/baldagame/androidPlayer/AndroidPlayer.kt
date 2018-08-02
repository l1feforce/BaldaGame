package gusev.spbstu.org.baldagame.androidPlayer

import android.widget.TextView
import gusev.spbstu.org.baldagame.GameField

class AndroidPlayer(val field: GameField) {
    val allLetters = listOf("а", "б", "в", "г", "д", "е", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п",
            "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я")
    val foundWords = Trie<Char>()
    val sortedList = field.database.sorted()

    fun findNewWords() {
        field.tableInit()
        field.table.forEachIndexed { y, list ->
            list.forEachIndexed { x, textView ->
                if (isThisOkayPlaceForNewLetter(x, y)) {
                    allLetters.forEach {
                        textView.text = it
                        getRightWords(searchOfWords(textView)).forEach {
                            if (!foundWords.contains(it.toList())) foundWords.add(it.toList())
                        }
                    }
                    textView.text = ""
                }
            }
        }
    }

    fun isThisOkayPlaceForNewLetter(x: Int, y: Int): Boolean {
        if (field.table[x][y].text.isNotBlank()) return false
        val list = mutableListOf<TextView>()
        if (y - 1 >= 0) list.add(field.table[x][y - 1])//upper cell
        if (x + 1 <= 4) field.table[x + 1][y] //right cell
        if (y + 1 <= 4) field.table[x][y + 1] //lower cell
        if (x - 1 >= 0) field.table[x - 1][y] //left cell
        var isItHaveLetterNear: Boolean = false
        list.forEach {
            if (it.text.isNotBlank()) {
                isItHaveLetterNear = true
                return@forEach
            }
        }

        return isItHaveLetterNear
    }

    fun searchOfWords(textView: TextView): List<String> {
        //TODO()
        return listOf<String>()
    }

    fun getRightWords(list: List<String>): List<String> = list.filter { field.checkWordInDictionary(it) }

}