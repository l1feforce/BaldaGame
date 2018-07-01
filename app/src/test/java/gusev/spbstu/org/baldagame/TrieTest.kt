package gusev.spbstu.org.baldagame

import org.junit.Test

import org.junit.Assert.*

class TrieTest {

    @Test
    fun contains() {
        val trie = Trie<String>()
        trie.add(listOf("b", "a", "l", "d", "a"))
        assertTrue(trie.contains(listOf("balda")))
        // assertTrue(trie.contains(listOf()))
    }
}