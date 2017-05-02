package barrellchee.slider.ai;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aiproj.slider.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by barrelld on 2/05/2017.
 */
public class SliderAlphaBetaSearch extends IterativeDeepeningAlphaBetaSearch<SliderState, Move, Character> {
    public SliderAlphaBetaSearch(Game<SliderState, Move, Character> game, int time) {
        super(game, 0.0, 1.0, time);
    }

    public static SliderAlphaBetaSearch createFor(Game<SliderState, Move, Character> game, int time) {
        return new SliderAlphaBetaSearch(game, time);
    }

    public Move makeDecision(SliderState state) {
        Move result = super.makeDecision(state);
        if (result.i == -1) {
            return null;
        }
        return result;
    }

    @Override
    protected boolean isSignificantlyBetter(double newUtility, double utility) {
//        return super.isSignificantlyBetter(newUtility, utility);
        return newUtility - utility > (utilMax - utilMin) * 0.4;
    }

    @Override
    protected boolean hasSafeWinner(double resultUtility) {
//        return super.hasSafeWinner(resultUtility);
        return Math.abs(resultUtility - (utilMin + utilMax) / 2) > 0.4
                * utilMax - utilMin;
    }

    @Override
    protected double eval(SliderState state, Character character) {
        // TODO: use a cache to store identical cases
        double value = super.eval(state, character);
        if (hasSafeWinner(value)) {
            if (value > (utilMin + utilMax) / 2)
                value -= state.getMoves() / 1000.0;
            else
                value += state.getMoves() / 1000.0;
        }
        return value;
    }

    /**
     * Orders moves with respect to the state's analyseMoveValue() method
     */
    @Override
    public List<Move> orderActions(SliderState state,
                                      List<Move> moves, Character player, int depth) {
        List<Move> result = moves;
        if (depth == 0) {
            List<MoveValuePair> actionEstimates = new ArrayList<>(
                    moves.size());
            for (Move move : moves)
                actionEstimates.add(new MoveValuePair(move,
                        state.analyseMoveValue(move, player)));
            Collections.sort(actionEstimates);
            result = new ArrayList<>();
            for (MoveValuePair pair : actionEstimates) {
                result.add(pair.getMove());
                System.out.println(pair.getMove() + " valued at " + pair.getValue());
            }

        }
        return result;
    }

    private class MoveValuePair implements Comparable<MoveValuePair> {
        private Move move;
        private double value;

        public MoveValuePair(Move move, double utility) {
            this.move = move;
            this.value = utility;
        }

        public Move getMove() {
            return move;
        }

        public double getValue() {
            return value;
        }

        @Override
        public int compareTo(MoveValuePair pair) {
            return (int) (pair.value - value);
        }
    }

}
