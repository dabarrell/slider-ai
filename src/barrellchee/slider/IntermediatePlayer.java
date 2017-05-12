package barrellchee.slider;

import aiproj.slider.Move;
import aiproj.slider.SliderPlayer;

public class IntermediatePlayer implements SliderPlayer {

	private CompactBoard board = new CompactBoard();
    private Character player;

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
		this.board.initBoard(dimension, board);
        this.player = player;
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
			try {
				board.update(move);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		MinimaxNode node = new MinimaxNode(this.board, this.player, false);
		int c = minimax(node, 2, this.player.equals('H') ? true : false);
		Move move = node.getMove(c, this.player.equals('H') ? 'V' : 'H');
		update(move);
		return move;
	}

	/**
	 * Minimax algorithm
	 * @param node
	 * @param depth
	 * @param maximizingPlayer
	 * @return
	 */
	private int minimax(MinimaxNode node, int depth, boolean maximizingPlayer) {
		int bestValue;
		if (depth == 0 || node.isTerminal()) {
			return node.getHeuristic(maximizingPlayer? 'H' : 'V');
		}
		if (maximizingPlayer) {
			bestValue = Integer.MIN_VALUE;
			for (Move key: node.getKeys()) {
				MinimaxNode child = node.getChild(key);
				int v = minimax(child, depth - 1, false);
				bestValue = max(bestValue, v);
			}
		} else {
			bestValue = Integer.MAX_VALUE;
			for (Move key: node.getKeys()) {
				MinimaxNode child = node.getChild(key);
				int v = minimax(child, depth - 1, true);
				bestValue = min(bestValue, v);
			}
		}
		return bestValue;
	}

	/**
	 * Returns the maximum of two integers
	 * @param x
	 * @param y
	 * @return
	 */
	private int max(int x, int y) {
		return (x > y) ? x : y;
	}

	/**
	 * Returns the minimum of two integers
	 * @param x
	 * @param y
	 * @return
	 */
	private int min(int x, int y) {
		return (x < y) ? x : y;
	}

}
