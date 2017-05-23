package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.CompactBoard2;

/**
 * Represents a transition in game state, analogous to a player moving a piece
 * @author    : David Barrell, Ivan Chee
 */
public class SliderTransition implements Cloneable, Transition {
    private CompactBoard2 b;
    private Character p;
    private Move m;

    /**
     * Initializes a SliderTransition instance
     * @param dimension : The dimension of the board
     * @param player    : The player of this transition
     * @param move      : The move made during this transition
     * @param board     : The state of the board as a String
     */
    public SliderTransition(Integer dimension, Character player, Move move, String board) {
    	this.p = player;
    	this.m = move;
    	this.b = new CompactBoard2();
    	this.b.initBoard(dimension, board, player);
    }

    /**
     * Returns the player of this transition
     * @return : The player
     */
    public int getPlayer() {
    	return this.p == 'H' ? 0 : 1;
    }

    /**
     * Returns the move made during this transition
     * @return : The move
     */
    public Move getMove() {
    	return this.m;
    }

    /**
     * Sets the move
     * @param move : The move to be set
     */
    public void setMove(Move move) {
    	this.m = move;
    }

    /**
     * Returns the current state of the board
     * @return : The state of the board as a String
     */
    public CompactBoard2 getBoard() {
    	return this.b;
    }
}
