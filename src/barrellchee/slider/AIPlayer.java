package barrellchee.slider;

import aiproj.slider.Move;
import barrellchee.slider.ai.SliderAlphaBetaSearch;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;
import barrellchee.slider.ai.TDLeaf;

import java.util.ArrayList;

/**
 * Slider game player that utilises a search algorithm to determine next moves, keeps
 * internal track of state.
 *
 * COMP30024 Artificial Intelligence
 * Project Part B - Sem 1 2017
 * @author David Barrell (520704), Ivan Chee (736901)
 */
public class AIPlayer implements aiproj.slider.SliderPlayer {

    private static final Class BOARD_CLASS = ArrayListSliderBoard.class;
    private static final boolean LOGGING = false;
    private static final boolean RUNNING_TDLEAF = true;

    private char player;
    private SliderGame game;
    private SliderState currState;
    private SliderAlphaBetaSearch search;

    private ArrayList<SliderState> stateHistory;

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
    @SuppressWarnings("unchecked")
    public void init(int dimension, String board, char player) {
        this.player = player;
        this.game = new SliderGame(dimension, board, BOARD_CLASS);

        this.currState = game.getInitialState();
        if (RUNNING_TDLEAF) {
            stateHistory = new ArrayList<>();
            stateHistory.add(currState);
        }

        search = new SliderAlphaBetaSearch(game,-1D,1D,2);

        if (LOGGING) {
            search.enableLogging();
            System.out.println("Print board");
            currState.getBoard().printBoard();
            System.out.println();
        }

    }

    /**
     * Updates the internal state with a move.
     *
     * @param move A Move object, which may be null (indicating a pass). Also, before the first
     * move at the beginning of the game, move = null.
     */
    @Override
    public void update(Move move) {
        if (move != null) {
            currState = game.getResult(currState, move);
            if (RUNNING_TDLEAF) {
                stateHistory.add(currState);
            }
        }

    }

    /**
     * Requests the next move of this player.
     *
     * @return a Move object representing the move to make at this point of the game,
     * or null if there are no legal moves.
     */
    @Override
    public Move move() {
        currState.setPlayerToMove(player);

        Move move = search.makeDecision(currState);

        if (LOGGING) {
            double timePerNode = search.getTimeElapsed() / search.getNodesExpanded();
            System.out.println("Depth searched: " + search.getMaxDepth()
                        + ", nodes expanded: " + search.getNodesExpanded()
                        + ", time per node: " + timePerNode + "ms");

            if (move == null) {
                System.out.println("No possible move for AI (" + player + ")");
            }

            System.out.println(currState.getPlayerToMove() + " makes move: " + move);
        }

        update(move);

        return move;
    }

    public void runTDLeaf() {
        if (!RUNNING_TDLEAF) {
            return;
        }

        TDLeaf tdLeaf = new TDLeaf(stateHistory, game, player, 0.5, 0.7);
        System.out.println("Updated weights:");
        for (Double w : tdLeaf.updateWeights(game.getWeights())) {
            // Replaced with output to file when running learning, to be read in for the
            // following game
            System.out.println(w);
        }
    }
}