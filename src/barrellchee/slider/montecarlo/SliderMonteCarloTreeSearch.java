package barrellchee.slider.montecarlo;

import barrellchee.slider.montecarlo.Node;
import barrellchee.slider.montecarlo.Path;
import barrellchee.slider.montecarlo.Transition;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Adapted from MonteCarloTreeSearch.java on GitHub by Antoine Vianey
 * https://github.com/avianey/mcts4j
 * Monte Carlo Tree Search Algorithm
 * @param <Transition> : A transition representing an atomic action that modifies the state
 * @param <Node> : A node that stores simulations and wins
 * @author    : David Barrell, Ivan Chee
 */
// TODO : Describe pass, skip actions
public abstract class SliderMonteCarloTreeSearch<T extends Transition, N extends Node<Transition>> {

	private final static Integer SIMULATION_LIMIT = 20;

	private Path<Transition, Node<Transition>> pathToRoot;

	/**
	 * Initializes a SliderMonteCarloTreeSearch instance
	 */
	public SliderMonteCarloTreeSearch() {
		reset();
	}

	/**
	 * Resets the path to root
	 */
	public void reset() {
//		root = newNode(null, false);
		pathToRoot = new Path<Transition, Node<Transition>>(newNode(null, false));
	}

	/**
	 * Get the best transition for the current player
	 * Playing a transition must be done by calling makeTransitionAndChangeRoot(Transition)
	 * unless next call to this method will rely on a wrong origin
	 * @return : The best transition for the current player
	 *           or null if the current player has no possible move
	 */
	public Transition getBestTransition() {
		// TODO: Do it in an interruptible Thread
		if (!getPossibleTransitions().isEmpty()) {
			Integer player = getCurrentPlayer();
			Path<Transition, Node<Transition>> path;
			Boolean halt = false;
			Integer simCount = 0;
			do {
				path = selection(pathToRoot, player);
				if (path != null) {
					// Tree has not been fully explored yet
					path = expansion(path);
					int winner = simulation();
					simCount ++;
					backPropagation(path, winner);
				}
			} while (path != null && !halt && simCount < SIMULATION_LIMIT);
			// State is restored
			assert player == getCurrentPlayer();
			Transition best = null;
			double bestValue = Double.NEGATIVE_INFINITY;
			// All possible actions are set on root node
			for (Map.Entry<Transition, ? extends Node<Transition>> e : pathToRoot.endNode().getTransitionsAndNodes().entrySet()) {
				double value = e.getValue().value(player);
				if (value > bestValue) {
					bestValue = value;
					best = e.getKey();
				}
			}
			return best;
		}
		return null;
	}

	/**
	 * Truncate the tree, keeping only the current root node and its sub-tree
	 * Sub-trees that do not contain the current root node are removed as well
	 */
	public void simplifyTree() {
		this.pathToRoot = new Path<Transition, Node<Transition>>(pathToRoot.endNode());
		this.pathToRoot.endNode().makeRoot();
	}

	/**
	 * Update the context and change the root of the tree to this context so
	 * that it reflects the realisation of the given transition
	 * This method is the same as makeTransition(Transition) but it also changes
	 * the root of the tree to the node reached by the given transition
	 * Must only be called with a transition returned by getBestTransition()
	 * @param transition
	 */
	public final void doTransition(Transition transition) {
		if (transition != null) {
			makeTransition(transition);
			pathToRoot.expand(transition, (Node<Transition>) pathToRoot.endNode().getNode(transition));
		}
	}

	/**
	 * Update the context and change the root of the tree to this context so
	 * that it reflects the rollback of the realisation of the given transition
	 * This method is the same as unmakeTransition(Transition) but it also changes
	 * the root of the tree to the origin node of the given transition in the tree
	 * Must only be called with the last transition passed to makeTransition(Transition)
	 * @param transition
	 */
	public final void undoTransition(Transition transition) {
		if (transition != null) {
			unmakeTransition(transition);
			pathToRoot.getNodes().pollLast();
		}
	}

	/**
	 * Select a non-terminal leaf node to expand expressed by a path from
	 * the current root node to the leaf node
	 * The selection is done by calling selectNonTerminalChildOf(Node) from
	 * child to child until we reach a leaf node
	 * @param root   : The path to the current root node
	 * @param player : The player for which we are seeking an optional transition
	 * @return       : The path to the leaf node to expand or null if none left to expand
	 */
	private Path<Transition, Node<Transition>> selection(final Path<Transition, Node<Transition>> root, Integer player) {
		Node<Transition> current = root.endNode();
		// TODO : Initialize with root path instead ???
		Path<Transition, Node<Transition>> path = new Path<Transition, Node<Transition>>(current);
		Map.Entry<Transition, Node<Transition>> next;
		while (!current.isLeaf()) {
			next = selectNonTerminalChildOf(current, player);
			if (next == null) {
				// Nothing to explore
				current.setTerminal(true);
				if (current == root.endNode()) {
					// Stuck at root node
					return null;
				} else {
					// Get back to the parent
					current = (Node<Transition>) current.getParent();
					unmakeTransition(path.getNodes().pollLast().getKey());
				}
			} else {
				current = next.getValue();
				path.expand(next.getKey(), current);
				makeTransition(next.getKey());
			}
		}
		return path;
	}

	/**
	 * Expand the leaf node by creating every child node
	 * The leaf node to expand must not be a terminal node
	 * After expansion, the leaf node has all of its children created
	 * @param path : A path to a non-terminal leaf node
	 * @return     : The path to run the random simulation from
	 *               (The expanded node might be a terminal node)
	 */
	private Path<Transition, Node<Transition>> expansion(final Path<Transition, Node<Transition>> path) {
		if (path == null) {
			throw new IllegalArgumentException("The node to expand must not be null");
		}
		Set<Transition> possibleTransitions = getPossibleTransitions();
		if (!possibleTransitions.isEmpty()) {
			// Choose the child to expand
			Transition transition = expansionTransition(possibleTransitions);
			// Add every child node to the tree
			for (Transition possibleTransition : possibleTransitions) {
				if (possibleTransition.equals(transition)) {
					continue;
				}
				makeTransition(possibleTransition);
				Node<Transition> node = newNode(path.endNode(), isOver());
				path.endNode().addChildNode(possibleTransition, node);
				unmakeTransition(possibleTransition);
			}
			// Expand the path with the chosen transition
			makeTransition(transition);
			Node<Transition> node = newNode(path.endNode(), isOver());
			path.endNode().addChildNode(transition, node);
			path.expand(transition, node);
		} else {
			return path;
			
			/*
			throw new IllegalStateException("Trying to expand a node with no possible transitions\n"
					+ "Only non terminal node could be expanded...\n"
					+ "Check the contract for " + SliderMonteCarloTreeSearch.class.getName() + ".selectNonTerminalChildOf()");
					*/
		}
		return path;
	}

	/**
	 * Run a random simulation from the expanded position to get a winner
	 * @return : The winner of the random simulation
	 */
	private int simulation() {
		LinkedList<Transition> transitions = new LinkedList<Transition>();
		while (!isOver()) {
			Set<Transition> possibleTransitions = getPossibleTransitions();
			if (possibleTransitions.isEmpty()) {
				break;
			}
			Transition transition = simulationTransition(possibleTransitions);
			makeTransition(transition);
			transitions.add(transition);
		}
		int winner = getWinner();
//		System.out.println(winner + "wins!");
//		System.out.println(this.toString());
		// Undo moves
		while (!transitions.isEmpty()) {
			unmakeTransition(transitions.pollLast());
		}
		// Game over
		return winner;
	}

	/**
	 * Propagate the winner from the expanded node up to the current node
	 * @param path   : The path starting at the current root node and leading
	 *                 to the expanded node
	 * @param winner : The winner of the simulation
	 */
	private void backPropagation(final Path<Transition, Node<Transition>> path, final int winner) {
		if (path != null) {
			Map.Entry<Transition, Node<Transition>> e;
			while ((e = path.getNodes().pollLast()) != null) {
				// Every node of the path except the root
				unmakeTransition(e.getKey());
				(e.getValue()).result(winner);
			}
			// The root node
			path.rootNode().result(winner);
		}
	}

	/**
	 * Method used to select a child of a node and reach a leaf node to expand
	 * @param node   : A node that has already been visited
	 * @param player : The player for which we are seeking a promising child node
	 * @return       : The next non terminal node in the selection step
	 *                 or null if there are no more child nodes to explore
	 *                 or null if there are only terminal child nodes
	 */
	public abstract Map.Entry<Transition, Node<Transition>> selectNonTerminalChildOf(Node<Transition> node, int player);

	/**
	 * Select the next transition during the simulation step
	 * @param possibleTransitions : The possible transitions from the current root node
	 * @return                    : The desired transition
	 *                              or null if possibleTransitions is null or empty
	 */
	public abstract Transition simulationTransition(Set<Transition> possibleTransitions);

	/**
	 * Choose the node to be expanded and to run the simulation from
	 * @param possibleTransitions : Possible transition of the selected node
	 *                              At least one possible transition
	 * @return                    : The desired transition to get the expanded node from
	 */
	public abstract Transition expansionTransition(Set<Transition> possibleTransitions);

	/**
	 * Update the context so it takes into account the realisation of the given transition
	 * Must only be called with a transition returned by getBestTransition()
	 * @param transition : A transition returned by getBestTransition() or null
	 */
	protected abstract void makeTransition(Transition transition);

	/**
	 * Update the context so that it takes into account the rollback of the given transition
	 * Must only be called with the last transition passed to makeTransition(Transition)
	 * @param transition : A transition returned by getBestTransition() or null
	 */
	// TODO : Handle errors when undoing transition after simplifyTree()
	protected abstract void unmakeTransition(Transition transition);

	/**
	 * Get possible transitions from the current position
	 * Returned transitions might involve any of the players, taking into account passes
	 * @return : Possible transitions for the current position or an empty set
	 *           if there are no possible transitions
	 */
	// TODO : return no transition <==> isOver() transitions when pass
	public abstract Set<Transition> getPossibleTransitions();

	/**
	 * Create a new node
	 * @param parent   : The parent of the created node
	 * @param terminal : True if the node represents a configuration where isOver() returns true
	 * @return         : The new node
	 *                   Must not be null
	 */
	public abstract Node<Transition> newNode(Node<Transition> parent, Boolean terminal);

	/**
	 * Must return true if there are no possible transitions from the current position
	 * @return : True if getPossibleTransitions() returns an empty set
	 */
	public abstract Boolean isOver();

	/**
	 * Return the index of the winner when isOver() returns true
	 * @return : The index of the winner
	 */
	// TODO : Handle draw (Also in node and backpropagation)
	public abstract int getWinner();

	/**
	 * Returns the index of the player for the current root node
	 * @return : Index of the plyaer for the current root node
	 */
	public abstract int getCurrentPlayer();

	/**
	 * Change current turn to the next player
	 * This method must not be used in conjunction with the makeMove() method
	 * Use it to implement a pass functionality
	 */
	public abstract void next();

	/**
	 * Change current turn to the previous player
	 * This method must not be used in conjunction with the unmakeMove() method
	 * Use it to implement an undo functionality
	 */
	public abstract void previous();

}
