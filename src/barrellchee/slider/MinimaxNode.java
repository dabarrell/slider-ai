package barrellchee.slider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import aiproj.slider.Move;

public class MinimaxNode {

	private Map<Move, MinimaxNode> children;
	private CompactBoard board;

	public MinimaxNode(CompactBoard board, Character p, Boolean leaf) {
		this.board = board;
		this.children = new HashMap<Move, MinimaxNode>();
		if (!leaf) {
			for (Integer i: board.getValidPieces(p)) {
				for (Move move: board.getValidMoves(i, p)) {
					CompactBoard state = new CompactBoard();
					state.initBoard(board.getDimension(), board.toString());
					try {
						state.update(move);
						if (p.equals('H')) {
							this.children.put(move, new MinimaxNode(state, 'V', true));
						} else {
							this.children.put(move, new MinimaxNode(state, 'H', true));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public boolean isTerminal() {
		return this.children.size() == 0;
	}

	public int getHeuristic(Character p) {
		Integer h = 0;
		if (p.equals('H')) {
			for (Integer i: this.board.getValidPieces(p)) {
				h += (this.board.getDimension() - this.board.getX(i));
			}
		} else {
			for (Integer i: this.board.getValidPieces(p)) {
				h += (this.board.getDimension() - this.board.getY(i));
			}
		}
		return h;
	}

	public Set<Move> getKeys() {
		return children.keySet();
	}

	public MinimaxNode getChild(Move key) {
		return this.children.get(key);
	}

	public Move getMove(Integer h, Character p) {
		for (Move key: getKeys()) {
			if (getChild(key).getHeuristic(p) == h) {
				return key;
			}
		}
		return null;
	}

}
