package barrellchee.slider;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderMinimaxSearch;
import barrellchee.slider.ai.SliderState;

public class IntermediatePlayer implements SliderPlayer {

	private final static Class<CompactBoard> BOARD_CLASS = CompactBoard.class;

	private SliderMinimaxSearch search;
	private Character player;
	private SliderState state;
	private SliderGame game;

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
		this.player = player;
		this.game = new SliderGame(dimension, board, BOARD_CLASS);
		this.search = new SliderMinimaxSearch(this.game, 4);
		this.state = this.game.getInitialState();
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
			this.state = this.game.getResult(this.state, move);
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
		this.state.setPlayerToMove(this.player);
		Move move = this.search.makeDecision(this.state);
		update(move);
		this.state = this.game.getResult(this.state, move);
		return move;
	}

}
