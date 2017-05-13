package barrellchee.slider;

import aiproj.slider.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by barrelld on 1/05/2017.
 */
public class ArrayListSliderBoard extends SliderBoard {
    private List<Integer> vertPieces;
    private List<Integer> horPieces;
    private List<Integer> blockPieces;
    private int dimension;
    private int spaces;

    public void initBoard(int dimension) {
        this.dimension = dimension;
        spaces = (int) Math.pow(dimension+2, 2);
        vertPieces = new ArrayList<>();
        horPieces = new ArrayList<>();
        blockPieces = new ArrayList<>();
        for (int i = 1; i < dimension; i++) {
            horPieces.add(coordToSpace(0,i));
            vertPieces.add(coordToSpace(i,0));
        }

        System.out.println("Printing board info:");
        System.out.println("Vert: " + vertPieces);
        System.out.println("Hori: " + horPieces);

    }

    /**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board The string representation of the board.
     */
    @Override
    public void initBoard(int dimension, String board) {
        if (board == null) {
            initBoard(dimension);
            return;
        }
        this.dimension = dimension;

        Scanner scanner = new Scanner(board);

        // The board will have a 1-cell border around it, hence (dimension + 2)^2
        spaces = (int) Math.pow(dimension+2, 2);

        vertPieces = new ArrayList<>();
        horPieces = new ArrayList<>();
        blockPieces = new ArrayList<>();
        int i = dimension + 3;         // Starts at dimension + 3 to skip the top border row

        while (scanner.hasNext()) {
            String in = scanner.next();
            switch (in) {
                case "V":
                    vertPieces.add(i);
                    break;
                case "H":
                    horPieces.add(i);
                    break;
                case "B":
                    blockPieces.add(i);
                    break;
            }
            i++;
            if ((i+1)%(dimension+2) == 0) {  // Jump the border cells when at end of line
                i += 2;
            }
        }
    }

    /**
     * Updates a board with a particular move.
     *
     * @param move The move that will be performed on the board.
     */
    @Override
    public Character update(Move move) {
        if (move == null || move.i == -1) {
            return null;
        }
        int space = coordToSpace(move.i, move.j);
        int newSpace = -1;
        switch (move.d) {
            case UP:
                newSpace = space - (dimension + 2);
                break;
            case DOWN:
                newSpace = space + (dimension + 2);
                break;
            case LEFT:
                newSpace = space - 1;
                break;
            case RIGHT:
                newSpace = space + 1;
                break;
        }
        if (vertPieces.contains(space)) {
            vertPieces.remove(Integer.valueOf(space));
            if (!isOffBoard(newSpace)) {
                vertPieces.add(newSpace);
            }
            return 'V';
        } else if (horPieces.contains(space)) {
            horPieces.remove(Integer.valueOf(space));
            if (!isOffBoard(newSpace)) {
                horPieces.add(newSpace);
            }
            return 'H';
        }
        return null;
    }

    @Override
    public Boolean isMovingOffBoard(Move move) {
        if (move == null || move.i == -1) {
            return null;
        }
        if (move.d.equals(Move.Direction.DOWN) || move.d.equals(Move.Direction.LEFT))
            return false;

        int space = coordToSpace(move.i, move.j);
        switch (move.d) {
            case UP:
                space = space - (dimension + 2);
                break;
            case RIGHT:
                space = space + 1;
                break;
        }
        return isOffBoard(space);
    }

    /**
     * Prints the board to std out.
     */
    @Override
    public void printBoard() {
        for (int i = 0; i < spaces; i++) {
            if (i < dimension + 2 || i >= spaces - (dimension + 2)) {
                System.out.print("-");
            } else if ((i+1)%(dimension+2) == 0 || (i)%(dimension+2) == 0) {
                System.out.print("|");
            } else if (vertPieces.contains(i)) {
                System.out.print("V");
            } else if (horPieces.contains(i)) {
                System.out.print("H");
            } else if (blockPieces.contains(i)) {
                System.out.print("B");
            } else {
                System.out.print("+");
            }
            if ((i+1)%(dimension+2) == 0) {
                System.out.println();
            }
            else {
                System.out.print(" ");
            }
        }
    }

    /**
     * Determines if the given space is off the board or not.
     * @param i The space to check
     * @return True if the space is off the board, otherwise false.
     */
    private boolean isOffBoard(int i) {
        return (i < dimension + 2 || i >= spaces - (dimension + 2)
                || (i+1)%(dimension+2) == 0 || (i)%(dimension+2) == 0);
    }

    /**
     * Counts the available moves for a particular player.
     * @param p A player - either 'H' or 'V'.
     * @return a count of the available moves for said player.
     */
    @Override
    public int countMoves(char p) {
        int count = 0;

        List<Integer> pieces;
        if (p == 'V') {
            pieces = vertPieces;
        } else if (p == 'H') {
            pieces = horPieces;
        } else {
            return -1;
        }

        // For each of the player's pieces, check whether a move up, down, left,
        // or right is possible. Increase count for each possible move.
        for (Integer i : pieces) {
            if (isMove(i + 1, p)) {
                count++;
            }
            if (p == 'V' && isMove(i - 1, p)) {
                count++;
            }
            if (p == 'H' && isMove(i + dimension + 2, p)) {
                count++;
            }
            if (isMove(i - (dimension + 2), p)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Returns a list of possible moves for a particular player.
     *
     * @param p A player - either 'H' or 'V'.
     * @return a list of possible moves for said player.
     */
    @Override
    public List<Move> getMoves(char p) {
        List<Move> moves = new ArrayList<>();

        List<Integer> pieces;
        if (p == 'V') {
            pieces = vertPieces;
        } else if (p == 'H') {
            pieces = horPieces;
        } else {
            return null;
        }

        // For each of the player's pieces, check whether a move up, down, left,
        // or right is possible. Increase count for each possible move.
        for (Integer i : pieces) {
            Coords c = spaceToCoord(i);
            if (isMove(i + 1, p)) {
                moves.add(new Move(c.i, c.j, Move.Direction.RIGHT));
            }
            if (isMove(i - (dimension + 2), p)) {
                moves.add(new Move(c.i, c.j, Move.Direction.UP));
            }
            if (p == 'V' && isMove(i - 1, p)) {
                moves.add(new Move(c.i, c.j, Move.Direction.LEFT));
            }
            if (p == 'H' && isMove(i + dimension + 2, p)) {
                moves.add(new Move(c.i, c.j, Move.Direction.DOWN));
            }
        }

        if (moves.isEmpty()) {
            moves.add(new Move(-1,-1, Move.Direction.DOWN));
        }

        return moves;
    }

    /**
     * Determines if the given space is a valid location for the given player.
     *
     * @param i The space to check
     * @param p The player
     * @return True if valid move, false if otherwise.
     */
    private boolean isMove(int i, char p) {
        if (p == 'V' && i <= dimension && i >= 1) {
            // If the piece is vertical, and it's going off the top of board, move is valid
            return true;
        } else if (p == 'H' && (i+1)%(dimension+2) == 0) {
            // If the piece is horizontal, and it's going off the right of board, move is valid
            return true;
        } else if (isOffBoard(i)) {
            // If neither of above, and pieces is going off board, move is invalid
            return false;
        }

        // Otherwise, return true if cell is empty
        return !(vertPieces.contains(i) || horPieces.contains(i) || blockPieces.contains(i));
    }

    /**
     * Calculates the winner, if there is one.
     *
     * @return the char value of winner, or null of none.
     */
    @Override
    public Character getWinner() {
        if (vertPieces.isEmpty()) {
            return 'V';
        } else if (horPieces.isEmpty()) {
            return 'H';
        } else {
            return null;
        }
    }

    @Override
    public int movesMadeTowardsEnd(Character player) {
        int total = 0;
        List<Integer> pieces = (player == 'V') ? vertPieces : horPieces;

        for (Integer p : pieces) {
            Coords c = spaceToCoord(p);
            int d = (player == 'V') ? c.j : c.i;
            total += dimension - d;
        }
        return (int)Math.pow(dimension-1,2) - total;
    }

    @Override
    public double fracPiecesBlockingOpp(Character player) {
        int total = 0;
        List<Integer> pieces = (player == 'V') ? vertPieces : horPieces;
        List<Integer> oppPieces = (player == 'V') ? horPieces : vertPieces;
        int diff = (player == 'V') ? -1 : dimension + 2;

        for (Integer p : pieces) {
            if (oppPieces.contains(p + diff))
                total++;
        }

        return total/(dimension - 1);
    }

    @Override
    public double fracRemovedPieces(Character player) {
        List<Integer> pieces = (player == 'V') ? vertPieces : horPieces;
        return (dimension - 1 - pieces.size())/(dimension - 1);
    }

    @Override
    public double fracUnblockedPieces(Character player) {
        int total = 0;
        List<Integer> pieces = (player == 'V') ? vertPieces : horPieces;
        List<Integer> oppPieces = (player == 'V') ? horPieces : vertPieces;
        int diff = (player == 'V') ? -(dimension + 2) : 1;

        for (Integer p : pieces) {
            if (oppPieces.contains(p + diff))
                total++;
            else if (blockPieces.contains(p + diff))
                total++;
        }

        return (dimension - 1 - total)/(dimension - 1);
    }

    @Override
    public boolean isEmpty(int i, int j) {
        int space = coordToSpace(i,j);
        boolean retVal  = !(vertPieces.contains(space) || horPieces.contains(space) || blockPieces.contains(space));
        return retVal;
    }

    /**
     * Clones the board.
     *
     * @return a cloned board.
     */
    @Override
    public SliderBoard cloneBoard() {
        return (ArrayListSliderBoard) this.clone();
    }

    @Override
    public Object clone() {
        ArrayListSliderBoard newBoard = new ArrayListSliderBoard();

        newBoard.setBlockPieces(new ArrayList<>(blockPieces));
        newBoard.setHorPieces(new ArrayList<>(horPieces));
        newBoard.setVertPieces(new ArrayList<>(vertPieces));
        newBoard.setDimension(dimension);
        newBoard.setSpaces(spaces);

        return newBoard;
    }

    private int coordToSpace(int i, int j) {
        return (dimension - j)*(dimension + 2) + (i + 1);
    }

    private Coords spaceToCoord(int space) {
        int i = (space % (dimension + 2)) - 1;
        int j = (int)(dimension - Math.floor(space / (float) (dimension + 2)));
        return new Coords(i,j);
    }

    public void setVertPieces(List<Integer> vertPieces) {
        this.vertPieces = vertPieces;
    }

    public void setHorPieces(List<Integer> horPieces) {
        this.horPieces = horPieces;
    }

    public void setBlockPieces(List<Integer> blockPieces) {
        this.blockPieces = blockPieces;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }


    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < spaces; i++) {
            if (i < dimension + 2 || i >= spaces - (dimension + 2)) {
                buf.append("-");
            } else if ((i+1)%(dimension+2) == 0 || (i)%(dimension+2) == 0) {
                buf.append("|");
            } else if (vertPieces.contains(i)) {
                buf.append("V");
            } else if (horPieces.contains(i)) {
                buf.append("H");
            } else if (blockPieces.contains(i)) {
                buf.append("B");
            } else {
                buf.append("+");
            }
            if ((i+1)%(dimension+2) == 0) {
                buf.append("\n");
            }
            else {
                buf.append(" ");
            }
        }
        return buf.toString();
    }

    class Coords {
        int i;
        int j;

        public boolean equals(Object o) {
            Coords c = (Coords) o;
            return c.i == i && c.j == j;
        }

        public Coords(int i, int j) {
            super();
            this.i = i;
            this.j = j;
        }

        public int hashCode() {
            return new Integer(i + "0" + j);
        }
    }

    @Override
    public boolean equals(Object anObj) {
        if(anObj != null && anObj.getClass() == this.getClass()) {
            ArrayListSliderBoard board = (ArrayListSliderBoard) anObj;

            if (this.vertPieces != board.vertPieces
                || this.horPieces != board.horPieces
                || this.blockPieces != board.blockPieces
                || this.dimension != board.dimension) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }
}
