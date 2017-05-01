package barrellchee.slider;

import aiproj.slider.Move;

/**
 * Created by barrelld on 1/05/2017.
 */
public interface Board {

    /**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board The string representation of the board.
     */
    void initBoard(int dimension, String board);

    /**
     * Updates a board with a particular move.
     *
     * @param move The move that will be performed on the board.
     * @throws Exception
     */
    void update(Move move) throws Exception;

    /**
     * Prints the board to std out.
     */
    void printBoard();

    /**
     * Counts the available moves for a particular player.
     * @param p A player - either 'H' or 'V'.
     * @return a count of the available moves for said player.
     */
    int countMoves(char p);

}
