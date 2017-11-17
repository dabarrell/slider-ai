package barrellchee.slider.montecarlo;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;
import barrellchee.slider.montecarlo.CompactBoard;
import barrellchee.slider.montecarlo.SliderMonteCarloTreeSearch;
import barrellchee.slider.montecarlo.SliderTransition;
import barrellchee.slider.montecarlo.Transition;

/**
 * Adapted from MonteCarloTreeSearch.java on GitHub by Antoine Vianey
 * https://github.com/avianey/mcts4j
 * Uses the monte carlo tree search algorithm to play a game of Slideron
 * @param <T> : A transition representing an atomic action that modifies the state
 * @author    : David Barrell, Ivan Chee
 */
public class MonteCarloPlayer<T> implements SliderPlayer, Transition {

	private SliderMonteCarloTreeSearch<Transition, Node<Transition>> mcts;
	private Boolean pass;

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
		this.mcts = new CompactBoard();
		if (player == 'H') {
			pass = false;
		} else {
			pass = true;
		}
		((CompactBoard)this.mcts).initBoard(dimension, board, 'H');
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
		if (pass) {
			mcts.doTransition(new SliderTransition(
					((CompactBoard)this.mcts).getDimension(),
					((CompactBoard)this.mcts).getCurrentPlayer() == 0 ? 'H' : 'V',
					move,
					((CompactBoard)this.mcts).toString()));
			mcts.reset();
		} else {
			pass = true;
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
		SliderTransition transition = null;
        while (!mcts.isOver()) {
            transition = (SliderTransition) mcts.getBestTransition();
            if (transition == null) {
            	mcts.next();
            	mcts.reset();
                break;
            }
            mcts.doTransition(transition);
            mcts.reset();
            return transition.getMove();
        }
		return null;
	}

}
