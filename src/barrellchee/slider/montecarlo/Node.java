package barrellchee.slider.montecarlo;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a node in the search tree
 * @param <Transition> : A transition representing an atomic action that modifies the state
 * @author    : David Barrell, Ivan Chee
 */
public abstract class Node<T extends Transition> {
    
    private boolean terminal;
    private Node<Transition> parent;
    
    /**
     * Initializes a Node instance
     * @param parent   : The parent node
     * @param terminal : Whether the node is a leaf
     */
    public Node(Node<Transition> parent, boolean terminal) {
        this.terminal = terminal;
        this.parent = parent;
    }
    
    /**
     * A leaf node is a node with no children.
     * There are two cases where a node can be leaf :
     * - The node is a terminal node
     * - The node has never been expanded
     * In both cases getTransitionsAndNodes() returns an empty map
     * @return : true if the node is a leaf node
     */
    public abstract boolean isLeaf();
    
    /**
     * A node is terminal when there is no child to explore.
     * The sub-Tree of this node has been fully explored or the node corresponding
     * to a configuration where isOver() returns true.
     * @return : true if a node is a terminal node
     */
    public boolean isTerminal() {
        return this.terminal;
    }
    
    /**
     * Sets the terminal value of a node
     * @param terminal : Whether the node is a terminal node
     */
    public void setTerminal(boolean terminal) {
    	this.terminal = terminal;
    }
    
    /**
     * Add a child node and associate it with the given transition
     * @param transition : The transition leading to the child
     * @param child      : The child node
     */
    public void addChildNode(Transition transition, Node<Transition> child) {
    	getTransitionsAndNodes().put(transition, child);
    }
    
    /**
     * Number of simulations back-propagated to this node
     * @return : Number of simulations back-propagated to this node
     */
    public abstract long simulations();
    
    /**
     * Number of simulation back-propagated to this node where the given player has won
     * @param player : The player
     * @return       : Number of simulation back-propagated to this node where the given player has won
     */
    public abstract long wins(int player);
    
    /**
     * Propagate the result of a simulation to this node.
     * After a call to this method, simulations() is incremented as well as
     * wins(int) for the given winner.
     * @param winner : The winner of the back-propagated simulation
     */
    public abstract void result(int winner);
    
    /**
     * Get the value of the node for the given player.
     * The node with the greater value will be picked
     * as the best choice for this player.
     * @param player : The player
     * @return       : The value of the node for the given player
     */
    public abstract double value(int player);
    
    /**
     * Get a map of all transitions and children of this node
     * @return : The return map MUST NOT be null
     * 			 If the node is the leaf node, then an empty map is returned
     */
    public abstract Map<Transition, Node<Transition>> getTransitionsAndNodes();
    
    /**
     * Returns a collection of all children of this node
     * @return : The return collection MUST NOT be null
     * 			 If the node is the leaf node, then an empty collection is returned
     */
    public abstract Collection<Node<Transition>> getChilds();
    
    /**
     * Get the child node reached by the given transition
     * @param transition : The transition to fetch the child node from
     * @return           : The child node reached by the given transition
     */
    public abstract Node<Transition> getNode(Transition transition);

    /**
     * Returns the parent of this node
     * @return : The parent of this node
     */
    public Node<Transition> getParent() {
        return parent;
    }
    
    /**
     * Make this node a root node by removing the reference to it's parent
     */
    public void makeRoot() {
    	this.parent = null;
    }
    
}
