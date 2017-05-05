package barrellchee.slider.ai;

import aima.core.search.adversarial.Game;
import aiproj.slider.Move;
import barrellchee.slider.SliderBoard;

import java.util.List;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderGame implements Game<SliderState, Move, Character> {
    SliderState initialState;

    public <T extends SliderBoard> SliderGame(int dimension, String board, Class<T> boardClass) {
        initialState = new SliderState(dimension, board, boardClass);
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
        return sliderState.getBoard().getMoves(sliderState.getPlayerToMove());
    }

    @Override
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
