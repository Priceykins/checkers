package checkers.search;
import checkers.core.Move;
import checkers.core.MoveList;
import checkers.evaluator.V7.EvaluatorV7;

public class SearchTree {

	private MoveList[] tree;
	private int depth;

	public SearchTree(int d){

		depth = d;
		tree = new MoveList[depth];

		
	}

	public void addMoves(MoveList moveList, int i){


		tree[i] = moveList;
	}

	public MoveList getMoves(int i){return tree[i];}

	public Move getMove(int i, int j){

		return tree[i].getMove(j);
	}

	public Move bestMove(int depth, int col){

		Move bestMove = null;
		float bestW = (float) -1E9;
		float bestB = (float) 1E9;

		if (col == 1){
			for (int i = 0; i < tree[depth].getSize(); i++){
				if (tree[depth].getMove(i).getEvalScore() >= bestW){
					bestW = tree[depth].getMove(i).getEvalScore();
					bestMove = tree[depth].getMove(i);
				}
			}
			return bestMove;
		}

		else {
			for (int i = 0; i < tree[depth].getSize(); i++){
				if (tree[depth].getMove(i).getEvalScore() <= bestB){
					bestB = tree[depth].getMove(i).getEvalScore();
					bestMove = tree[depth].getMove(i);
				}
			}
			return bestMove;
		}
	}
}

