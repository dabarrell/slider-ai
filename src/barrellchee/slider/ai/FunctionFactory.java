package barrellchee.slider.ai;

/**
 * Created by barrelld on 2/05/2017.
 */

import aima.core.agent.Action;
import aima.core.environment.nqueens.PlaceQueenAction;
import barrellchee.slider.SliderBoard;

import java.util.LinkedHashSet;
import java.util.Set;

public class FunctionFactory {
    private static aima.core.search.framework.ActionsFunction _actionsFunction = null;
    private static aima.core.search.framework.ResultFunction _resultFunction = null;

    public FunctionFactory() {
    }

    public static aima.core.search.framework.ActionsFunction getActionsFunction() {
        if(null == _actionsFunction) {
            _actionsFunction = new ActionsFunction();
        }

        return _actionsFunction;
    }

    public static aima.core.search.framework.ResultFunction getResultFunction() {
        if(null == _resultFunction) {
            _resultFunction = new ResultFunction();
        }

        return _resultFunction;
    }

//    private static NQueensBoard placeQueenAt(int x, int y, NQueensBoard parentBoard) {
//        NQueensBoard newBoard = new NQueensBoard(parentBoard.getSize());
//        List<XYLocation> queenPositionsOnParentBoard = parentBoard.getQueenPositions();
//        queenPositionsOnParentBoard.add(new XYLocation(x, y));
//        newBoard.setBoard(queenPositionsOnParentBoard);
//        return newBoard;
//    }

    private static class ResultFunction implements aima.core.search.framework.ResultFunction {
        private ResultFunction() {
        }

        public Object result(Object s, Action a) {
            if(a instanceof PlaceQueenAction) {
                PlaceQueenAction pqa = (PlaceQueenAction)a;
//                return aima.core.environment.nqueens.NQueensFunctionFactory.placeQueenAt(pqa.getX(), pqa.getY(), (NQueensBoard)s);
                return s;
            } else {
                return s;
            }
        }
    }

    private static class ActionsFunction implements aima.core.search.framework.ActionsFunction {
        private ActionsFunction() {
        }

        public Set<Action> actions(Object state) {
            SliderBoard board = (SliderBoard) state;
            Set<Action> actions = new LinkedHashSet();
//            int numQueens = board.getNumberOfQueensOnBoard();
//            int boardSize = board.getSize();
//
//            for(int i = 0; i < boardSize; ++i) {
//                XYLocation newLocation = new XYLocation(numQueens, i);
//                if(!board.isSquareUnderAttack(newLocation)) {
//                    actions.add(new PlaceQueenAction(newLocation.getXCoOrdinate(), newLocation.getYCoOrdinate()));
//                }
//            }

            return actions;
        }
    }
}
