package checkers.evaluator.V8;

public class PhaseCheck {

	/**
	 * Phase check
	 * Returns the phase of the game based on the total pieces left on the board
	 * 
	 * @return the phase of the game
	 */
	private PhaseCheck(){}
	
	public static int getPhase(int[][] theBoard){
		
		int totalPieces = 0;

		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){
				if (theBoard[row][col] == 2 || theBoard[row][col] == -2 || theBoard[row][col] == 4 || theBoard[row][col] == -4)
					totalPieces++;
			}
		}
		
		//Beginning Phase (24 to 17 pieces on board)
				if (totalPieces > 16){
					return 1;
				}
				//mid phase (16 to 7 pieces on board)
				else if (totalPieces > 6 && totalPieces < 17){
					return 2;
				}
				//end phase (6 or less pieces on board)
				else{
					return 3;
				}
		
	}
}
