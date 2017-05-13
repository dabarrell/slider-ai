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
    abstract public Character update(Move move);

    abstract public Boolean isMovingOffBoard(Move move);

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
     * Returns the number of moves made towards the players end.
     *
     * @param player Player to check
     * @return total moves made
     */
    abstract public int movesMadeTowardsEnd(Character player);


    /**
     * Returns the number of the player's pieces that are blocking
     * the opponent's pieces
     *
     * @param player Player to check
     * @return number of pieces
     */
    abstract public double fracPiecesBlockingOpp(Character player);

    /**
     * Returns the fraction of pieces that the player has removed
     *
     * @param player Player to check
     * @return fraction of player's pieces removed
     */
    abstract public double fracRemovedPieces(Character player);

    abstract public double fracUnblockedPieces(Character player);

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
