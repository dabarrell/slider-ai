package barrellchee.slider.montecarlo;

import java.util.Map;

/**
 * Adapted from MonteCarloTreeSearch.java on GitHub by Antoine Vianey
 * https://github.com/avianey/mcts4j
 * Upper Confidence Bound 1 applied to Trees Formula
 * @param <T> : A transition representing an atomic action that modifies the state
 * @param <N> : A node that stores simulations and wins
 * @author    : David Barrell, Ivan Chee
 */
public abstract class UCT<T extends Transition, N extends Node<Transition>> extends SliderMonteCarloTreeSearch<Transition, Node<Transition>> {
    
    private static final double C = Math.sqrt(2);

    @Override
    public Map.Entry<Transition, Node<Transition>> selectNonTerminalChildOf(Node<Transition> node, int player) {
        double v = Double.NEGATIVE_INFINITY;
        Map.Entry<Transition, Node<Transition>> best = null;
        for (Map.Entry<Transition, Node<Transition>> e : node.getTransitionsAndNodes().entrySet()) {
            if (!e.getValue().isTerminal()) {
            	// w/n + C * Math.sqrt(ln(n(p)) / n)
            	// TODO : add a random hint to avoid ex-aequo
                double value = (e.getValue().simulations() == 0 ? 0 : (e.getValue().wins(player) / e.getValue().simulations())
                        + C * Math.sqrt(Math.log(node.simulations()) / e.getValue().simulations()));
                if (value > v) {
                    v = value;
                    best = (Map.Entry<Transition, Node<Transition>>) e;
                }
            }
        }
        return best;
    }
    
}
