package barrellchee.slider;

import aima.core.search.adversarial.*;
import aiproj.slider.Move;
import barrellchee.slider.ai.SliderAlphaBetaSearch;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

import java.util.Iterator;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class Test {

    public static void main(String[] args) {
        startAlphaBetaDemo();
//        test1();
    }

    private static void test1() {
        SliderGame game = new SliderGame(4,null, ArrayListSliderBoard.class);
        SliderState state = game.getInitialState();

        System.out.println(state.getBoard().isEmpty(1,1));

        Object action;
        int i = 1;
        for(Iterator var5 = game.getActions(state).iterator(); var5.hasNext(); i++) {
            action = var5.next();
            System.out.println(action);
        }
    }

    private static void startAlphaBetaDemo() {
        System.out.println("ALPHA BETA DEMO\n");
        SliderGame game = new SliderGame(4,null, ArrayListSliderBoard.class);
        SliderState currState = game.getInitialState();
        SliderAlphaBetaSearch search = new SliderAlphaBetaSearch(game,0D,1D,5, 8);
        System.out.println("Print board");
        currState.getBoard().printBoard();
        System.out.println();
        while (!(game.isTerminal(currState))) {
            System.out.println(game.getPlayer(currState) + "  playing ... ");
            Move action = search.makeDecision(currState);
            System.out.println(action);
            currState = game.getResult(currState, action);
            System.out.println(currState);
        }
        System.out.println("ALPHA BETA DEMO done");
    }
}
