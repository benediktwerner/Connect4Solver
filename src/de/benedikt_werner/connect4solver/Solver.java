package de.benedikt_werner.connect4solver;

import java.util.Scanner;

public class Solver {
    private Board board;
    private int move;
    
    public static void main(String[] a) {
        Solver s = new Solver(new Board(4, 4));
        s.board.playSequence("1122302");
        // 112230
        // 1122303 interesting sequence
        s.board.printBoard();
        int[] result = s.solve();
        System.out.println(result[0] + " - " + result[1]);
//        s.play();
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
        long time0 = System.nanoTime();
        int value1 = max(true);
        int move1 = move;
        System.out.println("1 finished");
        long time1 = System.nanoTime();
        int value2 = miniMax(true);
        int move2 = move;
        System.out.println("2 finished");
        long time2 = System.nanoTime();
        int value3 = max(true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int move3 = move;
        System.out.println("3 finished");
        long time3 = System.nanoTime();
        int value4 = miniMax(true, -10000, 10000);
        int move4 = move;
        System.out.println("4 finished");
        long time4 = System.nanoTime();
        System.out.println("Values: " + value1 + " - " + value2 + " - " + value3 + " - " + value4);
        System.out.println("Moves: " + move1 + " - " + move2 + " - " + move3 + " - " + move4);
        System.out.println("Times: " + (time1-time0) + " - " + (time2-time1) + " - " + (time3-time2) + " - " + (time4-time3));
        return new int[]{move, value1};
    }
    
    private int getWinValue() {
        return 3;
        // return board.width * board.height - board.history.size();
    }
    
    private int max(boolean start) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return getWinValue();
            }
        int maxValue = Integer.MIN_VALUE;
        for (int x = 0; x < board.width; x++) {
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = min(false);
            if (start) System.out.println(x + " -> " + value);
            board.revert();
            if (value > maxValue) {
                maxValue = value;
                if (start) move = x;
            }
        }
        return maxValue;
    }
    
    private int min(boolean start) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return -getWinValue();
            }
        int minValue = Integer.MAX_VALUE;
        for (int x = 0; x < board.width; x++) {
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = max(false);
            board.revert();
            if (value < minValue) {
                minValue = value;
                if (start) move = x;
            }
        }
        return minValue;
    }
    
    private int max(boolean start, int alpha, int beta) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return getWinValue();
            }
        for (int x = 0; x < board.width; x++) {
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = min(false, alpha, beta);
            if (start) System.out.println(x + " -> " + value);
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
    
    private int min(boolean start, int alpha, int beta) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return -getWinValue();
            }
        for (int x = 0; x < board.width; x++) {
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = max(false, alpha, beta);
            board.revert();
            if (value < beta) {
                beta = value;
                if (value <= alpha)
                    return value;
                if (start) move = x;
            }
        }
        return beta;
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
    
    private int miniMax(boolean start) {
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (start) move = x;
                return getWinValue();
            }
        int maxValue = Integer.MIN_VALUE;
        for (int x = 0; x < board.width; x++) {
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = -miniMax(false);
            board.revert();
            if (value > maxValue) {
                maxValue = value;
                if (start) move = x;
            }
        }
        return maxValue;
    }
}
