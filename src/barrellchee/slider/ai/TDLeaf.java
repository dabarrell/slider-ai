package barrellchee.slider.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * ai-partB
 * Created by David Barrell on 24/5/17.
 */
public class TDLeaf {

    private ArrayList<SliderState> stateHistory;
    private SliderGame game;
    private SliderAlphaBetaSearch search;
    private char player;
    private double alpha;
    private double lambda;

    public TDLeaf(ArrayList<SliderState> stateHistory, SliderGame game, SliderAlphaBetaSearch search,
                  char player, double alpha, double lambda) {
        this.stateHistory = stateHistory;
        this.game = game;
        this.player = player;
        this.alpha = alpha;
        this.lambda = lambda;
        this.search = search;
    }

    public ArrayList<Double> updateWeights(ArrayList<Double> weights) {
//        ArrayList<Double> newWeights = new ArrayList<>();

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
        // TODO: Should this be leaf?
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
