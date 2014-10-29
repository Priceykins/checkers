package checkers.core;


// TODO: Auto-generated Javadoc
/**
 * The Class move holds the information on a single move
 * if the move is a jump, it holds a jump list of possible jumps
 * takes in a copy of current board and generates a board that shows the result of the move
 * 
 * @author Chris Price
 */
public class Move {

	/** The move coordinates */
	private int fromRow, fromCol, toRow, toCol, piece, level = 0;
	
	private final int EDGE_LENGTH = 8;
	
	/** The is jump */
	private boolean isJump = false, canBeDeleted = true;
	
	/** The move board */
	private int [][] baseBoard, moveBoard;
	
	private float evalScore = 0;
	
	private int piecesTaken;
	
	private int playerColour = 0;
	
	private boolean isHistoryMove = false;


	/**
	 * Instantiates a new move.
	 * 
	 * @param row the row coord of the piece
	 * @param col the col coord of the piece
	 * @param tRow the tRow coord of the move
	 * @param tCol the tCol coord of the move
	 * @param jump the jump
	 * @param b the board representation
	 */
	public Move(int row, int col, int tRow, int tCol, int p, boolean jump, int [][] b){

		fromRow = row;
		fromCol = col;
		toRow = tRow;
		toCol = tCol;
		isJump = jump;
		if (isJump)
			jumpList();
		baseBoard = b;
		piece = baseBoard[fromRow][fromCol];
		piecesTaken = 0;
		playerColour = p;
		if (p == 1)
			evalScore = -1000000000;
		else
			evalScore = 1000000000;
		this.genMoveBoard();
	}
	
	public Move(int[][] a){
		
		this.setMoveBoard(a);
	}

	
	/**
	 * Jump list
	 * Calls the method jump list to ensure only jumps are added to list
	 */
	public void jumpList(){

		isJump = true;
	}

	/**
	 * Gets the fromCol coordinate
	 * 
	 * @return fromCol
	 */
	public int getFromCol(){return fromCol;}
	
	/**
	 * Gets the fromRow coordColnate
	 * 
	 * @return fromRow
	 */
	public int getFromRow(){return fromRow;}
	
	/**
	 * Gets the to Col
	 * 
	 * @return the to Col
	 */
	public int getToCol(){return toCol;}
	
	/**
	 * Gets the to Row
	 * 
	 * @return the to Row
	 */
	public int getToRow(){return toRow;}
	
	/**
	 * Checks if is jump.
	 * 
	 * @return true, if is jump
	 */
	public boolean isJump(){return isJump;}

	/**
	 * Gen move board.
	 */
	public void genMoveBoard(){
		
		//Instantiates the move board
		moveBoard = new int [EDGE_LENGTH][EDGE_LENGTH];

		//copies the values of the baseboard to the move board
		for (int row = 0; row < EDGE_LENGTH; row++){
			for (int col = 0; col < EDGE_LENGTH; col++){
				moveBoard [row][col] = baseBoard[row][col];
			}
		}

		//if move is not a jump, generating the resultant board is below
		if (!isJump){
			int piece = baseBoard[fromRow][fromCol]; //gets value of piece
			moveBoard [fromRow][fromCol] = 0; //sets start square to empty
			moveBoard [toRow][toCol] = piece; //sets destination square to the value of piece
		}
		//if move is a jump, generation of board is more complex
		else{
			int piece = baseBoard[fromRow][fromCol];
			moveBoard [fromRow][fromCol] = 0;
			
			if (fromRow < toRow && fromCol < toCol){ //if jump is towards bottom right corner
				moveBoard [fromRow][fromCol] = 0;
				int dRow = fromRow+1, dCol = fromCol+1;
				moveBoard [dRow][dCol] = 0;
				moveBoard [toRow][toCol] = piece;
				
			}
			else if (fromRow < toRow && fromCol > toCol){ //if jump is towards bottom left corner
				moveBoard [fromRow][fromCol] = 0;	
				int dRow = fromRow+1, dCol = fromCol-1;
				moveBoard [dRow][dCol] = 0;
				moveBoard [toRow][toCol] = piece;
				
			}
			else if (fromRow > toRow && fromCol > toCol){ //if jump is towards top left corner
				moveBoard [fromRow][fromCol] = 0;
				
				int dRow = fromRow-1, dCol = fromCol-1;
				moveBoard [dRow][dCol] = 0;
				moveBoard [toRow][toCol] = piece;
				
			}
			else if (fromRow > toRow && fromCol < toCol){ //if jump is towards top right corner
				moveBoard [fromRow][fromCol] = 0;
				
				int dRow = fromRow-1, dCol = fromCol+1;
				moveBoard [dRow][dCol] = 0;
				moveBoard [toRow][toCol] = piece;
				
			}
		}
		
		if (toRow == 0 && piece == 2)	//determines whether move kings a piece by determining whether the destination row coord of the move
			// puts the piece on either row 0 (white kings) or row 7 (black kings) and whether piece is eligible to become king (eg, 2 or -2)
			moveBoard [toRow][toCol] = 4;
		else if (toRow == 7 && piece == -2)
			moveBoard [toRow][toCol] = -4;
		
		baseBoard = null;
	}
		
	public int[][] getMoveBoard(){
		
		return moveBoard;
	}
	
	public void setMoveBoard(int [][] b){moveBoard = b.clone();}
	
	public void canBeDeleted(){canBeDeleted = true;}
	
	public Boolean getCanBeDeleted(){return canBeDeleted;}
	
	public int getResultSq(int p, int q){return moveBoard [p][q];}
	
	public void setLevel(int l){
		level = l;
		piecesTaken = l+1;
		}
	
	public int getLevel(){return level;}
	
	public void setEvalScore(float d){
		evalScore = d;
	}
	
	public float getEvalScore(){return evalScore;}
	
	public int piecesTaken(){return piecesTaken;}
	
	public int getColour(){return playerColour;}
	
	public void setHistory(Boolean n){isHistoryMove = n;}
	
	public boolean isHistory(){return isHistoryMove;}
	
}


