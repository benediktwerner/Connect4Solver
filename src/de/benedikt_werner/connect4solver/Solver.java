package de.benedikt_werner.connect4solver;

import java.util.Scanner;

public class Solver {
    private Board board;
    private int move;
    
    public static void main(String[] a) {
        Solver s = new Solver(new Board(7, 6));
        //s.board.playSequence("1122303");
        // 1122303 interesting sequence
        //s.board.printBoard();
        //int[] result = s.solve();
        //System.out.println(result[0] + " - " + result[1]);
        s.play();
    }
    
    public Solver(Board board) {
        this.board = board;
    }
    
    public void play() {
        Scanner in = new Scanner(System.in);
        
        while (true) {
            System.out.println("Calculating move...");
            int[] result = solve();
            System.out.println("Best move: " + result[0] + " - " + result[1]);
            if (result[0] == -1) {
                System.out.println("No move found");
                break;
            }
            if (placeStone(result[0]))
                break;
            
            board.printBoard();
            int move;
            while (true) {
                System.out.println("Your move: ");
                move = in.nextInt();
                if (board.canPlay(move))
                    break;
                else System.out.println("Invalid move!");
            }
            if (placeStone(move))
                break;
        }
        in.close();
    }
    
    // Place stone in column, return true if game is over
    public boolean placeStone(int column) {
        if (board.isWinningMove(column)) {
            System.out.println("Player " + board.player + " wins!");
            return true;
        }
        board.placeStone(column);
        if (board.isFull()) {
            System.out.println("Game draw!");
            return true;
        }
        return false;
    }
    
    // return: {move, value}
    public int[] solve() {
        move = -1;
        long start = System.nanoTime();
        int value = miniMax(true, -10000, 10000);
        long end = System.nanoTime();
        System.out.println("Time: " + (end - start) + "ns == " + (end-start)/1000 + "µs == " + (end-start)/1000000 + "ms");
        return new int[]{move, value};
    }
    
    private int getWinValue() {
        return board.width * board.height - board.history.size();
    }
    
    private int miniMax(boolean start, int alpha, int beta) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return getWinValue();
            }
        for (int x = 0; x < board.width; x++) {
            if (start) System.out.println(x);
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = -miniMax(false, -beta, -alpha);
            board.revert();
            if (value > alpha) {
                alpha = value;
                if (value >= beta)
                    return value;
                if (start) move = x;
            }
        }
        return alpha;
    }
}
