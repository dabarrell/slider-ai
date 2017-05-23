package barrellchee.slider.ai;

import aiproj.slider.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * action ordering. Maximal computation time is specified in seconds. The
 * algorithm is implemented as template method and can be configured and tuned
 * by subclassing.
 *
 * H is maximising
 * V is minimising
 * 
 * @author David Barrell, Ivan Chee
 */
public class SliderAlphaBetaSearch {

	protected SliderGame game;
	private double utilMax;
    private double utilMin;
    private int currDepthLimit;
	private boolean heuristicEvaluationUsed; // indicates that non-terminal
												// nodes
												// have been evaluated.
	private Timer timer;
	private boolean logEnabled;

	private int nodesExpanded;
	private int maxDepth;
	private long timeElapsed;

    private int maxDepthLimit;

	/**
	 * Creates a new search object for a given game.
	 *
	 * @param game
	 *            The game.
	 * @param utilMin
	 *            Utility value of worst state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param utilMax
	 *            Utility value of best state for this player. Supports
	 *            evaluation of non-terminal states and early termination in
	 *            situations with a safe winner.
	 * @param time
	 *            Maximal computation time in seconds.
	 */
	public SliderAlphaBetaSearch(SliderGame game, double utilMin, double utilMax,
                                              double time, int depthLimit) {
		this.game = game;
		this.utilMin = utilMin;
		this.utilMax = utilMax;
		this.timer = new Timer(time);
		this.maxDepthLimit = depthLimit;
	}

	public void enableLogging() {
		logEnabled = true;
	}

	/**
	 * Template method controlling the search. It is based on iterative
	 * deepening and tries to make to a good decision in limited time. Credit
	 * goes to Behi Monsio who had the idea of ordering actions by utility in
	 * subsequent depth-limited search runs.
	 */
	public Move makeDecision(SliderState state) {
		Logger log = new Logger();
		nodesExpanded = 0;
		maxDepth = 0;
		Character player = game.getPlayer(state);

		boolean maximising = player == 'H';

		List<Move> actions = game.getActions(state);
        if (actions.get(0).i == -1)
            return null;

		List<Move> results = orderActions(state, actions, player);
		timer.start();
		currDepthLimit = 0;
		do {
            incrementDepthLimit();
            log.append("depth " + currDepthLimit + ": ");
			heuristicEvaluationUsed = false;
			ActionStore newResults = new ActionStore(maximising);
			for (Move action : results) {
                double value;
                if (maximising)
				    value = maxValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY, 1);
                else
                    value = minValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
                            Double.POSITIVE_INFINITY, 1);
				if (timer.timeOutOccured())
					break; // exit from action loop
				newResults.add(action, value);
				log.append(action).append("->").append(String.format("%.2f", value)).append(" | ");
			}
            log.println();
			if (newResults.size() > 0) {
                results = newResults.actions;
                if (!timer.timeOutOccured()) {
                    if (hasSafeWinner(newResults.utilValues.get(0)))
                        break; // exit from iterative deepening loop
                    else if (newResults.size() > 1
                            && isSignificantlyBetter(newResults.utilValues.get(0), newResults.utilValues.get(1)))
                        break; // exit from iterative deepening loop
                }
            }
		} while (currDepthLimit < maxDepthLimit && !timer.timeOutOccured() && heuristicEvaluationUsed);

        return results.get(0);
	}

	// returns an utility value
	private double maxValue(SliderState state, Character player, double alpha, double beta, int depth) {
		updateMetrics(depth);
		if (game.isTerminal(state) || game.checkMoveHistory(state,player) || depth >= currDepthLimit || timer.timeOutOccured()) {
			return eval(state, player);
		} else {
			double value = Double.NEGATIVE_INFINITY;
			for (Move action : orderActions(state, game.getActions(state), player)) {
				value = Math.max(value, minValue(game.getResult(state, action),
						player, alpha, beta, depth + 1));
				if (value >= beta)
					return value;
				alpha = Math.max(alpha, value);
			}
			return value;
		}
	}

	// returns an utility value
    private double minValue(SliderState state, Character player, double alpha, double beta, int depth) {
		updateMetrics(depth);
		if (game.isTerminal(state) || game.checkMoveHistory(state,player) || depth >= currDepthLimit || timer.timeOutOccured()) {
			return eval(state, player);
		} else {
			double value = Double.POSITIVE_INFINITY;
			for (Move action : orderActions(state, game.getActions(state), player)) {
				value = Math.min(value, maxValue(game.getResult(state, action),
						player, alpha, beta, depth + 1));
				if (value <= alpha)
					return value;
				beta = Math.min(beta, value);
			}
			return value;
		}
	}

	private void updateMetrics(int depth) {
	    nodesExpanded++;
	    maxDepth = Math.max(maxDepth, depth);
        timeElapsed = timer.time();
	}

	/** Returns some statistic data from the last search. */
    public int getNodesExpanded() {
        return nodesExpanded;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    /**
	 * Primitive operation which is called at the beginning of one depth limited
	 * search step. This implementation increments the current depth limit by
	 * one.
	 */
	private void incrementDepthLimit() {
		currDepthLimit++;
	}

	/**
	 * Primitive operation which is used to stop iterative deepening search in
	 * situations where a clear best action exists.
	 */
    private boolean isSignificantlyBetter(double newUtility, double utility) {
        return Math.abs(newUtility - utility) > (utilMax - utilMin) * 0.3;
    }

	/**
	 * Primitive operation which is used to stop iterative deepening search in
	 * situations where a safe winner has been identified. This implementation
	 * returns true if the given value (for the currently preferred action
	 * result) is the highest or lowest utility value possible.
	 */
    private boolean hasSafeWinner(double resultUtility) {
        return Math.abs(resultUtility - (utilMin + utilMax) / 2) > 0.4
                * utilMax - utilMin;
    }

    public double eval(SliderState state, Character player) {
        // TODO: use a cache to store identical cases
        double value;
        if (game.isTerminal(state)) {
            value = game.getUtility(state);
        } else {
            heuristicEvaluationUsed = true;
            value = (utilMin + utilMax) / 2;
            List<SliderGame.WeightFeature> evalList = game.evalFeatures(state, player);
            double tempVal = 0D;
            for (SliderGame.WeightFeature wf : evalList) {
                tempVal += wf.getWeight() * wf.getValue();
            }
            if (player == 'H') {
                value += tempVal;
            } else {
                value -= tempVal;
            }
        }

        return Math.tanh(value);
    }

	/**
	 * Orders moves with respect to the state's analyseMoveValue() method
	 * TODO: prioritise moves towards the opponent's end
	 */
    private List<Move> orderActions(SliderState state,
								   List<Move> moves, Character player) {
		List<MoveValuePair> actionEstimates = new ArrayList<>(
				moves.size());
		for (Move move : moves) {
			actionEstimates.add(new MoveValuePair(move,
					state.analyseMoveValue(move, player)));
		}
		Collections.sort(actionEstimates);
		List<Move> result = new ArrayList<>();
		for (MoveValuePair pair : actionEstimates) {
			result.add(pair.move);
//				System.out.println(pair.move + " valued at " + pair.value);
		}

		return result;
	}

	private class Timer {
		private long duration;
		private long startTime;

		Timer(double maxSeconds) {
			this.duration = (long)(1000D * maxSeconds);
		}

		void start() {
			startTime = System.currentTimeMillis();
		}

		boolean timeOutOccured() {
			return System.currentTimeMillis() > startTime + duration;
		}

        long time() {
            return System.currentTimeMillis() - startTime;
        }
	}

	/** Orders actions by utility. */
	private static class ActionStore {
		private List<Move> actions = new ArrayList<>();
		private List<Double> utilValues = new ArrayList<>();
		boolean maximising;

        public ActionStore(boolean maximising) {
            this.maximising = maximising;
        }

        void add(Move action, double utilValue) {
			int idx ;
			if (maximising)
			    for (idx = 0; idx < actions.size() && utilValue <= utilValues.get(idx); idx++);
			else
                for (idx = 0; idx < actions.size() && utilValue >= utilValues.get(idx); idx++);
			actions.add(idx, action);
			utilValues.add(idx, utilValue);
		}

		int size() {
			return actions.size();
		}
	}

	private class MoveValuePair implements Comparable<MoveValuePair> {
		private Move move;
		private double value;

		private MoveValuePair(Move move, double utility) {
			this.move = move;
			this.value = utility;
		}

		@Override
		public int compareTo(MoveValuePair pair) {
			return (int) (pair.value - value);
		}
	}

	private class Logger {
	    private StringBuffer sb;

        Logger append(Object o) {
            if (!logEnabled)
                return this;
            if (sb == null) {
                sb = new StringBuffer(o.toString());
            } else {
                sb.append(o);
            }
            return this;
        }

        void println() {
            if (!logEnabled || sb == null)
                return;
            System.out.println(sb);
            sb = null;
        }
    }
}
