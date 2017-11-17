package barrellchee.slider;

import aiproj.slider.Move;
import barrellchee.slider.ai.SliderAlphaBetaSearch;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is a random player, used to generate games for machine learning.
 * 70% of the time, it will choose a forward move, if one exists.
 * 30% of the time, it will choose a sideways move, if one exists.
 *
 * Created by barrelld on 1/05/2017.
 */
public class RandomPlayer implements aiproj.slider.SliderPlayer {

    private static final Class BOARD_CLASS = ArrayListSliderBoard.class;

    private char player;
    private SliderGame game;
    private SliderState currState;

    boolean flag = false;

    /**
     * Prepare a newly created SliderPlayer to play a game of Slider on a given
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
        this.player = player;
        this.game = new SliderGame(dimension, board, BOARD_CLASS);
        this.currState = game.getInitialState();
    }

    /**
     * Notify the player of the last move made by their opponent. In response to
     * this method, your player should update its internal representation of the
     * board state to reflect the result of the move made by the opponent.
     *
     * @param move A MoveWrapper object representing the previous move made by the
     * opponent, which may be null (indicating a pass). Also, before the first
     * move at the beginning of the game, move = null.
     */
    @Override
    public void update(Move move) {
        if (move != null) {
            currState = game.getResult(currState, move);
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
     * @return a MoveWrapper object representing the move you would like to make
     * at this point of the game, or null if there are no legal moves.
     */
    @Override
    public Move move() {
        currState.setPlayerToMove(player);
        flag = false;
        Move action;

        List<Move> actions = game.getActions(currState);

        if (actions.get(0).i == -1) {
            action = null;
            System.out.println("No possible move for random player (" + player + ")");
        } else {
            int rand = ThreadLocalRandom.current().nextInt(0, 10 + 1);
            if (rand <= 7) {
                action = chooseFwd(actions);
            } else {
                action = chooseSideways(actions);
            }
        }

        System.out.println(currState.getPlayerToMove() + " makes move: " + action);
        currState = game.getResult(currState, action);

        return action;
    }

    private Move chooseFwd(List<Move> moves) {
        List<Move> filtered = new ArrayList<>();
        for (Move m : moves) {
            if (player == 'V' && m.d == Move.Direction.UP) {
                filtered.add(m);
            } else if (player == 'H' && m.d == Move.Direction.RIGHT) {
                filtered.add(m);
            }
        }

        if (filtered.size() > 0) {
            int rand = ThreadLocalRandom.current().nextInt(0, filtered.size());
            return filtered.get(rand);
        }

        if (flag) {
            return null;
        }

        flag = true;
        return chooseSideways(moves);
    }

    private Move chooseSideways(List<Move> moves) {
        List<Move> filtered = new ArrayList<>();
        for (Move m : moves) {
            if (player == 'V' && (m.d == Move.Direction.LEFT || m.d == Move.Direction.RIGHT)) {
                filtered.add(m);
            } else if (player == 'H' && (m.d == Move.Direction.UP || m.d == Move.Direction.DOWN)) {
                filtered.add(m);
            }
        }

        if (filtered.size() > 0) {
            int rand = ThreadLocalRandom.current().nextInt(0, filtered.size());
            return filtered.get(rand);
        }

        if (flag) {
            return null;
        }

        flag = true;
        return chooseFwd(moves);
    }
}