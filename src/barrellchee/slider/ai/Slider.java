package barrellchee.slider.ai;

import aima.core.search.adversarial.Game;
import aiproj.slider.Move;
import barrellchee.slider.ArrayListSliderBoard;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrelld on 2/05/2017.
 */
public class Slider implements Game<SliderState, Move, Character> {
    SliderState initialState = new SliderState();

    @Override
    public SliderState getInitialState() {
        return this.initialState;
    }

    @Override
    public Character[] getPlayers() {
        return new Character[]{'H','V'};
    }

    @Override
    public Character getPlayer(SliderState state) {
        return state.getPlayerToMove();
    }

    @Override
    public List<Move> getActions(SliderState sliderState) {
        return null;
    }

    @Override
    public SliderState getResult(SliderState sliderState, Move move) {
        return null;
    }

    @Override
    public boolean isTerminal(SliderState sliderState) {
        return false;
    }

    @Override
    public double getUtility(SliderState sliderState, Character character) {
        return 0;
    }

    //    public Slider(SliderBoard board) {
//
//        // What player?
//        this.initialState.put("board", board);
//        this.initialState.put("moves", board.getMoves('H'));
////        this.initialState.put("vmoves", board.getMoves('V'));
//
//        this.initialState.put("utility", new Integer(0));
//        this.initialState.put("player", "H");
//        this.initialState.put("level", new Integer(0));
//        this.presentState = this.initialState;
//    }
//
//    public SliderBoard getBoard(GameState state) {
//        return (SliderBoard)state.get("board");
//    }
//
//    @Override
//    public ArrayList getSuccessorStates(GameState state) {
//        GameState temp = this.presentState;
//        ArrayList<Object> retVal = new ArrayList();
//        int parentLevel = this.getLevel(state);
//
//        for(int i = 0; i < this.getMoves(state).size(); ++i) {
//            Move move = (Move)this.getMoves(state).get(i);
//            GameState aState = this.makeMove(state, move);
//            aState.put("moveMade", move);
//            aState.put("level", new Integer(parentLevel + 1));
//            retVal.add(aState);
//        }
//
//        this.presentState = temp;
//        return retVal;
//    }
//
//    @Override
//    public GameState makeMove(GameState state, Object o) {
//        Move move = (Move) o;
//        return this.makeMove(state, move);
//    }
//
//    public GameState makeMove(GameState state, Move move) {
//        GameState temp = this.getMove(state, move);
//        if(temp != null) {
//            this.presentState = temp;
//        }
//
//        return this.presentState;
//    }
//
//    public GameState getMove(GameState state, Move move) {
//        GameState retVal = null;
//        ArrayList moves = this.getMoves(state);
//        ArrayList newMoves = (ArrayList)moves.clone();
//        if(moves.contains(move)) {
//            int index = newMoves.indexOf(move);
//            newMoves.remove(index);
//            retVal = new GameState();
//            retVal.put("moves", newMoves);
//            SliderBoard newBoard = this.getBoard(state).cloneBoard();
//            newBoard.update(move);
//            if(this.getPlayerToMove(state) == "H") {
//                retVal.put("player", "V");
//            } else {
//                retVal.put("player", "H");
//            }
//
//            retVal.put("board", newBoard);
//            retVal.put("utility", this.computeUtility(newBoard, this.getPlayerToMove(this.getState())));
//            retVal.put("level", this.getLevel(state) + 1);
//        }
//
//        return retVal;
//    }
//
//    public void printPossibleMoves() {
//        System.out.println("Possible moves");
//        ArrayList moves = this.getMoves(this.presentState);
//
//        for(int i = 0; i < moves.size(); ++i) {
//            Move move = (Move)moves.get(i);
//            GameState newState = this.getMove(this.presentState, move);
////            SliderBoard board = (SliderBoard)newState.get("board");
//            System.out.println(move + "(utility = " + this.computeUtility(newState) + ")");
//            System.out.println("");
//        }
//
//    }
//
//    private int computeUtility(SliderBoard aBoard, String playerToMove) {
//        int retVal = 0;
//        if(aBoard.getWinner() != null) {
//            if(playerToMove.equals("H")) {
//                retVal = -1;
//            } else {
//                retVal = 1;
//            }
//        }
//        return retVal;
//    }
//
//    @Override
//    public int computeUtility(GameState state) {
//        int utility = this.computeUtility((SliderBoard) state.get("board"), this.getPlayerToMove(state));
//        return utility;
//    }
//
//    @Override
//    public int getMiniMaxValue(GameState state) {
//        return this.getPlayerToMove(state).equalsIgnoreCase("H")?this.maxValue(state):this.minValue(state);
//    }
//
//    @Override
//    public int getAlphaBetaValue(GameState state) {
//        AlphaBeta initial;
//        if(this.getPlayerToMove(state).equalsIgnoreCase("H")) {
//            initial = new AlphaBeta(-2147483648, 2147483647);
//            int max = this.maxValue(state, initial);
//            return max;
//        } else {
//            initial = new AlphaBeta(-2147483648, 2147483647);
//            return this.minValue(state, initial);
//        }
//    }
//
//    @Override
//    protected boolean terminalTest(GameState gameState) {
//        SliderBoard board = (SliderBoard)gameState.get("board");
//        return board.isFinished();
//    }
}
