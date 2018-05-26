package gusev.spbstu.org.baldagame

import org.junit.Test

import org.junit.Assert.*

class GameFieldTest {


    @Test
    fun newWord() {
        val player1 = Player("player1", 0)
        val player2 = Player("player2", 0)
        val testingTable = mutableListOf("fалда", "балда", "балда", "балда", "балда")
        val testingTable2 = mutableListOf("балда", "балда", "балда", "балда", "балда")
        val testingGameField1 = GameField(player1, player2, true)
        testingGameField1.table = testingTable2
        val testingGameField2 = GameField(player1, player2, true)
        testingGameField2.table = testingTable
        testingGameField1.newWord(Point(0,0), 'f')
        assertEquals(testingGameField1.table,
                testingGameField2.table)
    }
}