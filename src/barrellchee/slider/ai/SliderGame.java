package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderGame {
    private SliderState initialState;
    private ArrayList<Double> weights = new ArrayList<>();

    public <T extends SliderBoard> SliderGame(int dimension, String board, Class<T> boardClass) {
        initialState = new SliderState(dimension, board, boardClass);
        initWeights();
    }

    public void initWeights() {
        weights.add(0,0.6D);
        weights.add(1,0.2);
        weights.add(2,10D);
        weights.add(3,0.5);
        weights.add(4,0.1);
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public SliderState getInitialState() {
        return this.initialState;
    }

    public Character getPlayer(SliderState state) {
        return state.getPlayerToMove();
    }

    public List<Move> getActions(SliderState sliderState) {
        return sliderState.getBoard().getMoves(sliderState.getPlayerToMove());
    }

    public SliderState getResult(SliderState state, Move move) {
        if (move != null && move.i == -1) {
            move = null;
        }
        SliderState result = state.clone();

        result.makeMove(move);
//        System.out.println(move);
//        result.getBoard().printBoard();
        return result;
    }

    public boolean isTerminal(SliderState state) {
        return state.isFinished();
    }

    public double getUtility(SliderState state) {
        Double result = state.getUtility();
        if(result != null) {
            return result;
        } else {
            throw new IllegalArgumentException("State is not terminal.");
        }

    }

    public List<WeightFeature> evalFeatures(SliderState state, Character player) {
        List<WeightFeature> list = new ArrayList<>();
        SliderBoard board = state.getBoard();

        // Features
        // 1 - Moves made towards end
        list.add(new WeightFeature(weights.get(0),board.movesMadeTowardsEnd(player)));

        // 2 - Opponent's pieces being blocked
        list.add(new WeightFeature(weights.get(1),board.fracPiecesBlockingOpp(player)));

        // 3 - Fraction of removed pieces
        list.add(new WeightFeature(weights.get(2),board.fracRemovedPieces(player)));

        // 4 - Fraction of unblocked pieces
        list.add(new WeightFeature(weights.get(3),board.fracUnblockedPieces(player)));

        // 5 - Inverse of number of moves made
        list.add(new WeightFeature(weights.get(4),1.0/state.getMoves()));


        return list;
    }

//
//    public List<WeightFeatureFunc> evalFeatures2(SliderState state) {
//        List<WeightFeatureFunc> list = new ArrayList<>();
//        SliderBoard board = state.getBoard();
//
//        ArrayList<Double> weights = new ArrayList<>();
//
//
//
//        // Features
//        // 1 - Moves made towards end
//        list.add(new WeightFeatureFunc(0.01,player1 -> board.movesMadeTowardsEnd(player1)));
//
//        // 2 - Opponent's pieces being blocked
//        list.add(new WeightFeature(0.1,board.fracPiecesBlockingOpp(player)));
//
//        // 3 - Fraction of removed pieces
//        list.add(new WeightFeature(0.5,board.fracRemovedPieces(player)));
//
//        // 4 - Fraction of unblocked pieces
//        list.add(new WeightFeature(0.05,board.fracUnblockedPieces(player)));
//
//        // 5 - Inverse of number of moves made
//        list.add(new WeightFeature(0.05,1.0/state.getMoves()));
//
//
//        return list;
//    }

    /**
     *
     * @param state
     * @param player
     * @return true if previous moves mean branch should be abandoned
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

    public class WeightFeatureFunc {
        private double weight;
        private Command func;

        WeightFeatureFunc(double weight, Command func) {
            this.weight = weight;
            this.func = func;
        }

        public double getWeight() {
            return weight;
        }

        public Double calculate(Character player) {
            return func.apply(player);
        }
    }

    @FunctionalInterface
    public interface Command {
        double apply(Character player);
    }

}
