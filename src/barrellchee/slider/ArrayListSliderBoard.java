package barrellchee.slider;

import aiproj.slider.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by barrelld on 1/05/2017.
 */
public class ArrayListSliderBoard implements SliderBoard {
    private List<Integer> vertPieces;
    private List<Integer> horPieces;
    private List<Integer> blockPieces;
    private int dimension;
    private int spaces;

    /**
     * Initiates the internal board.
     *
     * @param dimension The dimension of the board.
     * @param board The string representation of the board.
     */
    @Override
    public void initBoard(int dimension, String board) {
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
     * @throws Exception
     */
    @Override
    public void update(Move move) throws Exception {
        int space = (dimension - move.j)*(dimension + 2) + (move.i + 1);
//        System.out.println("Old space: " + space);
        int newSpace;
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
            default:
                throw new Exception("Direction not found");
        }
        if (vertPieces.contains(space)) {
            vertPieces.remove(Integer.valueOf(space));
            if (!isOffBoard(newSpace)) {
                vertPieces.add(newSpace);
            }
        } else if (horPieces.contains(space)) {
            horPieces.remove(Integer.valueOf(space));
            if (!isOffBoard(newSpace)) {
                horPieces.add(newSpace);
            }
        } else {
            throw new Exception("Piece doesn't exist in arrays");
        }
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
}
