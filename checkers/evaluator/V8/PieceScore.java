package checkers.evaluator.V8;

public class PieceScore {

	private PieceScore(){}
	
	public static float getScore(int[][] theBoard, int player){
		
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
