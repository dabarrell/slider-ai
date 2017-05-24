package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the game logic of the Slider game.
 *
 * This has been adapted from aima-java.
 *
 * COMP30024 Artificial Intelligence
 * Project Part B - Sem 1 2017
 * @author David Barrell (520704), Ivan Chee (736901)
 */
public class SliderGame {
    private SliderState initialState;
    private ArrayList<Double> weights = new ArrayList<>();

    public <T extends SliderBoard> SliderGame(int dimension, String board, Class<T> boardClass) {
        initialState = new SliderState(dimension, board, boardClass);
        initWeights();
    }

    /**
     * Initialises the weights. This function is overridden when learning via TDLeaf.
     */
    public void initWeights() {
        weights.add(0,0.1293D);
        weights.add(1,0.2167D);
        weights.add(2,0.8149D);
        weights.add(3,0.3425D);
        weights.add(4,-0.0144D);
    }

    /**
     * Gets weights.
     *
     * @return list of weights
     */
    public ArrayList<Double> getWeights() {
        return weights;
    }

    /**
     * Gets initial state.
     * @return initial state
     */
    public SliderState getInitialState() {
        return this.initialState;
    }

    /**
     * Finds next player to move given a state.
     *
     * @param state State to check
     * @return Player to move
     */
    public Character getPlayer(SliderState state) {
        return state.getPlayerToMove();
    }

    /**
     * Gets possible actions for the next player to move.
     *
     * @param sliderState State to check
     * @return List of possible moves
     */
    public List<Move> getActions(SliderState sliderState) {
        return sliderState.getBoard().getMoves(sliderState.getPlayerToMove());
    }

    /**
     * Gets result of making a move on a state.
     *
     * @param state Current state
     * @param move Move to make
     * @return New state, post-move
     */
    public SliderState getResult(SliderState state, Move move) {
        if (move != null && move.i == -1) {
            move = null;
        }
        SliderState result = state.clone();

        result.makeMove(move);
        return result;
    }

    /**
     * Determines whether state is terminal.
     *
     * @param state State to check
     * @return True if finished, false otherwise
     */
    public boolean isTerminal(SliderState state) {
        return state.isFinished();
    }

    /**
     * Gets final utility of a state/
     *
     * @param state State to check
     * @return Final utility
     *      -1 -> V has won
     *       0 -> Draw
     *      +1 -> H has won
     */
    public double getUtility(SliderState state) {
        Double result = state.getUtility();
        if(result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("State is not terminal.");
        }

    }

    /**
     * Calculate a list of weight-feature pairs
     *
     * @param state State to check
     * @param player Player to check
     * @return List of pairs
     */
    public List<WeightFeature> evalFeatures(SliderState state, Character player) {
        List<WeightFeature> list = new ArrayList<>();
        SliderBoard board = state.getBoard();

        // Features
        // 1 - Moves made towards end
        list.add(new WeightFeature(weights.get(0),board.movesMadeTowardsEnd(player)));

        // 2 - Opponent's pieces being blocked
        list.add(new WeightFeature(weights.get(1),board.piecesBlockingOpp(player)));

        // 3 - Fraction of removed pieces
        list.add(new WeightFeature(weights.get(2),board.fracRemovedPieces(player)));

        // 4 - Fraction of unblocked pieces
        list.add(new WeightFeature(weights.get(3),board.fracUnblockedPieces(player)));

        // 5 - Inverse of number of moves made
        list.add(new WeightFeature(weights.get(4),state.getMoves()));


        return list;
    }

    /**
     * Checks recent moves for a given player to determine if the current branch
     * should be abandoned. Reduces searches of less valuable branches.
     * @param state State to check
     * @param player Player to check
     * @return True if:
     *              - player's last two moves were in opposite directions
     *              - player's last MAX_STRAIGHT_MOVES were sideways
     */
    public boolean checkMoveHistory(SliderState state, Character player) {
        final int MAX_STRAIGHT_MOVES = 3;

        List<Move> moves = state.getHistory(player);

        if (moves.size() < 2) {
            return false;
        }

        Move last = moves.get(moves.size()-1);
        Move secondLast = moves.get(moves.size()-2);

        if (last != null && secondLast != null
                && oppositeMoves(last.d, secondLast.d)) {
            return true;
        }

        if (last == null || moves.size() < MAX_STRAIGHT_MOVES) {
            return false;
        }

        return moves.stream()
                .skip(Math.max(0, moves.size() - MAX_STRAIGHT_MOVES))
                .allMatch(move -> isSideways(move, player));
    }

    /**
     * Determines if a move is sideways for a particular player.
     *
     * @param m Move to check
     * @param player Player to check
     * @return True if the move is sideways (e.g. left or right for 'V'), false otherwise
     */
    private boolean isSideways(Move m, char player) {
        if (m == null) {
            return false;
        }
        if (player == 'V' && (m.d == Move.Direction.LEFT || m.d == Move.Direction.RIGHT)) {
            return true;
        } else if (player == 'H' && (m.d == Move.Direction.UP || m.d == Move.Direction.DOWN)) {
            return true;
        }
        return false;
    }

    /**
     * Determines if two moves are opposites
     *
     * @param d1 First move
     * @param d2 Second move
     * @return True if d1 and d2 are in opposite directions, false otherwise
     */
    private boolean oppositeMoves(Move.Direction d1, Move.Direction d2) {
        switch (d1) {
            case UP:
                if (d2 == Move.Direction.DOWN)
                    return true;
                break;
            case DOWN:
                if (d2 == Move.Direction.UP)
                    return true;
                break;
            case LEFT:
                if (d2 == Move.Direction.RIGHT)
                    return true;
                break;
            case RIGHT:
                if (d2 == Move.Direction.LEFT)
                    return true;
                break;
        }
        return false;
    }

    /**
     * Stores the weight and value of a feature.
     */
    public class WeightFeature {
        private double weight;
        private double value;

        WeightFeature(double weight, double value) {
            this.weight = weight;
            this.value = value;
        }

        public double getWeight() {
            return weight;
        }

        public double getValue() {
            return value;
        }
    }

}
