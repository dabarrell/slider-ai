package barrellchee.slider;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

import java.util.List;
import java.util.Scanner;

/**
 * Created by barrelld on 5/05/2017.
 */
public class ArraySliderBoard extends SliderBoard {
    private String[][] board;
    private int dimension;

    @Override
    public void initBoard(int dimension) {
        this.dimension = dimension;
    }

    /**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board     The string representation of the board.
     */
    @Override
    public void initBoard(int dimension, String board) {
        if (board == null) {
            initBoard(dimension);
            return;
        }
        this.dimension = dimension;

        Scanner scanner = new Scanner(board);

        this.board = new String[dimension][dimension];
        int i = 0;

        while (scanner.hasNext()) {
            // Check this!
            this.board[dimension-1-i] = scanner.nextLine().split(" ");
            i++;
        }
    }

    /**
     * Updates a board with a particular move.
     *
     * @param move The move that will be performed on the board.
     */
    @Override
    public Character update(Move move) {
        if (move == null || move.i == -1) {
            return null;
        }

        return null;
    }

    @Override
    public Boolean isMovingOffBoard(Move move) {
        return null;
    }

    /**
     * Prints the board to std out.
     */
    @Override
    public void printBoard() {

    }

    /**
     * Counts the available moves for a particular player.
     *
     * @param p A player - either 'H' or 'V'.
     * @return a count of the available moves for said player.
     */
    @Override
    public int countMoves(char p) {
        return 0;
    }

    /**
     * Returns a list of possible moves for a particular player.
     *
     * @param p A player - either 'H' or 'V'.
     * @return a list of possible moves for said player.
     */
    @Override
    public List<Move> getMoves(char p) {
        return null;
    }

    /**
     * Clones the board.
     *
     * @return a cloned board.
     */
    @Override
    public SliderBoard cloneBoard() {
        return null;
    }

    /**
     * Calculates the winner, if there is one.
     *
     * @return the char value of winner, or null of none.
     */
    @Override
    public Character getWinner() {
        return null;
    }

    @Override
    public boolean isEmpty(int i, int j) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public boolean equals(Object anObj) {
        return false;
    }
}
