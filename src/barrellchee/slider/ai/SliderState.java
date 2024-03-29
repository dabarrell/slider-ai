package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Stores the state of the Slider game, along with functions required for Alpha Beta search.
 *
 * This has been adapted from aima-java.
 *
 * COMP30024 Artificial Intelligence
 * Project Part B - Sem 1 2017
 * @author David Barrell (520704), Ivan Chee (736901)
 */
public class SliderState implements Cloneable {
    private final static int MOVES_TO_STORE = 6;

    private char playerToMove = 'V';
    private Double utility = null;
    private int moves = 0;

    private SliderBoard board;
    private HistoryList moveHistory = new HistoryList(MOVES_TO_STORE);

    /**
     * Initialises instance of SliderState.
     *
     * @param dimension Dimension of game
     * @param board Current board
     * @param boardClass Internal class to use to hold board
     */
    public <T extends SliderBoard> SliderState(int dimension, String board, Class<T> boardClass) {
        this.board = new ArrayListSliderBoard();
        try {
            this.board = boardClass.newInstance();
            this.board.initBoard(dimension, board);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * @return Next player to move
     */
    public char getPlayerToMove() {
        return this.playerToMove;
    }

    /**
     * Sets player to move.
     *
     * @param playerToMove Player to move
     */
    public void setPlayerToMove(char playerToMove) {
        this.playerToMove = playerToMove;
    }

    /**
     * Determines if a space is empty.
     *
     * @param col Column of space
     * @param row Row of space
     * @return True if empty, false otherwise
     */
    public boolean isEmpty(int col, int row) {
        return board.isEmpty(col,row);
    }

    /**
     * @return Utility of state
     */
    public Double getUtility() {
        return this.utility;
    }

    /**
     * Makes a move on the current state.
     *
     * @param move Move to make
     */
    public void makeMove(Move move) {
        if(this.utility == null && (move == null || !board.isEmpty(move.i, move.j))) {
            Character player = board.update(move);
            if (player == null && move != null) {
                System.err.println("Error - piece doesn't exist");
                System.err.println(move);
                board.printBoard();
                System.exit(1);
            }
            moveHistory.add(new PlayerMoveTuple(playerToMove,move));
            moves++;
            this.analyseUtility();
            nextPlayer();
        } else {
            System.err.println(this.utility + " " + (move == null) + " " + move);
            board.printBoard();
            System.err.println("Error - move not valid");
            System.exit(1);
        }
    }

    /**
     * Updates playerToMove to next player.
     */
    public void nextPlayer() {
        this.playerToMove = Objects.equals(this.playerToMove, 'H')?'V':'H';
    }

    /**
     * @return The number of move which have been made
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Analyses utility after a move.
     */
    private void analyseUtility() {
        Character winner = board.getWinner();
        if(winner != null) {
            this.utility = Objects.equals(winner, 'H')? 1D : -1D;
        } else if(board.countMoves() == 0) {
            this.utility = 0D;
        }
    }

    /**
     * Analyses the value of a move for sorting.
     *
     * @param move Move to analyse
     * @param player Player making move
     * @return Value of move
     */
    public double analyseMoveValue(Move move, char player) {
        if (move == null || move.i == -1) {
            return 0.0;
        }
        if (board.isMovingOffBoard(move)) {
            return 2.0;
        }
        if (player == 'V' && move.d.equals(Move.Direction.UP)) {
            return 1.0;
        }
        if (player == 'H' && move.d.equals(Move.Direction.RIGHT)) {
            return 1.0;
        }
        if (player == 'V' && move.d.equals(Move.Direction.LEFT)) {
            return 0.5;
        }
        if (player == 'H' && move.d.equals(Move.Direction.DOWN)) {
            return 0.5;
        }
        return 0.0;
    }

    /**
     * @return True if game is finished, false otherwise
     */
    public boolean isFinished() {
        return board.isFinished();
    }

    /**
     * @return A clone of this state
     */
    public SliderState clone() {
        SliderState copy = null;

        try {
            copy = (SliderState)super.clone();
            copy.board = board.cloneBoard();
        } catch (CloneNotSupportedException var3) {
            var3.printStackTrace();
        }

        return copy;
    }

    public boolean equals(Object anObj) {
        if(anObj != null && anObj.getClass() == this.getClass()) {
            SliderState anotherState = (SliderState)anObj;

            return (this.board.equals(anotherState.getBoard()));
        } else {
            return false;
        }
    }

    /**
     * @return The board attached to this state
     */
    public SliderBoard getBoard() {
        return board;
    }

    /**
     * @return The stored history of moves
     */
    public ArrayList<PlayerMoveTuple> getMoveHistory() {
        return moveHistory.getList();
    }

    /**
     * @param player Player to check
     * @return The stored history of moves for a player
     */
    public List<Move> getHistory(char player) {
        return moveHistory.getList().stream()
                .filter(move -> move.player == player)
                .map(m -> m.move)
                .collect(Collectors.toList());
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        return board.toString();
    }

    /**
     * Stores a move and a player, for the moveHistory list
     */
    private class PlayerMoveTuple {
        private final char player;
        private final Move move;
        private PlayerMoveTuple(char player, Move move) {
            this.player = player;
            this.move = move;
        }
    }

    /**
     * Implements a list with a maximum length, used to store a history of moves
     */
    private class HistoryList {
        private ArrayList<PlayerMoveTuple> list;
        private int maxLen;

        public HistoryList(int maxLen) {
            this.list = new ArrayList<>();
            this.maxLen = maxLen;
        }

        private void add(PlayerMoveTuple tuple) {
            if (list.size()<maxLen) {
                list.add(0,tuple);
            } else {
                list.add(0,tuple);
                list.remove(maxLen);
            }
        }

        private ArrayList<PlayerMoveTuple> getList() {
            return list;
        }
    }
}

