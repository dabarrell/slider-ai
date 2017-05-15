package barrellchee.slider.ai;

import aiproj.slider.Move;

/**
 * Adapted from Artificial Intelligence A Modern Approach (3rd Edition): page 169
 * by Ruediger Lunde
 * An algorithm for calculating minimax decisions
 * It returns the action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility.
 * The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to max depth, to determine the backed-up
 * value of a state.
 * 
 * @author David Barrell, Ivan Chee
 */
public class SliderMinimaxSearch {

	private SliderGame game;
	private Integer maxDepth;
	private Integer expandedNodes;

	public SliderMinimaxSearch(SliderGame game, Integer maxDepth) {
		this.game = game;
		this.maxDepth = maxDepth;
	}

	public Move makeDecision(SliderState state) {
		expandedNodes = 0;
		Move result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		Character player = game.getPlayer(state);
		for (Move action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player, 1);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
	}

	public double maxValue(SliderState state, Character player, Integer depth) { // returns an utility value
		expandedNodes++;
		if (game.isTerminal(state) || depth >= maxDepth) {
			return game.getUtility(state, player);
		}
		double value = Double.NEGATIVE_INFINITY;
		for (Move action : game.getActions(state)) {
			value = Math.max(value, minValue(game.getResult(state, action), player, depth + 1));
		}
		return value;
	}

	public double minValue(SliderState state, Character player, Integer depth) { // returns an utility value
		expandedNodes++;
		if (game.isTerminal(state) || depth >= maxDepth) {
			return game.getUtility(state, player);
		}
		double value = Double.POSITIVE_INFINITY;
		for (Move action : game.getActions(state)) {
			value = Math.min(value, maxValue(game.getResult(state, action), player, depth + 1));
		}
		return value;
	}
}
