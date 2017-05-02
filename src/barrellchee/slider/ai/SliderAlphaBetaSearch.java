package barrellchee.slider.ai;

import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aiproj.slider.Move;

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

    @Override
    protected boolean isSignificantlyBetter(double newUtility, double utility) {
        return super.isSignificantlyBetter(newUtility, utility);
    }

    @Override
    protected boolean hasSafeWinner(double resultUtility) {
        return super.hasSafeWinner(resultUtility);
    }

    @Override
    protected double eval(SliderState sliderState, Character character) {
        return super.eval(sliderState, character);
    }

    @Override
    public List<Move> orderActions(SliderState sliderState, List<Move> moves, Character character, int depth) {
        return super.orderActions(sliderState, moves, character, depth);
    }
}
