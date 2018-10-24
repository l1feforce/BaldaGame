package gusev.spbstu.org.baldagame.mvp.model


import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.dictionary
import gusev.spbstu.org.baldagame.mvp.model.GameFieldModel.invDictionary
import java.util.*

class Graph {
    data class Vertex(val name: String, var letter: String) {
        val neighbors = mutableSetOf<Vertex>()
    }

    data class FoundWord(val word: String) {
        var visitedVertex = mutableSetOf<Vertex>()
    }

    val vertices = mutableMapOf<String, Vertex>()

    operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    fun addVertex(name: String, letter: String) {
        vertices[name] = Vertex(name, letter)
    }

    private fun connect(first: Vertex, second: Vertex) {
        first.neighbors.add(second)
        second.neighbors.add(first)
    }

    fun connect(first: String, second: String) = connect(this[first], this[second])

    fun neighbors(name: String) = vertices[name]?.neighbors?.map { it.name to it.letter }
            ?: listOf()

    fun bfs(start: String, finish: String) = bfs(this[start], this[finish])

    private fun bfs(start: Vertex, finish: Vertex): Int {
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        val visited = mutableMapOf(start to 0)
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            val distance = visited[next]!!
            if (next == finish) return distance
            for (neighbor in next.neighbors) {
                if (neighbor in visited) continue
                visited[neighbor] = distance + 1
                queue.add(neighbor)
            }
        }
        return -1
    }

    fun wordsSearch(start: String): Set<String> {
        val setOfWords = mutableSetOf<String>()
        val foundInvWords = invWordsSearch(this[start], mutableSetOf(), StringBuilder(this[start].letter), mutableSetOf(), mutableSetOf())
        foundInvWords.add(FoundWord(this[start].letter))
        foundInvWords.forEach {
            if (dictionary.contains(it.word.reversed())) setOfWords.add(it.word.reversed())
            val foundWords = wordsSearch(this[start], it.visitedVertex, StringBuilder(""), mutableSetOf(), it)
            foundWords.forEach { foundWord -> setOfWords.add(foundWord) }
        }
        return setOfWords
    }

    private fun invWordsSearch(start: Vertex, visited: MutableSet<Vertex>, word: StringBuilder,
                               setOfWords: MutableSet<FoundWord>, visitedForWord: MutableSet<Vertex>): MutableSet<FoundWord> {
        val min = start.neighbors.filter { it !in visited }
        visited.add(start)
        visitedForWord.add(start)
        min.forEach {
            word.append(it.letter)
            if (invDictionary.contains(word.toString())) {
                val foundWord = FoundWord(word.toString())
                visitedForWord.add(it)
                foundWord.visitedVertex = visitedForWord
                setOfWords.add(foundWord)
            }
            if (!invDictionary.isWordHaveEnding(word.toString())) {
                if (word.isNotEmpty()) word.setLength(word.length - 1)
                return@forEach
            }
            invWordsSearch(it, visited, word, setOfWords, visitedForWord)
        }
        visited.remove(start)
        if (word.isNotEmpty()) word.setLength(word.length - 1)
        return setOfWords
    }

    private fun wordsSearch(start: Vertex, visited: MutableSet<Vertex>, word: StringBuilder,
                            setOfWords: MutableSet<String>, usedWord: FoundWord): Set<String> {
        val min = start.neighbors.filter { it !in visited }
        visited.add(start)
        min.forEach {
            word.append(it.letter)
            val wordToCheck =  usedWord.word.reversed() + word.toString()
            if (dictionary.contains(wordToCheck)) {
                setOfWords.add(wordToCheck)
            }
            if (!dictionary.isWordHaveEnding(wordToCheck)) {
                if (word.isNotEmpty()) word.setLength(word.length - 1)
                return@forEach
            }
            wordsSearch(it, visited, word, setOfWords, usedWord)
        }
        visited.remove(start)
        if (word.isNotEmpty()) word.setLength(word.length - 1)
        return setOfWords
    }
}