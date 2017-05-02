package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.SliderBoard;

import java.util.Objects;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderState implements Cloneable {
    private char playerToMove = 'V';
    private double utility = -1.0D;
    private int moves = 0;

    private SliderBoard board;

    public SliderState(int dimension, String board) {
        this.board = new ArrayListSliderBoard();
        this.board.initBoard(dimension, board);
    }

    public char getPlayerToMove() {
        return this.playerToMove;
    }

    public void setPlayerToMove(char playerToMove) {
        this.playerToMove = playerToMove;
    }

    public boolean isEmpty(int col, int row) {
        return board.isEmpty(col,row);
    }

    public double getUtility() {
        return this.utility;
    }

    public void makeMove(Move move) {
        if(this.utility == -1.0D && (move == null || !board.isEmpty(move.i, move.j))) {
            board.update(move);
            moves++;
            this.analyzeUtility();
            nextPlayer();
        } else {
            System.err.println("Error");
        }
    }

    public void nextPlayer() {
        this.playerToMove = Objects.equals(this.playerToMove, 'H')?'V':'H';
    }

    public int getMoves() {
        return moves;
    }

    private void analyzeUtility() {
        if(board.getWinner() != null) {
            this.utility = (double)(Objects.equals(this.playerToMove, 'H')?1:0);
        } else if(board.countMoves() == 0) {
            this.utility = 0.5D;
        }
    }

    public double analyseMoveValue(Move move, char player) {
        if (board.isMovingOffBoard(move)) {
            return 2.0;
        }
        if (player == 'V' && move.d.equals(Move.Direction.UP)) {
            return 1.0;
        }
        if (player == 'H' && move.d.equals(Move.Direction.RIGHT)) {
            return 1.0;
        }
        return 0.0;
    }

    public boolean isFinished() {
        return board.isFinished();
    }

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

    public SliderBoard getBoard() {
        return board;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        return board.toString();
    }
}

