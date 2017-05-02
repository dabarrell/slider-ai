package barrellchee.slider.ai;

import aima.core.search.adversarial.Game;

import aiproj.slider.Move;

import java.util.List;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderGame implements Game<SliderState, Move, Character> {
    SliderState initialState = new SliderState();

    public SliderGame() {
    }

    @Override
    public SliderState getInitialState() {
        return this.initialState;
    }

    @Override
    public Character[] getPlayers() {
        return new Character[]{'H','V'};
    }

    @Override
    public Character getPlayer(SliderState state) {
        return state.getPlayerToMove();
    }

    @Override
    public List<Move> getActions(SliderState sliderState) {
        return sliderState.getMoves();
    }

    @Override
    public SliderState getResult(SliderState state, Move move) {
        SliderState result = state.clone();
        result.makeMove(move);
        return result;
    }

    @Override
    public boolean isTerminal(SliderState state) {
        return state.isFinished();
    }

    @Override
    public double getUtility(SliderState state, Character player) {
        double result = state.getUtility();
        if(result != -1.0D) {
            if (player == 'V') {
                result = 1.0D - result;
            }

            return result;
        } else {
            throw new IllegalArgumentException("State is not terminal.");
        }

    }
}
