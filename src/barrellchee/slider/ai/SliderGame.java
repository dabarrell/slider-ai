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

    public boolean checkMoveHistory(SliderState state, Character player) {
        // TODO: what player to use here?
        final int MAX_STRAIGHT_MOVES = 3;

        List<Move> moves = state.getHistory(player);

        if (moves.size() < 2) {
            return false;
        }

        Move last = moves.get(moves.size()-1);
        Move secondLast = moves.get(moves.size()-2);

        if (oppositeMoves(last.d, secondLast.d)) {
            return true;
        }

        if (moves.size() < MAX_STRAIGHT_MOVES) {
            return false;
        }

        // TODO: change this to any sideways movement of any piece of the same player
        return moves.stream()
                .skip(Math.max(0, moves.size() - MAX_STRAIGHT_MOVES))
                .allMatch(move -> move.d.equals(last.d))
                && ((player == 'V' && last.d != Move.Direction.UP)
                || (player == 'H' && last.d != Move.Direction.RIGHT));
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
}
