package gusev.spbstu.org.baldagame

import org.junit.Test

import org.junit.Assert.*

class TrieTest {

    @Test
    fun contains() {
        val trie = Trie<Char>()
        val words = listOf("тигр", "тапир", "барибал", "барсук")
        words.forEach {
            trie.add(it.toList())
        }
        assertTrue(trie.contains(listOf('т', 'а', 'п', 'и', 'р')))
        assertFalse(trie.contains(listOf('т', 'и', 'г')))
        // assertTrue(trie.contains(listOf()))
    }
}