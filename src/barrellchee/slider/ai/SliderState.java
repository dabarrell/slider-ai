package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.SliderBoard;

import java.util.List;
import java.util.Objects;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderState implements Cloneable {
    private char playerToMove = 'H';
    private double utility = -1.0D;

    private SliderBoard board;

    public SliderState() {
        board = new ArrayListSliderBoard();
        board.initBoard(4);
    }

    public SliderState(int dimension, String board) {
        this.board = new ArrayListSliderBoard();
        this.board.initBoard(dimension, board);
    }

    public char getPlayerToMove() {
        return this.playerToMove;
    }

    public boolean isEmpty(int col, int row) {
        return board.isEmpty(col,row);
    }

    public double getUtility() {
        return this.utility;
    }

    public void makeMove(Move move) {
        boolean empty = board.isEmpty(move.i, move.j);
        if(this.utility == -1.0D && !empty) {
            board.update(move);
            this.analyzeUtility();
            this.playerToMove = Objects.equals(this.playerToMove, 'H')?'V':'H';
        } else {
            System.err.println("Test");
        }
    }

    private void analyzeUtility() {
        if(board.getWinner() != null) {
            this.utility = (double)(Objects.equals(this.playerToMove, 'H')?1:0);
        } else if(board.countMoves() == 0) {
            this.utility = 0.5D;
        }

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

