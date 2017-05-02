package barrellchee.slider;

import aima.core.agent.Action;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.framework.SearchAgent;
import aima.core.search.uninformed.DepthFirstSearch;
import aiproj.slider.Move;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

import java.util.List;

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

        search = IterativeDeepeningAlphaBetaSearch
                .createFor(game,0D,1D,1);

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
     * @param move A Move object representing the previous move made by the
     * opponent, which may be null (indicating a pass). Also, before the first
     * move at the beginning of the game, move = null.
     */
    @Override
    public void update(Move move) {
        if (move == null) {
//            System.out.println("Player passes " + player);
        } else {
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
     * @return a Move object representing the move you would like to make
     * at this point of the game, or null if there are no legal moves.
     */
    @Override
    public Move move() {
        if (currState.getBoard().countMoves(player) <= 0) {
            System.out.println("No moves available for AI");
            return null;
        }
        Move action = search.makeDecision(currState);
        System.out.println(action);
        currState = game.getResult(currState, action);
        System.out.println(currState);
        return action;
    }
}