package de.benedikt_werner.connect4solver;

import java.util.LinkedList;

public class Board {
    public int[][] board;
    public LinkedList<Integer> history;
    public int[] heights;
    public int player;
    public int width, height;
    
    public Board() {
        this(7, 6);
    }
    
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        board = new int[height][width];
        history = new LinkedList<>();
        heights = new int[width];
        player = 1;
    }
    
    public void placeStone(int column) {
        if (heights[column] == height)
            throw new IllegalArgumentException("Column " + column + " is already full!");
        board[heights[column]][column] = player;
        player *= -1;
        heights[column]++;
        history.addLast(column);
    }
    
    public void revert() {
        int column = history.removeLast();
        heights[column]--;
        player *= -1;
        board[heights[column]][column] = 0;
    }
    
    public boolean canPlay(int column) {
        return heights[column] < height;
    }
    
    public boolean isFull() {
        return history.size() >= width * height;
    }
    
    public boolean isWinningMove(int column) {
        int row = heights[column];
        if (row >= 3
                && board[row-1][column] == player
                && board[row-2][column] == player
                && board[row-3][column] == player)
            return true;
        
        for (int dy = -1; dy <=1; dy++) {
            int nb = 0;
            for (int dx = -1; dx <= 1; dx += 2)
                for (int x = column+dx, y = row+dx*dy; x >= 0 && x < width && y >= 0 && y < height && board[y][x] == player; nb++) {
                    x += dx;
                    y += dx*dy;
                }
            if(nb >= 3) return true;
        }
        return false;
    }
    
    public void playSequence(int[] sequence) {
        boolean gameOver = false;
        for (int i : sequence) {
            if (gameOver) throw new IllegalStateException("Game is already over!");
            if (isWinningMove(i)) gameOver = true;
            placeStone(i);
            if (isFull()) gameOver = true;
        }
    }
    
    public void playSequence(String sequence) {
        int[] seq = new int[sequence.length()];
        for (int i = 0; i < seq.length; i++)
            seq[i] = sequence.charAt(i) - '0';
        playSequence(seq);
    }
    
    public void printBoard() {
        System.out.println("+-----+");
        for (int y = height-1; y >= 0; y--) {
            String line = "|";
            for (int x = 0; x < width; x++) {
                switch (board[y][x]) {
                    case -1: line += "O"; break;
                    case  1: line += "X"; break;
                    default: line += ".";
                }
            }
            System.out.println(line + "|");
        }
        System.out.println("+-----+");
    }
}
