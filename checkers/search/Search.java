package checkers.search;
import checkers.core.*;
import checkers.evaluator.V7.*;


public class Search {

	private static int depth = 0;
	private static SearchTree tree;
	private static int depthMod = 0;

	public Search(int d){

		tree = new SearchTree(d+8);
		depth = d;
	}

	public static Move minimax(Move move, int d, int player) {

		Board board = new Board(move.getMoveBoard(), player);
		MoveList moves = new MoveList();
		board.genAllMoves();
		moves.setAll(board.genMoveArray());

		if (moves.getSize() == 0){
			EvaluatorV7 eval = new EvaluatorV7();
			//if there is no moves, return previous move after evaluation.
			move.setEvalScore(eval.evFunc(move.getMoveBoard(), player));
			return move;}

		if (moves.getMove(0).isJump())
			depthMod = 9;
		else
			depthMod = 0;

		if (d < (depth - 1 + depthMod)) {
			tree.addMoves(moves, d);

			if (player == 1) {//White.
				for (int i = 0; i < tree.getMoves(d).getSize(); i++){
					Move foundMove = minimax(tree.getMove(d, i), d+1, player*-1);
					if (foundMove != null && foundMove.getEvalScore() > tree.getMove(d, i).getEvalScore())
						tree.getMove(d, i).setEvalScore(foundMove.getEvalScore());
				}
				return tree.bestMove(d, player);
			}
			else {//Black
				for (int i = 0; i < tree.getMoves(d).getSize(); i++){
					Move foundMove = minimax(tree.getMove(d, i), d+1, player*-1);
					if (foundMove != null && foundMove.getEvalScore() < tree.getMove(d, i).getEvalScore())
						tree.getMove(d, i).setEvalScore(foundMove.getEvalScore());
				}
				return tree.bestMove(d, player);
			}
		}
		else{
			//if node is a leaf node, evaluate
			EvaluatorV7 eval = new EvaluatorV7();
			move.setEvalScore(eval.evFunc(move.getMoveBoard(), player));
			return move;
		}
	}

}