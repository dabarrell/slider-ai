package barrellchee.slider;

import java.util.Scanner;

import aiproj.slider.Move;

/**
 * Created by barrelld on 1/05/2017.
 */
public class HumanPlayer implements aiproj.slider.SliderPlayer {
    Board board = new ArrayListBoard();
    char player;

    /**
     * Prepare a newly created SliderPlayer to play a game of Slideron a given
     * board, as a given player.
     *
     * @param dimension The width and height of the board in cells
     * @param board A string representation of the initial state of the board,
     * as described in the part B specification
     * @param player 'H' or 'V', corresponding to which pieces the player will
     * control for this game ('H' = Horizontal, 'V' = Vertical)
     */
    @Override
    public void init(int dimension, String board, char player) {
        this.board.initBoard(dimension,board);
        this.player = player;
//        System.out.println(player);
//        this.board.printBoard();
    }

    /**
     * Notify the player of the last move made by their opponent. In response to
     * this method, your player should update its internal representation of the
     * board state to reflect the result of the move made by the opponent.
     *
     * @param move A Move object representing the previous move made by the
     * opponent, which may be null (indicating a pass). Also, before the first
     * move at the beginning of the game, move = null.
     */
    @Override
    public void update(Move move) {
        if (move == null) {
//            System.out.println("Player passes " + player);
        } else {
            try {
                board.update(move);
//                board.printBoard();
            } catch (Exception e) {
//                System.err.println("Internal board update failed: " + e.getMessage());
//                e.printStackTrace();
            }
        }

    }

    /**
     * Request a decision from the player as to which move they would like to
     * make next. Your player should consider its options and select the best
     * move available at the time, according to whatever strategy you have
     * developed.
     *
     * The move returned must be a legal move based on the current
     * state of the game. If there are no legal moves, return null (pass).
     *
     * Before returning your move, you should also update your internal
     * representation of the board to reflect the result of the move you are
     * about to make.
     *
     * @return a Move object representing the move you would like to make
     * at this point of the game, or null if there are no legal moves.
     */
    @Override
    public Move move() {
        System.out.println(player + "'s turn:");
        if (board.countMoves(player) == 0) {
            System.out.println("No moves available");
            return null;
        }

        Scanner s = new Scanner(System.in);
        int i = s.nextInt();
        int j = s.nextInt();
        String dir = s.next();
        Move move = null;
        switch (dir) {
            case "up":
                move = new Move(i, j, Move.Direction.UP);
                break;
            case "down":
                move = new Move(i, j, Move.Direction.DOWN);
                break;
            case "left":
                move = new Move(i, j, Move.Direction.LEFT);
                break;
            case "right":
                move = new Move(i, j, Move.Direction.RIGHT);
                break;
        }
        update(move);
        s.close();
        return move;
    }
}