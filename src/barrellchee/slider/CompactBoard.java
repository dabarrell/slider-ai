package barrellchee.slider;

import java.util.ArrayList;
import java.util.Scanner;

import aiproj.slider.Move;

/**
 * Compact representation of ArrayListBoard
 * 
 * @author barrellchee
 */
public class CompactBoard implements Board {

	private Integer d;
	private Integer n;
	private ArrayList<Integer> bPieces;
	private ArrayList<Integer> hPieces;
	private ArrayList<Integer> vPieces;

	/**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board The string representation of the board.
     */
	@Override
	public void initBoard(int dimension, String board) {
		this.d = dimension;
		this.n = (int) Math.pow(this.d, 2);
		
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
     *
     * @param move The move that will be performed on the board.
     * @throws Exception
     */
	@Override
	public void update(Move move) throws Exception {
		Integer n = d * (d - move.j - 1) + move.i;
		if (hPieces.contains(n)) {
			hPieces.remove(n);
			switch (move.d) {
			case UP:
				if (!offBoard(n - d, move.d) && !inPieces(n - d)) {
					hPieces.add(n - d);
				}
				break;
			case DOWN:
				if (!offBoard(n + d, move.d) && !inPieces(n + d)) {
					hPieces.add(n + d);
				}
				break;
			case RIGHT:
				if (!offBoard(n + 1, move.d) && !inPieces(n + 1)) {
					hPieces.add(n + 1);
				}
				break;
			default:
				throw new Exception("Direction not found");
			}
		} else if (vPieces.contains(n)) {
			vPieces.remove(n);
			switch (move.d) {
			case UP:
				if (!offBoard(n - d, move.d) && !inPieces(n - d)) {
					vPieces.add(n - d);
				}
				break;
			case LEFT:
				if (!offBoard(n - 1, move.d) && !inPieces(n - 1)) {
					vPieces.add(n - 1);
				}
				break;
			case RIGHT:
				if (!offBoard(n + 1, move.d) && !inPieces(n + 1)) {
					vPieces.add(n + 1);
				}
				break;
			default:
				throw new Exception("Direction not found");
			}
		} else {
			throw new Exception("Piece does not exist");
		}
	}

	/**
     * Prints the board to std out.
     */
	@Override
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
     * @param p A player - either 'H' or 'V'.
     * @return a count of the available moves for said player.
     */
	@Override
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
     * @param i The space to check
     * @return True if the space is off the board, otherwise false.
     */
	private boolean offBoard(Integer i, Move.Direction m) {
		return (i < 0) ?
				true : (m.equals(Move.Direction.RIGHT) && (i % d) == 0) ?
				true : (m.equals(Move.Direction.LEFT) && (i % d) == (d - 1)) ?
				true : (m.equals(Move.Direction.DOWN) && i >= this.n) ?
				true : (m.equals(Move.Direction.UP) && i < 0) ?
				true : false;
	}

	private boolean inPieces(Integer i) {
		return (vPieces.contains(i) || hPieces.contains(i) || bPieces.contains(i));
	}

	/**
     * Determines if the given space is a valid location for the given player.
     *
     * @param i The space to check
     * @param p The player
     * @return True if valid move, false if otherwise.
     */
	private boolean validMove(Integer i, Character p, Move.Direction m) {
		if (p == 'H' && i > 0 && (i % d) == 0 && i < this.n) {
			return !inPieces(i);
		} else if (p == 'V' && i < 0 && m != Move.Direction.LEFT) {
			return !inPieces(i);
		} else if (p == 'V' && (i % d) == (d - 1)) {
			return false;
		}
		if (offBoard(i, m)) {
			return false;
		}
		return !inPieces(i);
	}

	/**
	 * Returns a list of pieces with valid moves
	 * @param p The player
	 * @return ArrayList of pieces with valid moves 
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
	 * @param i The space to check
	 * @param p The player
	 * @return ArrayList of valid moves for a piece
	 */
	public ArrayList<Move> getValidMoves(Integer i, Character p) {
		ArrayList<Move> validMoves = new ArrayList<Move>();
		Integer x = Math.floorMod(i, d);
		Integer y = d - Math.floorDiv(i, d) - 1;
		if (validMove(i - d, p, Move.Direction.UP)) {
			validMoves.add(new Move(x, y, Move.Direction.UP));
		}
		if (validMove(i + 1, p, Move.Direction.RIGHT)) {
			validMoves.add(new Move(x, y, Move.Direction.RIGHT));
		}
		if (p.equals('H')) {
			if (validMove(i + d, p, Move.Direction.DOWN)) {
				validMoves.add(new Move(x, y, Move.Direction.DOWN));
			}
		} else {
			if (validMove(i - 1, p, Move.Direction.LEFT)) {
				validMoves.add(new Move(x, y, Move.Direction.LEFT));
			}
		}
		return validMoves;
	}

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

	public int getX(Integer i) {
		return Math.floorMod(i, d);
	}

	public int getY(Integer i) {
		return d - Math.floorDiv(i, d) - 1;
	}

}
