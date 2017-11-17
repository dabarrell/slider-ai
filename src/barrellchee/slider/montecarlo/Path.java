package barrellchee.slider.montecarlo;

import barrellchee.slider.montecarlo.Node;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Adapted from MonteCarloTreeSearch.java on GitHub by Antoine Vianey
 * https://github.com/avianey/mcts4j
 * Represents a path of nodes used to traverse the search tree
 * @param <Transition> : A transition representing an atomic action that modifies the state
 * @param <N> : A node that stores simulations and wins
 * @author    : David Barrell, Ivan Chee
 */
public class Path<T extends Transition, N extends Node<Transition>> {
    
    private final N root;
    private final LinkedList<Map.Entry<Transition, N>> nodes;

    /**
     * Initializes a Path instance
     * @param root : The root node
     */
    public Path(N root) {
    	if (root == null) {
    		throw new IllegalArgumentException(
    				"Root " + Node.class.getName() + " of a " + Path.class.getName() + " MUST NOT be null");
    	}
        this.root = root;
        this.nodes = new LinkedList<Map.Entry<Transition, N>>();
    }
    
    /**
     * Expand the path with the given transition and node.
     * This method does not add the given node as child of endNode()
     * @param transition : The transition to expand
     * @param node       : The node associated with the transition
     */
    public void expand(Transition transition, N node) {
        nodes.addLast(new AbstractMap.SimpleEntry<Transition, N>(transition, node));
    }

    /**
     * Returns the nodes in this path
     * @return : The nodes in this path
     */
    public LinkedList<Map.Entry<Transition, N>> getNodes() {
        return nodes;
    }

    /**
     * Whether there are no nodes
     * @return : True if there are no nodes
     */
    public boolean isEmpty() {
    	if (nodes.size() == 1) {
    		nodes.clear();
    		return true;
    	}
        return nodes.isEmpty();
    }

    /**
     * Returns the last node or the root if there are none
     * @return : The last node or the root
     */
    public N endNode() {
    	if (nodes.isEmpty()) {
    		// TODO : or null
    		return root;
    	} else {
    		return nodes.getLast().getValue();
    	}
    }

    /**
     * Returnst the root node
     * @return : The root node
     */
    public N rootNode() {
    	return root;
    }

    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (Map.Entry<Transition, N> e : getNodes()) {
    		sb.append(" -> ");
    		sb.append(e.getKey());
    	}
    	return sb.toString();
    }
}
