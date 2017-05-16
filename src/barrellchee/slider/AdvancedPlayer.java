package barrellchee.slider;

import java.util.Set;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;
import barrellchee.slider.ai.DefaultNode;
import barrellchee.slider.ai.SliderMonteCarloTreeSearch;
import barrellchee.slider.ai.SliderTransition;
import barrellchee.slider.ai.Transition;

public class AdvancedPlayer<T> implements SliderPlayer, Transition {

	private SliderMonteCarloTreeSearch<SliderTransition, DefaultNode<SliderTransition>> mcts;

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
		this.mcts = new CompactBoard2();
		((CompactBoard2)this.mcts).initBoard(dimension, board, 'H');
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
		if (move != null) {
//			System.out.println(((CompactBoard2)this.mcts).getCurrentPlayer() + ": Opponent has made a move " + move.toString());
			((CompactBoard2)this.mcts).makeTransition(new SliderTransition(
				((CompactBoard2)this.mcts).getDimension(),
				((CompactBoard2)this.mcts).getCurrentPlayer() == 0 ? 'H' : 'V',
				move,
				((CompactBoard2)this.mcts).toString()));
//			((CompactBoard2)this.mcts).printBoard();
//			System.out.println(((CompactBoard2)this.mcts).getCurrentPlayer() + ": Updated board based on opponent's move");
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
//		System.out.println(((CompactBoard2)this.mcts).getCurrentPlayer() + ": Preparing to move");	
		SliderTransition transition = null;
        while (!mcts.isOver()) {
            Set<SliderTransition> transitions = mcts.getPossibleTransitions();
            if (!transitions.isEmpty()) {
                transition = (SliderTransition) mcts.getBestTransition();
//              System.out.println(((CompactBoard2)this.mcts).getCurrentPlayer() + ": Executing move " + transition.getMove().toString());
                if (transition != null) {
	                mcts.doTransition(transition);
	                mcts.reset();
	                return transition.getMove();
                }
            }
        }
		return null;
	}

}
