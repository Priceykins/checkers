package checkers.evaluator;

/**
 * The Class Evaluator.
 * 
 * Evaluates a board, and returns the boards score
 */
public class EvaluatorV7 {

	private int [][] theBoard;

	private int player;

	private int phase = 1;

	public EvaluatorV7(){
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
		
		theBoard = aBoard;
		player = p;

		float finalScore = 0;

		float pieceScore = pieceScore();
		
		float bridScore = bridges();

		phase = phaseCheck();


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

	/**
	 * Phase check
	 * Returns the phase of the game based on the total pieces left on the board
	 * 
	 * @return the phase of the game
	 */

	private int phaseCheck(){

		int pieces = countPieces();

		//Beginning Phase (24 to 17 pieces on board)
		if (pieces > 16){
			return 1;
		}
		//mid phase (16 to 7 pieces on board)
		else if (pieces > 6 && pieces < 17){
			return 2;
		}
		//end phase (6 or less pieces on board)
		else{
			return 3;
		}

	}
	
	/**
	 * Count Pieces
	 * Counts the pieces on the board
	 * 
	 * @return total Pieces on the board
	 */
	private int countPieces(){
		
		int totalPieces = 0;

		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){
				if (theBoard[row][col] == 2 || theBoard[row][col] == -2 || theBoard[row][col] == 4 || theBoard[row][col] == -4)
					totalPieces++;
			}
		}
		return totalPieces;
	}

	private float bridges(){

		int white = 0, black = 0, bridgeScore = 2;

		if (player == 1){

			for (int row = 0; row < 8; row++){
				for (int col = 0; col < 8; col++){
						if ((theBoard[row][col] == -2 || theBoard[row][col] == -4) && row+2 < 8 && col+2 < 8 && (theBoard[row][col] == 2 || theBoard[row][col] == 4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							white += bridgeScore;
						if ((theBoard[row][col] == -2 || theBoard[row][col] == -4) && row+2 < 8 && col-2 > 0 && (theBoard[row][col] == 2 || theBoard[row][col] == 4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							white += bridgeScore;
						if ((theBoard[row][col] == -4) && row-2 > 0 && col-2 > 0 && (theBoard[row][col] == 2 || theBoard[row][col] == 4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							white += bridgeScore;
						if ((theBoard[row][col] == -4) && row-2 > 0 && col+2 < 8 && (theBoard[row][col] == 2 || theBoard[row][col] == 4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							white += bridgeScore;
				}
			}
			return white;
		}
		else{

			for (int row = 0; row < 8; row++){
				for (int col = 0; col < 8; col++){
						if ((theBoard[row][col] == 4) && row+2 < 8 && col+2 < 8 && (theBoard[row][col] == -2 || theBoard[row][col] == -4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							black += bridgeScore;
						if ((theBoard[row][col] == 4) && row+2 < 8 && col-2 > 0 && (theBoard[row][col] == -2 || theBoard[row][col] == -4) && (theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4))
							black += bridgeScore;
						if ((theBoard[row][col] == 2 || theBoard[row][col] == 4) && row-2 > 0 && col-2 > 0 && (theBoard[row][col] == -2 || theBoard[row][col] == -4) && ((theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4)))
							black += bridgeScore;
						if ((theBoard[row][col] == 2 || theBoard[row][col] == 4) && row-2 > 0 && col+2 < 8 && ((theBoard[row][col] == 2 || theBoard[row][col] == 4 || theBoard[row][col] == -2 || theBoard[row][col] == -4)))
							black += bridgeScore;
				}
			}
			return black;
		}
	}

	private float pieceScore(){

		int white = 0;
		int black = 0;

		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){

				//white evaluation
				if (theBoard[row][col] > 1){
					//if piece is king
					if (theBoard[row][col] == 4){
						white += theBoard[row][col];
					}
					//if piece is normal
					else if (theBoard[row][col] == 2){
						white += (theBoard[row][col]);
					}
				}

				//black evaluation
				else if (theBoard[row][col] < -1){
					//if piece is king
					if (theBoard[row][col] == -4){
						black += ((theBoard[row][col])*-1);
				}
					//if piece is normal
					else if (theBoard[row][col] == -2){
						black += ((theBoard[row][col])*-1);
					}
				}
			}	
		}

		float score = 0;
		//if win situation return +infinity
		if (player == 1 && black == 0){
			return 1000000000;
		}
		//if loss situation return -infinity
		else if (player == -1 && black == 0){
			return -1000000000;
		}
		//if win situation return +infinity
		else if (player == -1 && white == 0){
			return 1000000000;
		}
		//if loss situation return -infinity
		else if (player == 1 && white == 0){
			return -1000000000;
		}
			
		//if non win situation return score of board
		if (player == -1){
			score = ((float)black - (float)white)/((float)black + (float)white);
		}
		
		else{
			score = ((float)white - (float)black)/((float)white + (float)black);
		}

		return score;
	}

}