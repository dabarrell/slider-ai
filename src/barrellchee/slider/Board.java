package barrellchee.slider;

import aiproj.slider.Move;

/**
 * Created by barrelld on 1/05/2017.
 */
public interface Board {

    void initBoard(int dimension, String board);
    void update(Move move) throws Exception;
    void printBoard();
    int countMoves(char p);

}
