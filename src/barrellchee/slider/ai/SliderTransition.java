package barrellchee.slider.ai;

import aiproj.slider.Move;
import barrellchee.slider.CompactBoard2;

public class SliderTransition implements Cloneable, Transition {
    private CompactBoard2 b;
    private Character p;
    private Move m;

    public SliderTransition(Integer dimension, Character player, Move move, String board) {
    	this.p = player;
    	this.m = move;
    	this.b = new CompactBoard2();
    	this.b.initBoard(dimension, board, player);
    }

    public int getPlayer() {
    	return this.p == 'H' ? 0 : 1;
    }

    public Move getMove() {
    	return this.m;
    }

    public void setMove(Move move) {
    	this.m = move;
    }

    public CompactBoard2 getBoard() {
    	return this.b;
    }
}

