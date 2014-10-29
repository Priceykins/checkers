package checkers.evaluator.V8;

/**
 * The Class Evaluator.
 * 
 * Evaluates a board, and returns the boards score
 */
public class Evaluator {

	public static int count = 0;

	private Evaluator(){
		
		count = count + 1;
	}

	/**
	 * Ev func.
	 * 
	 * Calculates the score for a board
	 * linear equation with weighted variables
	 * first variable is piece score
	 * 
	 * 
	 * when other variables added, they will have differing weights
	 *
	 * 
	 * @return the score of the board supplied
	 */
	public float evFunc(int [][] aBoard, int p){
		
		float finalScore = 0;

		float pieceScore = PieceScore.getScore(aBoard, p);
		
		float bridScore = BridgeCheck.bridgeScore(aBoard, p);

		int phase = PhaseCheck.getPhase(aBoard);


		if (phase == 1){
			finalScore = (float) ((100*pieceScore) + (0.05*bridScore));
		}
		else if (phase == 2){
			finalScore = (float) ((100*pieceScore) + (0.05*bridScore));
		}
		else if (phase == 3){
			finalScore = (float) ((100*pieceScore) + (0.05*bridScore));
		}

		if (p == 1){
			return finalScore;
		}
		else{
			return finalScore*-1;
		}
	}

	
	public static void resetCount(){count = 0;}

}