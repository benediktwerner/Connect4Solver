package de.benedikt_werner.connect4solver;

import java.util.Scanner;

public class Solver {
    private Board board;
    private int move;
    private int startingDepth;
    
    public static void main(String[] a) {
        Solver s = new Solver(new Board(5, 5));
        s.board.playSequence("001122");
        s.board.printBoard();
        int[] result = s.miniMax(1, 26);
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
            int[] result = miniMax(1, 26);
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
    public int[] miniMax(int player, int depth) {
        move = -1;
        startingDepth = depth;
        int value = miniMax(player, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
        //int value = max(depth, Integer.MIN_VALUE, Integer.MIN_VALUE);
        return new int[]{move, value};
    }
    
    private int miniMax(int player, int depth, int alpha, int beta) {
        if (depth == 0)
            return evaluateBoard();
        if (board.isFull())
            return 0;
        for (int x = 0; x < board.width; x++)
            if (board.canPlay(x) && board.isWinningMove(x)) {
                if (depth == startingDepth)
                    move = x;
                return board.width * board.height - board.history.size();
            }
        
        int maxValue = alpha;
        for (int x = 0; x < board.width; x++) {
            if (depth == startingDepth)
                System.out.println(x);
            else if (depth == startingDepth-1)
                System.out.println("-" + x);
            else if (depth == startingDepth-2)
                System.out.println("--" + x);
            if (!board.canPlay(x)) continue;
            board.placeStone(x);
            int value = -miniMax(-player, depth-1, -beta, -maxValue);
            if (startingDepth - depth < 3)
                System.out.println("> " + value);
            board.revert();
            if (value > maxValue) {
                maxValue = value;
                if (maxValue >= beta) // gibt trotzdem maxValue zurück. kann das stimmen??
                    break;
                if (depth == startingDepth)
                    move = x;
            }
        }
        return maxValue;
    }
    
    private int evaluateBoard() {
        System.out.println("Evaluating board");
        return 0;
    }
}
