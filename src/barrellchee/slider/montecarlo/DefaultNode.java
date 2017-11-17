package barrellchee.slider.montecarlo;

import barrellchee.slider.montecarlo.Node;
import barrellchee.slider.montecarlo.Transition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultNode<T extends Transition> extends Node<Transition> {

	/**
	 * Always loosing node get a value of 0.
	 * An unexplored node is potentially a best choice than a node with no win.
	 */
	public static final double UNEXPLORED_VALUE = 0.5;

	private final Map<Transition, Node<Transition>> childs;
	private final Map<Integer, Integer> wins;
	private int simulations = 0;

	/**
	 * Initializes a DefaultNode instance
     * @param parent   : The parent node
     * @param terminal : Whether the node is a leaf
	 */
	public DefaultNode(Node<Transition> parent, boolean terminal) {
		super(parent, terminal);
		childs = new HashMap<>();
		wins = new HashMap<>();
	}

	/**
     * A leaf node is a node with no children.
     * There are two cases where a node can be leaf :
     * - The node is a terminal node
     * - The node has never been expanded
     * In both cases getTransitionsAndNodes() returns an empty map
     * @return : true if the node is a leaf node
     */
	@Override
	public boolean isLeaf() {
		return childs.isEmpty();
	}

	/**
     * Number of simulations back-propagated to this node
     * @return : Number of simulations back-propagated to this node
     */
	@Override
	public long simulations() {
		return simulations;
	}

	/**
     * Number of simulation back-propagated to this node where the given player has won
     * @param player : The player
     * @return       : Number of simulation back-propagated to this node where the given player has won
     */
	@Override
	public long wins(int player) {
		Integer w = wins.get(player);
		if (w == null) {
			return 0;
		} else {
			return w;
		}
	}

	/**
     * Propagate the result of a simulation to this node.
     * After a call to this method, simulations() is incremented as well as
     * wins(int) for the given winner.
     * @param winner : The winner of the back-propagated simulation
     */
	@Override
	public void result(int winner) {
		simulations++;
		Integer w = wins.get(winner);
		if (w == null) {
			wins.put(winner, 1);
		} else {
			wins.put(winner, w + 1);
		}
	}

	/**
     * Get a map of all transitions and children of this node
     * @return : The return map MUST NOT be null
     * 			 If the node is the leaf node, then an empty map is returned
     */
	@Override
	public Map<Transition, Node<Transition>> getTransitionsAndNodes() {
		return childs;
	}

	/**
     * Returns a collection of all children of this node
     * @return : The return collection MUST NOT be null
     * 			 If the node is the leaf node, then an empty collection is returned
     */
	@Override
	public Collection<Node<Transition>> getChilds() {
		return childs.values();
	}

	/**
     * Get the child node reached by the given transition
     * @param transition : The transition to fetch the child node from
     * @return           : The child node reached by the given transition
     */
	@Override
	public Node<Transition> getNode(Transition transition) {
		if (childs.isEmpty()) {
			return null;
		}
		return childs.get(transition);
	}

	/**
     * Get the value of the node for the given player.
     * The node with the greater value will be picked
     * as the best choice for this player.
     * @param player : The player
     * @return       : The value of the node for the given player
     */
	@Override
	public double value(int player) {
		return wins(player);
	}

}
