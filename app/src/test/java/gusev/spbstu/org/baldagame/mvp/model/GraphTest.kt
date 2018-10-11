package gusev.spbstu.org.baldagame.mvp.model

import gusev.spbstu.org.baldagame.PrefixTree
import org.junit.Test

class GraphTest {

    @Test
    fun wordsSearch() {
        val dictionary = PrefixTree()
        val invDictionary = PrefixTree()
        //dictionaries init

        //field init
        val field = Graph()

        fun init() {
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
        init()
        "балда".forEachIndexed { index, c ->
            field["cell2$index"].letter = c.toString()
        }
        //field["cell11"].letter = "к"

        //field["cell10"].letter = "о"
        //field["cell13"].letter = "ы"
        //field["cell02"].letter = "д"

        //field["cell01"].letter = "е"



        //println(field.wordsSearch("cell01", dictionary, invDictionary))
        //val test = field.invWordsSearch(field["cell11"], mutableSetOf(), StringBuilder(field["cell11"].letter), mutableSetOf(), invDictionary)
                //val test = field.wordsSearch("cell11", dictionary, invDictionary)
        //println(test)
        val set = mutableMapOf<String, String>()
        val input = javaClass.classLoader.getResourceAsStream("singular.txt")
        input.bufferedReader().use { it.readLines() }.forEach { word ->
            dictionary.insert(word)
            word.forEachIndexed {index, _ ->
                invDictionary.insert(word.substring(0, word.length - index).reversed())
            }
        }
        val listOfLetters = listOf("а", "б", "в", "г", "д", "е", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п",
                "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я")
        field.vertices.forEach { vertexName, vertex ->
            if (vertex.letter.isBlank()) {
                listOfLetters.forEach { newLetter ->
                    vertex.letter = newLetter
                            //field.wordsSearch(vertexName, dictionary, invDictionary).forEach { set[it] = vertexName }
                    vertex.letter = " "
                }
            }
        }
        val test = set.maxBy { it.key.length }?.key ?: "her"
        println(set)
    }
}