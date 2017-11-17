package barrellchee.slider.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a TDLeaf(lambda) machine learning algorithm. This is called at the end of every
 * game, and outputs an updated set of weights. The Alpha value is gradually reduced to zero,
 * as the temporal differences minimise.
 *
 * A number of papers were used to create this, including
 * Learning to Predict by the Methods of Temporal Differences, 1988
 * Combining Temporal Difference Learning with Game-Tree Search, Baxter et al, 1999
 *
 * COMP30024 Artificial Intelligence
 * Project Part B - Sem 1 2017
 * @author David Barrell (520704), Ivan Chee (736901)
 */
public class TDLeaf {

    private ArrayList<SliderState> stateHistory;
    private SliderGame game;
    private SliderAlphaBetaSearch search;
    private char player;
    private double alpha;
    private double lambda;

    public TDLeaf(ArrayList<SliderState> stateHistory, SliderGame game,
                  char player, double alpha, double lambda) {
        this.stateHistory = stateHistory;
        this.game = game;
        this.player = player;
        this.alpha = alpha;
        this.lambda = lambda;
        this.search = new SliderAlphaBetaSearch(game,-1D,1D,2);
    }

    public ArrayList<Double> updateWeights(ArrayList<Double> weights) {

        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, weights.get(i) + alpha*update(i));
        }

        return weights;

    }

    private double update(int i) {
        double sum = 0D;
        for (int t = 1; t < stateHistory.size(); t++) {
            SliderState state = stateHistory.get(t-1);
            List<SliderGame.WeightFeature> evalList
                    = game.evalFeatures(state, player);
            double featureValue = evalList.get(i).getValue();
            sum += featureValue * Math.pow(sech(search.eval(state,player)),2) * sumDiff(t);
        }

        return sum;
    }

    private double sumDiff(int t) {
        double sum = 0D;
        for (int j = 1; j < stateHistory.size(); j++) {
            sum += Math.pow(lambda,j-t) * diff(j);
        }

        return sum;
    }

    private double diff(int t) {
        try {
            return search.eval(stateHistory.get(t), player)
                    - search.eval(stateHistory.get(t - 1), player);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(stateHistory.get(t));
            return 0D;
        }
    }

    private double sech(double x) {
        return 2 / (Math.exp(x) + Math.exp(-x));
    }
}
