package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class SliderState implements Cloneable {
    private final static int MOVES_TO_STORE = 6;

    private char playerToMove = 'V';
    private Double utility = null;
    private int moves = 0;

    private SliderBoard board;
//    private PlayerMoveTuple[] moveHistory = new PlayerMoveTuple[MOVES_TO_STORE];
    private HistoryList moveHistory = new HistoryList(MOVES_TO_STORE);

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

    public char getPlayerToMove() {
        return this.playerToMove;
    }

    public void setPlayerToMove(char playerToMove) {
        this.playerToMove = playerToMove;
    }

    public boolean isEmpty(int col, int row) {
        return board.isEmpty(col,row);
    }

    public Double getUtility() {
        return this.utility;
    }

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
            this.analyzeUtility();
            nextPlayer();
        } else {
            System.err.println(this.utility + " " + (move == null) + " " + move);
            board.printBoard();
            System.err.println("Error - move not valid");
            System.exit(1);
        }
    }

    public void nextPlayer() {
        this.playerToMove = Objects.equals(this.playerToMove, 'H')?'V':'H';
    }

    public int getMoves() {
        return moves;
    }

    public int getMoves(Character player) {
        return moves;
    }

    private void analyzeUtility() {
        Character winner = board.getWinner();
        if(winner != null) {
            this.utility = Objects.equals(winner, 'H')? 1D : -1D;
        } else if(board.countMoves() == 0) {
            this.utility = 0D;
        }
    }

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

    public ArrayList<PlayerMoveTuple> getMoveHistory() {
        return moveHistory.getList();
    }

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

    private class PlayerMoveTuple {
        private final char player;
        private final Move move;
        private PlayerMoveTuple(char player, Move move) {
            this.player = player;
            this.move = move;
        }
    }

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

