package de.benedikt_werner.connect4solver.test;

import org.junit.Assert;
import org.junit.Test;

import de.benedikt_werner.connect4solver.Board;

public class BoardTest {

    @Test
    public void testIsWinningMove_empty() {
        assertIsWinningMove("", 4, false, 5, 5);
        assertIsWinningMove("", 0, false, 5, 5);
        assertIsWinningMove("", 2, false, 5, 5);
        assertIsWinningMove("", 1, false, 5, 5);
        assertIsWinningMove("2", 3, false, 5, 5);
    }
    
    @Test
    public void testIsWinningMove_horizontal() {
        assertIsWinningMove("112233", 4, true, 5, 5);
        assertIsWinningMove("112233", 0, true, 5, 5);
    }
    
    @Test
    public void testIsWinningMove_vertical() {
        assertIsWinningMove("121212", 1, true, 5, 5);
        assertIsWinningMove("1212123", 2, true, 5, 5);
    }
    
    @Test
    public void testIsWinningMove_diagonal() {
        assertIsWinningMove("0113332224", 3, true, 5, 5); // highest last
        assertIsWinningMove("4331112220", 1, true, 5, 5);
        assertIsWinningMove("2112233334", 0, true, 5, 5); // lowest last
        assertIsWinningMove("2332211110", 4, true, 5, 5);
        assertIsWinningMove("0122233334", 1, true, 5, 5); // middle last
        assertIsWinningMove("4322211110", 3, true, 5, 5);
    }

    public static void assertIsWinningMove(String sequence, int move, boolean winner, int bwidth, int bheight) {
        Board b = new Board(bwidth, bheight);
        b.playSequence(sequence);;
        Assert.assertEquals(winner, b.isWinningMove(move));
    }
}
