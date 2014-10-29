package checkers.evaluator.V8;

public class BridgeCheck {

	private BridgeCheck(){}
	
	public static float bridgeScore(int[][] theBoard, int player){
		
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
}
