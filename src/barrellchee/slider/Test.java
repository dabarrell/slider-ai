package barrellchee.slider;

import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aiproj.slider.Move;
import barrellchee.slider.ai.SliderGame;
import barrellchee.slider.ai.SliderState;

/**
 * ai-partB
 * Created by David Barrell on 2/5/17.
 */
public class Test {

    public static void main(String[] args) {
        startAlphaBetaDemo();
    }

    private static void startAlphaBetaDemo() {
        System.out.println("ALPHA BETA DEMO\n");
        SliderGame game = new SliderGame();
        SliderState currState = game.getInitialState();
        AdversarialSearch<SliderState, Move> search = AlphaBetaSearch
                .createFor(game);
        while (!(game.isTerminal(currState))) {
            System.out.println(game.getPlayer(currState) + "  playing ... ");
            Move action = search.makeDecision(currState);
            currState = game.getResult(currState, action);
            System.out.println(currState);
        }
        System.out.println("ALPHA BETA DEMO done");
    }
}
