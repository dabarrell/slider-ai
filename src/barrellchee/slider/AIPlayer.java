package barrellchee.slider;

import aima.core.search.adversarial.AdversarialSearch;
import aiproj.slider.Move;
import barrellchee.slider.ai.SliderAlphaBetaSearch;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

/**
 * Created by barrelld on 1/05/2017.
 */
public class AIPlayer implements aiproj.slider.SliderPlayer {
    char player;
    SliderGame game;
    SliderState currState;
    AdversarialSearch<SliderState, Move> search;

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
        this.player = player;
        this.game = new SliderGame(dimension, board);
        this.currState = game.getInitialState();

        search = SliderAlphaBetaSearch
                .createFor(game,1);

//        ((IterativeDeepeningAlphaBetaSearch)search).setLogEnabled(true);

        System.out.println("Print board");
        currState.getBoard().printBoard();
        System.out.println();
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

        Move action = search.makeDecision(currState);

        if (action == null) {
            System.out.println("No possible move for AI (" + player + ")");
        }

        System.out.println(currState.getPlayerToMove() + " makes move: " + action);
        currState = game.getResult(currState, action);
//        System.out.println(currState);

        return action;
    }
}