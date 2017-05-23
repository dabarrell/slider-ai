package barrellchee.slider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import aiproj.slider.Move;
import barrellchee.slider.ai.DefaultNode;
import barrellchee.slider.ai.SliderTransition;
import barrellchee.slider.ai.UCT;

/**
 * Compact representation of ArrayListBoard.java
 * @author David Barrell, Ivan Chee
 */
public class CompactBoard2 extends UCT<SliderTransition, DefaultNode<SliderTransition>> {

	private Character p;
	private Integer d;
	private Integer n;
	private ArrayList<Integer> bPieces;
	private ArrayList<Integer> hPieces;
	private ArrayList<Integer> vPieces;

	/**
     * Initiates the internal board.
     * @param dimension : The dimension of the board.
     * @param board     : The string representation of the board.
     */
	public void initBoard(int dimension, String board, Character player) {
		this.d = dimension;
		this.n = (int) Math.pow(this.d, 2);
		this.p = player;
		
		this.bPieces = new ArrayList<Integer>();
		this.hPieces = new ArrayList<Integer>();
		this.vPieces = new ArrayList<Integer>();
		
		Scanner s = new Scanner(board);
		for (int i = 0; s.hasNext(); i ++) {
			switch (s.next()) {
			case "B":
				this.bPieces.add(i);
				break;
			case "H":
				this.hPieces.add(i);
				break;
			case "V":
				this.vPieces.add(i);
				break;
			}
		}
		s.close();
	}

	/**
     * Updates a board with a particular move.
     * @param move : The move that will be performed on the board.
     */
	public void update(Move move, Integer i) {
//		System.out.println("PLAYER " + i + " ATTEMPTING TO MOVE " + move.toString() + " = " + (d * (d - move.j - 1) + move.i));
//		System.out.println("Before" + hPieces.toString());
//		System.out.println("Before" + vPieces.toString());
		if (move != null) {
			Integer n = d * (d - move.j - 1) + move.i;
			if (i == 0 && hPieces.contains(n)) {
				if (move.d.equals(Move.Direction.UP)) {
					if (!offBoard(n - d, move.d) && !inPieces(n - d)) {
						hPieces.add(n - d);
						hPieces.remove(n);
					}
				} else if (move.d.equals(Move.Direction.DOWN)) {
					if (!offBoard(n + d, move.d) && !inPieces(n + d)) {
						hPieces.add(n + d);
						hPieces.remove(n);
					}
				} else if (move.d.equals(Move.Direction.RIGHT)) {
					if (!offBoard(n + 1, move.d)) {
						if (!inPieces(n + 1)) {
							hPieces.add(n + 1);
							hPieces.remove(n);
						}
					} else {
						hPieces.remove(n);
					}
				} else {
					System.err.println("H Direction not found");
					System.exit(1);
				}
			} else if (i == 1 && vPieces.contains(n)) {
				if (move.d.equals(Move.Direction.UP)) {
					if (!offBoard(n - d, move.d)) {
						if (!inPieces(n - d)) {
							vPieces.add(n - d);
						}
						vPieces.remove(n);
					} else {
						vPieces.remove(n);
					}
				} else if (move.d.equals(Move.Direction.LEFT)) {
					if (!offBoard(n - 1, move.d) && !inPieces(n - 1)) {
						vPieces.add(n - 1);
						vPieces.remove(n);
					}
				} else if (move.d.equals(Move.Direction.RIGHT)) {
					if (!offBoard(n + 1, move.d) && !inPieces(n + 1)) {
						vPieces.add(n + 1);
						vPieces.remove(n);
					}
				} else {
					System.err.println("V Direction not found");
					System.exit(1);
				}
			} else {
				System.out.println("PLAYER " + i + " ATTEMPTING TO MOVE " + move.toString() + " = " + (d * (d - move.j - 1) + move.i));
				System.err.println("Piece does not exist");
				System.exit(1);
			}
//			System.out.println("After" + hPieces.toString());
//			System.out.println("After" + vPieces.toString());
		}
	}

	/**
	 * Rollback of update()
	 * @param move : The move that was performed on the board
	 * @param i    : The player who made the move
	 */
	public void downdate(Move move, Integer i) {
		if (move != null) {
			Integer n = d * (d - move.j - 1) + move.i;
			if (move.d.equals(Move.Direction.UP)) {
				if (i == 0) {
					hPieces.add(n);
					if (hPieces.contains(n - d)) {
						hPieces.remove(Integer.valueOf(n - d));
					}
				} else if (i == 1) {
					vPieces.add(n);
					if (vPieces.contains(n - d)) {
						vPieces.remove(Integer.valueOf(n - d));
					}
				}
			} else if (move.d.equals(Move.Direction.DOWN)) {
				hPieces.add(n);
				if (hPieces.contains(n + d)) {
					hPieces.remove(Integer.valueOf(n + d));
				}
			} else if (move.d.equals(Move.Direction.LEFT)) {
				vPieces.add(n);
				if (vPieces.contains(n - 1)) {
					vPieces.remove(Integer.valueOf(n - 1));
				}
			} else if (move.d.equals(Move.Direction.RIGHT)) {
				if (i == 0) {
					hPieces.add(n);
					if (hPieces.contains(n + 1)) {
						hPieces.remove(Integer.valueOf(n + 1));
					}
				} else if (i == 1) {
					vPieces.add(n);
					if (vPieces.contains(n + 1)) {
						vPieces.remove(Integer.valueOf(n + 1));
					}
				}
			} else {
				System.err.println("Direction not found");
				System.exit(1);
			}
		}
	}

	/**
     * Prints the board to std out.
     */
	public void printBoard() {
		for (int i = 0; i < n; i ++) {
			System.out.print(bPieces.contains(i) ?
					"B" : hPieces.contains(i) ?
					"H" : vPieces.contains(i) ?
					"V" : "+");
			System.out.print(((i + 1) % d == 0) ?
					"\n" : " ");
		}
	}

	/**
     * Counts the available moves for a particular player.
     * @param p  : A player - either 'H' or 'V'.
     * @return a : Count of the available moves for said player.
     */
	public int countMoves(char p) {
		int c = 0;
		switch (p) {
		case 'H':
			for (Integer i: this.hPieces) {
				c += validMove(i - d, p, Move.Direction.UP) ? 1 : 0;
				c += validMove(i + d, p, Move.Direction.DOWN) ? 1 : 0;
				c += validMove(i + 1, p, Move.Direction.RIGHT) ? 1 : 0;
			}
			break;
		case 'V':
			for (Integer i: this.vPieces) {
				c += validMove(i - d, p, Move.Direction.UP) ? 1 : 0;
				c += validMove(i - 1, p, Move.Direction.LEFT) ? 1 : 0;
				c += validMove(i + 1, p, Move.Direction.RIGHT) ? 1 : 0;
			}
			break;
		}
		return c;
	}

	/**
	 * Determines if the given space is off the board or not.
     * @param i : The space to check
	 * @param m : The direction of the move made
	 * @return  : True if the space is off the board, otherwise false.
	 */
	private boolean offBoard(Integer i, Move.Direction m) {
		return (i < 0) ?
				true : (m.equals(Move.Direction.RIGHT) && (i % d) == 0) ?
				true : (m.equals(Move.Direction.UP) && (i < 0)) ?
				true : (m.equals(Move.Direction.LEFT) && (i % d) == (d - 1)) ?
				true : (m.equals(Move.Direction.DOWN) && i >= this.n) ?
				true : false;
	}

	/**
	 * Returns true if a piece is found in the vertical, horizontal, or blocked pieces
	 * @param i : The piece
	 * @return  : true if a piece is found in the vertical, horizontal, or blocked pieces
	 */
	private boolean inPieces(Integer i) {
		return (vPieces.contains(i) || hPieces.contains(i) || bPieces.contains(i));
	}

	/**
     * Determines if the given space is a valid location for the given player.
     * @param i : The space to check
     * @param p : The player
     * @return  : True if valid move, false if otherwise.
     */
	private boolean validMove(Integer i, Character p, Move.Direction m) {
		if (p == 'H' && m.equals(Move.Direction.RIGHT) && i > 0 && (i % d) == 0) {
			return !inPieces(i);
		} else if (p == 'H' && m.equals(Move.Direction.UP) && i >= 0) {
			return !inPieces(i);
		} else if (p == 'V' && i < 0 && m.equals(Move.Direction.UP)) {
			return true;
		} else if (p == 'V' && m.equals(Move.Direction.LEFT) && (i % d) == (d - 1)) {
			return false;
		}
		if (offBoard(i, m)) {
			return false;
		}
		return !inPieces(i);
	}

	/**
	 * Returns a list of pieces with valid moves
	 * @param p : The player
	 * @return  : ArrayList of pieces with valid moves 
	 */
	public ArrayList<Integer> getValidPieces(Character p) {
    	ArrayList<Integer> validPieces = new ArrayList<>();
    	for (Integer i: (p.equals('H')) ? this.hPieces : this.vPieces) {
    		if (getValidMoves(i, p).size() > 0) {
    			validPieces.add(i);
    		}
    	}
    	return validPieces;
    }

	/**
	 * Returns a list of valid moves
	 * @param i : The space to check
	 * @param p : The player
	 * @return  : ArrayList of valid moves for a piece
	 */
	public ArrayList<Move> getValidMoves(Integer i, Character p) {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		Integer x = Math.floorMod(i, d);
		Integer y = d - Math.floorDiv(i, d) - 1;
		if (p.equals('H')) {
			if (validMove(i + 1, p, Move.Direction.RIGHT)) {
				validMoves.add(new Move(x, y, Move.Direction.RIGHT));
			}
			if (validMove(i - d, p, Move.Direction.UP)) {
				validMoves.add(new Move(x, y, Move.Direction.UP));
			}
			if (validMove(i + d, p, Move.Direction.DOWN)) {
				validMoves.add(new Move(x, y, Move.Direction.DOWN));
			}
		} else {
			if (validMove(i - d, p, Move.Direction.UP)) {
				validMoves.add(new Move(x, y, Move.Direction.UP));
			}
			if (validMove(i - 1, p, Move.Direction.LEFT)) {
				validMoves.add(new Move(x, y, Move.Direction.LEFT));
			}
			if (validMove(i + 1, p, Move.Direction.RIGHT)) {
				validMoves.add(new Move(x, y, Move.Direction.RIGHT));
			}
		}
		return validMoves;
	}

	/**
	 * Returns the dimension of the board
	 * @return : Board dimension
	 */
	public int getDimension() {
		return this.d;
	}

	@Override
	public String toString() {
		String s = new String();
		for (int i = 0; i < n; i ++) {
			s += (bPieces.contains(i) ?
					"B" : hPieces.contains(i) ?
					"H" : vPieces.contains(i) ?
					"V" : "+");
			s += (((i + 1) % d == 0) ?
					"\n" : " ");
		}
		return s;
	}

	/**
	 * Convert a piece value to its x-coordinate
	 * @param i : Piece value
	 * @return  : x-coordinate of the piece
	 */
	public int getX(Integer i) {
		return Math.floorMod(i, d);
	}

	/**
	 * Convert a piece value to its y-coordinate
	 * @param i : Piece value
	 * @return  : y-coordinate of the piece
	 */
	public int getY(Integer i) {
		return d - Math.floorDiv(i, d) - 1;
	}

	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == SliderTransition.class) {
            return (this.equalBoard(((SliderTransition) o).getBoard()));
        } else {
            return false;
        }
	}

	/**
	 * Compares another board to this
	 * @param o : The board to compare with
	 * @return  : True if the board is the same with this
	 */
	private boolean equalBoard(CompactBoard2 o) {
		if (this.vPieces != o.vPieces || this.hPieces != o.hPieces || this.bPieces != o.bPieces || this.getDimension() != o.getDimension()) {
			return false;
		}
		return true;
	}

	@Override
	public SliderTransition simulationTransition(Set<SliderTransition> possibleTransitions) {
		List<SliderTransition> transitions = new ArrayList<SliderTransition>(possibleTransitions);
		return transitions.get((int) Math.floor(Math.random() * possibleTransitions.size()));
	}

	@Override
	public SliderTransition expansionTransition(Set<SliderTransition> possibleTransitions) {
		List<SliderTransition> transitions = new ArrayList<SliderTransition>(possibleTransitions);
		return transitions.get((int) Math.floor(Math.random() * possibleTransitions.size()));
	}

	@Override
	protected void makeTransition(SliderTransition transition) {
//		System.out.println("Player " + transition.getPlayer() + " make transition " + transition.getMove().toString());
//		printBoard();
//		System.out.println();
		if (transition != null && transition.getMove() != null) {
			update(transition.getMove(), transition.getPlayer());
			next();
		}
//		printBoard();
//		System.out.println();
	}

	@Override
	protected void unmakeTransition(SliderTransition transition) {
//		System.out.println("Player " + transition.getPlayer() + " unmake transition " + transition.getMove().toString());
//		printBoard();
//		System.out.println();
		if (transition != null && transition.getMove() != null) {
			downdate(transition.getMove(), transition.getPlayer());
			previous();
		}
//		printBoard();
//		System.out.println();
	}

	@Override
	public Set<SliderTransition> getPossibleTransitions() {
		Set<SliderTransition> transitions = new HashSet<SliderTransition>();
		for (Integer i: getValidPieces(p)) {
			for (Move move: getValidMoves(i, p)) {
				SliderTransition transition = new SliderTransition(d, p, move, this.toString());
				transitions.add(transition);
			}
		}
//		System.out.println("PossibleTransitions: " + t.toString());
		return transitions;
	}

	@Override
	public DefaultNode<SliderTransition> newNode(DefaultNode<SliderTransition> parent, Boolean terminal) {
		return new DefaultNode<SliderTransition>(parent, terminal);
	}

	@Override
	public Boolean isOver() {
		if (getWinner() == -1) {
			return false;
		}
		return true;
	}

	@Override
	public int getCurrentPlayer() {
		return this.p.equals('H') ? 0 : 1;
	}

	@Override
	public void next() {
		this.p = this.p.equals('H') ? 'V' : 'H';
	}

	@Override
	public void previous() {
		this.p = this.p.equals('H') ? 'V' : 'H';
	}

	@Override
	public int getWinner() {
		if (this.hPieces.isEmpty()) {
			return 0;
		} else if (this.vPieces.isEmpty()) {
			return 1;
		}
		if (countMoves('H') == 0 && countMoves('V') == 0) {
			return 2;
		} else {
			return -1;
		}
	}

	/**
	 * Clones the board and returns a clone instance
	 * @return : A clone instance
	 */
	public CompactBoard2 cloneBoard() {
		CompactBoard2 clone = new CompactBoard2();
		clone.initBoard(getDimension(), this.toString(), this.p);
        return clone;
	}

	/**
	 * Returns true if a piece is moving off the board
	 * @param move : The move made on the piece
	 * @return     : Whether it is moving off the board
	 */
	public Boolean isMovingOffBoard(Move move) {
		Integer i = d * (d - move.j - 1) + move.i;
		return (i < 0) ?
				true : (move.equals(Move.Direction.RIGHT) && (i % d) == 0) ?
				true : (move.equals(Move.Direction.LEFT) && (i % d) == (d - 1)) ?
				true : (move.equals(Move.Direction.DOWN) && i >= this.n) ?
				true : false;
	}

	/**
	 * Returns true if a cell is empty
	 * @param i : x-coordinate
	 * @param j : y-coordinate
	 * @return  : true if a cell is empty
	 */
	public boolean isEmpty(int i, int j) {
		return !inPieces(d * (d - j - 1) + i);
	}

}
