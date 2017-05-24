package barrellchee.slider.ai;

import aiproj.slider.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * action ordering. It is time-limited per-move.
 *
 * This has been adapted from aima-java.
 *
 * COMP30024 Artificial Intelligence
 * Project Part B - Sem 1 2017
 * @author David Barrell (520704), Ivan Chee (736901)
 */
public class SliderAlphaBetaSearch {

	protected SliderGame game;
	private double utilMax;
    private double utilMin;
    private int currDepthLimit;
	private boolean heuristicEvaluationUsed;

	private Timer timer;
	private boolean logEnabled;

	private int nodesExpanded;
	private int maxDepth;
	private long timeElapsed;

    boolean maximising;

	/**
	 * Creates a new search instance.
	 *
	 * @param game The game.
	 * @param utilMin Minumum utility value.
	 * @param utilMax Maximum utility value.
	 * @param time Maximum time-per-move.
	 */
	public SliderAlphaBetaSearch(SliderGame game, double utilMin, double utilMax,
                                              double time) {
		this.game = game;
		this.utilMin = utilMin;
		this.utilMax = utilMax;
		this.timer = new Timer(time);
	}

    /**
     * Enables logging during execution of search.
     */
	public void enableLogging() {
		logEnabled = true;
	}

    /**
     * Implements the iterative deepening alpha beta search, with some modifications.
     * Allows for early exit if there is an obvious winner, based on the eval value of the
     * best move. Also exits if the time-per-move limit is hit.
     *
     * @param state The current state of the Slider game
     * @return the next move to make
     */
	public Move makeDecision(SliderState state) {
		Logger log = new Logger();
		nodesExpanded = 0;
		maxDepth = 0;
		Character player = game.getPlayer(state);

        maximising = player == 'H';

		List<Move> actions = game.getActions(state);
        if (actions.get(0).i == -1)
            return null;

		List<Move> results = orderActions(state, actions, player);
        int maxDepthLimit = calculateDepthLimit(results.size());
		timer.start();
		currDepthLimit = 0;
		do {
            currDepthLimit++;
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
                    // Exit from iterative deepening loop if there is a safe winner,
                    // or the best value action is significantly better than the rest.
                    if (hasSafeWinner(newResults.utilValues.get(0))
                            || (newResults.size() > 1
                            && isSignificantlyBetter(newResults.utilValues.get(0), newResults.utilValues.get(1))))
                        break;
                }
            }
		} while (currDepthLimit < maxDepthLimit && !timer.timeOutOccured() && heuristicEvaluationUsed);

        return results.get(0);
	}

    /**
     * maxValue internal method of a-b search.
     * @param state parent state
     * @param player player searching for move
     * @param alpha current alpha
     * @param beta current beta
     * @param depth current depth
     * @return the maximum utility value
     */
	private double maxValue(SliderState state, Character player, double alpha, double beta, int depth) {
		updateMetrics(depth);
        Character lastPlayer = Objects.equals(state.getPlayerToMove(), 'H')?'V':'H';
		if (game.isTerminal(state) || game.checkMoveHistory(state,lastPlayer) || depth >= currDepthLimit || timer.timeOutOccured()) {
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

    /**
     * minValue internal method of a-b search.
     * @param state parent state
     * @param player player searching for move
     * @param alpha current alpha
     * @param beta current beta
     * @param depth current depth
     * @return the minimum utility value
     */
    private double minValue(SliderState state, Character player, double alpha, double beta, int depth) {
		updateMetrics(depth);
		Character lastPlayer = Objects.equals(state.getPlayerToMove(), 'H')?'V':'H';
		if (game.isTerminal(state) || game.checkMoveHistory(state,lastPlayer) || depth >= currDepthLimit || timer.timeOutOccured()) {
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

    /**
     * Determines the maximum depth to iterate to, based on the number of available moves.
     *
     * @param numOfMoves number of available moves
     * @return max depth to iterate to
     */
	private int calculateDepthLimit(int numOfMoves) {
	    if (numOfMoves <= 8) {
	        return 9;
        } else if (numOfMoves <= 11) {
            return 8;
        } else {
            return 7;
        }
    }

    /**
     * Updates statistics of current search.
     *
     * @param depth current depth
     */
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
	 * Determines if, at current depth, the most valuable move is significantly better
     * than the next most valuable.
	 */
    private boolean isSignificantlyBetter(double newUtility, double utility) {
        return Math.abs(newUtility - utility) > (utilMax - utilMin) * 0.3;
    }

	/**
	 * Determines if, at current depth, the most valuable move is a safe winner.
	 */
    private boolean hasSafeWinner(double resultUtility) {
        return Math.abs(resultUtility - (utilMin + utilMax) / 2) > 0.4
                * utilMax - utilMin;
    }

    /**
     * Evaluates the score of a state, for a given player.
     *
     * @param state state to evaluate
     * @param player player to evaluate for
     * @return score for given player
     */
    public double eval(SliderState state, Character player) {
        // TODO: use a cache to store identical cases
        double value;
        if (game.isTerminal(state)) {
            return game.getUtility(state) * 2;
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

        return Math.tanh(value * 0.5);
    }

    /**
     * Orders moves with respect to the state's analyseMoveValue() method
     *
     * @param state Current state
     * @param moves List of moves to order
     * @param player Player about to move
     * @return Ordered list of moves
     */
    private List<Move> orderActions(SliderState state,
								   List<Move> moves, Character player) {
//        System.out.println(player);
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
		}

		return result;
	}

    /**
     * Helper classes
     */

    /**
     * Timer used to keep track of search execution time.
     */
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

	/**
     *  Orders actions by utility.
     **/
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

    /**
     * Stores a move with it's value
     */
	private class MoveValuePair implements Comparable<MoveValuePair> {
		private Move move;
		private double value;

		private MoveValuePair(Move move, double value) {
			this.move = move;
			this.value = value;
		}

		@Override
		public int compareTo(MoveValuePair pair) {
			return (int) (pair.value - value);
		}
	}

    /**
     * Logger for debugging purposes.
     */
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
