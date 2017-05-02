package barrellchee.slider;

import aima.core.agent.Action;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.TreeSearch;
import aima.core.search.uninformed.DepthFirstSearch;
import aiproj.slider.Move;
import barrellchee.slider.ai.FunctionFactory;
import barrellchee.slider.ai.GoalTest;


import java.util.List;

/**
 * Created by barrelld on 1/05/2017.
 */
public class AIPlayer implements aiproj.slider.SliderPlayer {
    SliderBoard board = new ArrayListSliderBoard();
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
        try {
            Problem problem = new Problem(board,
                    FunctionFactory.getActionsFunction(),
                    FunctionFactory.getResultFunction(),
                    new GoalTest());
//            Search search = new AStarSearch(new TreeSearch(), new HeuristicFunction());
            Search search = new DepthFirstSearch(new TreeSearch());
            SearchAgent agent = new SearchAgent(problem, search);
            List<Action> actions = agent.getActions();
            System.out.println(agent.getActions());
            System.out.println(agent.getInstrumentation());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}