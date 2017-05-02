package barrellchee.slider;

import aiproj.slider.Move;

import java.util.List;

/**
 * Created by barrelld on 1/05/2017.
 */
abstract public class SliderBoard {

    abstract public void initBoard(int dimension);

    /**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board The string representation of the board.
     */
    abstract public void initBoard(int dimension, String board);

    /**
     * Updates a board with a particular move.
     *
     * @param move The move that will be performed on the board.
     */
    abstract public void update(Move move);

    /**
     * Prints the board to std out.
     */
    abstract public void printBoard();

    /**
     * Counts the available moves for a particular player.
     *
     * @param p A player - either 'H' or 'V'.
     * @return a count of the available moves for said player.
     */
    abstract public int countMoves(char p);

    /**
     * Count the total number of moves available.
     *
     * @return the total number of moves available.
     */
    public int countMoves() {
        return countMoves('V') + countMoves('H');
    }

    /**
     * Returns a list of possible moves for a particular player.
     *
     * @param p A player - either 'H' or 'V'.
     * @return a list of possible moves for said player.
     */
    abstract public List<Move> getMoves(char p);

    /**
     * Clones the board.
     *
     * @return a cloned board.
     */
    abstract public SliderBoard cloneBoard();

    /**
     * Calculates the winner, if there is one.
     *
     * @return the char value of winner, or null of none.
     */
    abstract public Character getWinner();

    /**
     * Determines if board is in finished state.
     *
     * @return true if there is a winner, or a statemate, false otherwise
     */
    public boolean isFinished() {
        return (getWinner() != null) || (countMoves() == 0);
    }

    abstract public boolean isEmpty(int i, int j);

    @Override
    abstract public String toString();

    @Override
    abstract public boolean equals(Object anObj);

}
