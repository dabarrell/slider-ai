package barrellchee.slider.ai;

import aima.core.search.adversarial.AlphaBeta;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.GameState;
import barrellchee.slider.SliderBoard;

import java.util.ArrayList;

/**
 * Created by barrelld on 2/05/2017.
 */
public class Slider extends Game {

    public Slider(SliderBoard board) {
        this.initialState.put("board", board);
        this.initialState.put("hmoves", board.getMoves('H'));
        this.initialState.put("vmoves", board.getMoves('V'));

        this.initialState.put("utility", new Integer(0));
        this.initialState.put("player", "H");
        this.initialState.put("level", new Integer(0));
        this.presentState = this.initialState;
    }

    public SliderBoard getBoard(GameState state) {
        return (SliderBoard)state.get("board");
    }

    @Override
    public ArrayList getSuccessorStates(GameState state) {
        GameState temp = this.presentState;
        ArrayList<Object> retVal = new ArrayList();
        int parentLevel = this.getLevel(state);

        for(int i = 0; i < this.getMoves(state).size(); ++i) {
            XYLocation loc = (XYLocation)this.getMoves(state).get(i);
            GameState aState = this.makeMove(state, loc);
            aState.put("moveMade", loc);
            aState.put("level", new Integer(parentLevel + 1));
            retVal.add(aState);
        }

        this.presentState = temp;
        return retVal;
    }

    @Override
    public boolean hasEnded() {
        return super.hasEnded();
    }

    @Override
    public int getLevel(GameState g) {
        return super.getLevel(g);
    }

    @Override
    public ArrayList getMoves(GameState state) {
        return super.getMoves(state);
    }

    @Override
    public String getPlayerToMove(GameState state) {
        return super.getPlayerToMove(state);
    }

    @Override
    public int getUtility(GameState h) {
        return super.getUtility(h);
    }

    @Override
    public GameState getState() {
        return super.getState();
    }

    @Override
    public int maxValue(GameState state) {
        return super.maxValue(state);
    }

    @Override
    public int minValue(GameState state) {
        return super.minValue(state);
    }

    @Override
    public int minValue(GameState state, AlphaBeta ab) {
        return super.minValue(state, ab);
    }

    @Override
    public void makeMiniMaxMove() {
        super.makeMiniMaxMove();
    }

    @Override
    public void makeAlphaBetaMove() {
        super.makeAlphaBetaMove();
    }

    @Override
    protected int maxValue(GameState state, AlphaBeta ab) {
        return super.maxValue(state, ab);
    }

    @Override
    public GameState makeMove(GameState gameState, Object o) {
        return null;
    }

    @Override
    public int getMiniMaxValue(GameState gameState) {
        return 0;
    }

    @Override
    public int getAlphaBetaValue(GameState gameState) {
        return 0;
    }

    @Override
    protected int computeUtility(GameState gameState) {
        return 0;
    }

    @Override
    protected boolean terminalTest(GameState gameState) {
        return false;
    }
}
