package checkers.core;
import java.util.*;
import java.io.*;
import javax.swing.*;
import checkers.utils.BoardUtils;

import checkers.search.*;

/**
 * This generates the board on which the game will be displayed
 * Uses an 8x8 array to represent a board state
 * 
 * @author Chris Price
 */
public class Board {

	/**
	 * the array that represents the board
	 */
	private int [][] board; //the array that represents the board


	/**
	 * the length of the edge of the board in squares
	 */
	private final static int EDGE_LENGTH = 8; //Number of squares on the board that can be occupied by pieces

	/**
	 * the location of the initial input file
	 * Used as it might be possible to add ability to play
	 * from saved board states, or from initial board states
	 */
	//where the initialisation is located
	private final static String NEW_GAME_FILE = "data//checkers Input.txt";

	//where save files are located
	private final static String SAVE_GAME_DIR = "saveGames//";

	private final static String FILE_EXTENSION = ".txt";

	private int colourToPlay = -1; // 1 = white, -1 = black

	private int playerColour = 1; // 1 = white, -1 = black

	private int whitePieces = 12, whiteKings = 0, blackKings = 0, blackPieces = 12; //holds how many pieces each side has, a value of 0 indicates that a side has lost

	private boolean hasNoWinner = true, draw = false;

	private int turnsAdvantage = 0;

	private int moveLevel = 0, maxMoveLevel = 0;

	private ArrayList<Move> deleteList;

	private Move history;

	private int searchDepth = 0;
	
	private BoardUtils utilities;

	/**
	 *Constructor for new game board
	 *Instantiates a new board
	 *Initialises the board for a new game
	 */
	public Board(){

		board = new int [EDGE_LENGTH][EDGE_LENGTH];
		
		utilities = new BoardUtils(board);
	}

	public Board(int [][] b, int p){

		board = b;
		colourToPlay = p;
		utilities = new BoardUtils(board);
	}

	public boolean loadBoard(Boolean newGame, String fileName){


		if (newGame){
			board = utilities.newBoard();
		}
		else{
			board = utilities.loadBoard(fileName);
			playerColour=utilities.getPlayerColour();
			colourToPlay=utilities.getColourToPlay();
			blackPieces=utilities.getBlackPieces();
			whitePieces=utilities.getWhitePieces();
			turnsAdvantage=utilities.getTurnsAdvantage();
			searchDepth=utilities.getSearchDepth();
		}


		history = new Move(board);
		return true;
	}

	public void setDifficulty(int d){
		searchDepth = d;
	}

	public void saveBoard(String f){

		utilities.saveBoard(f, this.getBoardStream(), blackPieces, whitePieces, turnsAdvantage, searchDepth);
	}

	/**
	 * Prints the board out to the console
	 */
	public String printBoard(){

		String output = "";

		//System.out.print("---------------------------------------\n");
		output += "---------------------------------------\n";

		for (int row = 0; row < EDGE_LENGTH; row++){
			for (int col = 0; col < EDGE_LENGTH; col++){
				if (board[row][col] < -1){
					//System.out.print(board[row][col] + " | ");
					output += board[row][col] + " | ";
				}
				else if (board[row][col] == -1){
					//System.out.print("   | ");
					output += "   | ";
				}
				else{
					//System.out.print(" " + board [row][col] + " | ");
					output += " " + board [row][col] + " | ";
				}
			}
			//System.out.println("\n---------------------------------------");
			output += "\n---------------------------------------\n";


		}
		return output;
	}

	/**
	 * Gets the board.
	 * 
	 * @return the board
	 */
	public int[][] getBoard(){
		int [][]copy = new int [8][8];

		//copies board to new board so it can be manipulated by other methods without affecting original
		for (int row = 0; row < EDGE_LENGTH; row++){
			for (int col = 0; col < EDGE_LENGTH; col++){
				copy [row][col] = board[row][col];
			}
		}
		return copy;
	}

	//sets human player colour
	public void setPlayerColour(int colour){

		playerColour = colour;
	}

	public void setColourToMove(int colour){

		colourToPlay = colour;
	}

	public void clear(){

		for (int row = 0; row < EDGE_LENGTH; row++)
			for (int col = 0; col < EDGE_LENGTH; col++)
				board[row][col] = 0;

		mList.reset();
	}

	public void clearMlist(){
		mList.reset();
	}

	public int getPlayer(){

		return playerColour;
	}

	public int getColourToMove(){

		return colourToPlay;
	}

	public boolean hasNoWinner(){

		return hasNoWinner;
	}

	public void setMaxLevel(){
		maxMoveLevel = 0;
	}

	public void gameOver(){
		hasNoWinner = false; 
	}

	public boolean isDraw(){
		return draw;
	}

	public void setHistory(Move m){
		history = m;
	}

	public Move getHistory(){
		return history;
	}



	//New move for use in move generator
	private Move thisMove;
	//creates new move list each time a move generator is called
	private MoveList mList = new MoveList();


	/**
	 * Instantiates a new move generator for the initial case of checking possible move destinations
	 * for pieces, to check for either jumps or non-movable spaces
	 * 
	 * @param j the column coord of the piece
	 * @param i the row coord of the piece
	 * @param piece the piece 2 = white, -2 = black, 4 = white king, -4 = black king, 0 = empty
	 */


	private void moveGenerator(int row, int col, int [][] board, int piece) {

		// defines all possible destinations for a single move
		moveLevel = 0;

		if (piece == 2 || piece == -2){
			normalMoves(row, col, board);
		}
		else{
			kingMoves(row, col, board);
		}
	}

	private void normalMoves(int row, int col, int [][] board){

		int colMod = -1; //the column modifier

		while (colMod < 2){

			if ((row + (colourToPlay * -1) > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && board [row + (colourToPlay * -1)][col + colMod] == 0 && !mList.isJump()){//<1>
				thisMove = new Move(row, col, row + (colourToPlay * -1), col + colMod, this.colourToPlay, false, board);//<1.1>
				mList.addMove(thisMove);//<1.2>
			} 
			else if ((row + (colourToPlay * -1) > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && (row + (2*(colourToPlay * -1)) > -1) && (row + (2*(colourToPlay * -1)) < 8) && ((col + (2*colMod)) > -1) && ((col + (2*colMod)) < 8) && ( board [row + (colourToPlay * -1)][col + colMod] == (board[row][col] * -1) || board [row + (colourToPlay * -1)][col + colMod] == ((board[row][col]*2) * -1) ) &&  board [row + (2*(colourToPlay * -1))][col + (2*colMod)] == 0){//<2>
				if (moveLevel > maxMoveLevel){
					maxMoveLevel = moveLevel;}
				thisMove = new Move(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), this.colourToPlay, true, board);//<2.1>
				thisMove.setLevel(moveLevel);
				mList.jumpList();//<2.2>
				mList.addMove(thisMove);//<2.3>
				jumpMoves(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), thisMove, thisMove.getMoveBoard());
			}

			colMod = colMod + 2;//<3>
		}
		moveLevel--;
	}

	//<1>Checks that the coordinate destination of any move is on the board and that it is empty for a normal move.
	//<1.1>Creates a new move
	//<1.2>adds the move to the main move list
	//<2>checks to see if a jump is available by checking an adjacent square for a piece of the opposite colour (king or normal) then if the square directly behind any if they are empty. Performs all checks from <1> also
	//<2.1> creates a new move
	//<2.2>rids of the move list of any non jump moves and sets a flag to allow no non-jump moves to be added
	//<2.3>adds move to the move list
	//<3> increments the column modifier by two. this allows all move directions to be checked, and also acts as a cut off for the while loop

	private void kingMoves(int row, int col, int [][] board){

		int colMod = -1; //the column modifier

		while (colMod < 2){

			/*System.out.println("(row + (colourToPlay*-1)): " + (row + (colourToPlay*-1)));
			System.out.println("(col + colMod): " + (col + colMod));
			System.out.println("(row + (2*(colourToPlay * -1)): " + (row + (2*(colourToPlay * -1))));
			System.out.println("((col + (2*colMod)): " + ((col + (2*colMod))));
			System.out.println(" board [row + (colourToPlay * -1)][col + colMod]: " +  board [row + (colourToPlay * -1)][col + colMod]);
			System.out.println(" board [row + (2*(colourToPlay * -1))][col + (2*colMod)]: " +  board [row + (2*(colourToPlay * -1))][col + (2*colMod)]);
			System.out.println("board[row][col]: " + board[row][col]);
			System.out.println("board[row][col]*-1: " + board[row][col]*-1);
			System.out.println("(board[row][col]/2)*-1: " + (board[row][col]/2)*-1);
			System.out.println("===============================");*/

			if ((row + (colourToPlay * -1) > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && board [row + (colourToPlay * -1)][col + colMod] == 0 && !mList.isJump()){//<1>
				thisMove = new Move(row, col, row + (colourToPlay * -1), col + colMod, this.colourToPlay, false, board);//<1.1>
				mList.addMove(thisMove);//<1.2>
			} 
			else if ((row + colourToPlay*-1 > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && (row + (2*(colourToPlay * -1)) > -1) && (row + (2*(colourToPlay * -1)) < 8) && ((col + (2*colMod)) > -1) && ((col + (2*colMod)) < 8) && ( board [row + (colourToPlay * -1)][col + colMod] == (board[row][col] * -1) || board [row + (colourToPlay * -1)][col + colMod] == (((board[row][col])/2) * -1) ) &&  board [row + (2*(colourToPlay * -1))][col + (2*colMod)] == 0){//<2>
				thisMove = new Move(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), this.colourToPlay, true, board);//<2.1>
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				thisMove.setLevel(moveLevel);
				mList.jumpList();//<2.2>
				mList.addMove(thisMove);//<2.3>
				kingJumps(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), thisMove, thisMove.getMoveBoard());
			}
			colMod = colMod + 2;//<3>
		}

		colMod = -1;

		while (colMod < 2){	
			if ((row - (colourToPlay*-1) > -1) && (row - (colourToPlay*-1) < 8) && (col - colMod > -1) && (col - colMod < 8) && board [row - (colourToPlay*-1)][col - colMod] == 0 && !mList.isJump()){//<3>
				thisMove = new Move(row, col, row - (colourToPlay*-1), col - colMod, this.colourToPlay, false, board);//<3.1>
				mList.addMove(thisMove);//<3.2>
			} 
			else if ((row - (colourToPlay*-1) > -1) && (row - (colourToPlay*-1) < 8) && (col - colMod > -1) && (col - colMod < 8) && (row - (2*(colourToPlay * -1)) > -1) && (row - (2*(colourToPlay * -1)) < 8) && ((col - (2*colMod)) > -1) && ((col - (2*colMod)) < 8) && ( board [row - (colourToPlay * -1)][col - colMod] == (board[row][col] * -1) || board [row - (colourToPlay * -1)][col - colMod] == ((board[row][col]/2) * -1) ) &&  board [row - (2*(colourToPlay*-1))][col - (2*colMod)] == 0){//<4>
				thisMove = new Move(row, col, row - (2*(colourToPlay * -1)), col - (2*colMod), this.colourToPlay, true, board);//<4.1>
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				thisMove.setLevel(moveLevel);
				mList.jumpList();//<4.2>
				mList.addMove(thisMove);//<4.3>
				kingJumps(row, col, row - (2*(colourToPlay * -1)), col - (2*colMod), thisMove, thisMove.getMoveBoard());
			}
			colMod = colMod + 2;//<5>
		}
		moveLevel--;
	}

	private void jumpMoves(int fRow, int fCol, int row, int col, Move aMove, int [][] board){

		int colMod = -1; //the column modifier
		int count = 0;
		moveLevel++;

		while (colMod < 2){
			if ((row + colourToPlay > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && (row + (2*(colourToPlay * -1)) > -1) && (row + (2*(colourToPlay * -1)) < 8) && ((col + (2*colMod)) > -1) && ((col + (2*colMod)) < 8) && ( board [row + (colourToPlay * -1)][col + colMod] == (board[row][col] * -1) || board [row + (colourToPlay * -1)][col + colMod] == ((board[row][col]*2) * -1) ) &&  board [row + (2*(colourToPlay * -1))][col + (2*colMod)] == 0){//<2>
				thisMove = new Move(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), this.colourToPlay, true, board);//<2.1>
				thisMove.setLevel(moveLevel);
				jumpMoves(fRow, fCol, row + (2*(colourToPlay * -1)), col + (2*colMod), thisMove, thisMove.getMoveBoard());
				deleteList.add(aMove);

			}
			else if (count == 0){
				moveLevel--;
				thisMove = new Move(fRow, fCol, aMove.getToRow(), aMove.getToCol(), this.colourToPlay, true, aMove.getMoveBoard());
				thisMove.setMoveBoard(aMove.getMoveBoard());
				thisMove.setLevel(moveLevel);
				if (moveLevel > maxMoveLevel){
					maxMoveLevel = moveLevel;}
				if (mList.contains(fRow, fCol, aMove.getToRow(), aMove.getToCol(), aMove.getMoveBoard()) == true){
					count++;}
				else{
					mList.addMove(thisMove);
					count++;}
				moveLevel++;

			}
			else;
			colMod = colMod + 2;//<3>
		}

		moveLevel--;
	}

	private void kingJumps(int fRow, int fCol, int row, int col, Move aMove, int [][] board){

		int colMod = -1; //the column modifier
		int count = 0;
		moveLevel++;

		while (colMod < 2){
			if ((row + colourToPlay > -1) && (row + (colourToPlay * -1) < 8) && (col + colMod > -1) && (col + colMod < 8) && (row + (2*(colourToPlay * -1)) > -1) && (row + (2*(colourToPlay * -1)) < 8) && ((col + (2*colMod)) > -1) && ((col + (2*colMod)) < 8) && ( board [row + (colourToPlay * -1)][col + colMod] == (board[row][col] * -1) || board [row + (colourToPlay * -1)][col + colMod] == ((board[row][col]/2) * -1) ) &&  board [row + (2*(colourToPlay * -1))][col + (2*colMod)] == 0){//<2>
				thisMove = new Move(row, col, row + (2*(colourToPlay * -1)), col + (2*colMod), this.colourToPlay, true, board);//<2.1>
				thisMove.setLevel(moveLevel);
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				kingJumps(fRow, fCol, row + (2*(colourToPlay * -1)), col + (2*colMod), thisMove, thisMove.getMoveBoard());
			}
			else if (count == 0){
				moveLevel--;
				thisMove = new Move(fRow, fCol, aMove.getToRow(), aMove.getToCol(), this.colourToPlay, true, aMove.getMoveBoard());
				thisMove.setMoveBoard(aMove.getMoveBoard());
				thisMove.setLevel(moveLevel);
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				if (mList.contains(fRow, fCol, aMove.getToRow(), aMove.getToCol(), aMove.getMoveBoard()) == true){
					count++;}
				else{
					mList.addMove(thisMove);
					count++;}
				moveLevel++;


			}
			else;
			colMod = colMod + 2;//<3>
		}

		colMod = -1;

		while (colMod < 2){
			if ((row - (colourToPlay*-1) > -1) && (row - (colourToPlay*-1) < 8) && (col - colMod > -1) && (col - colMod < 8) && (row - (2*(colourToPlay * -1)) > -1) && (row - (2*(colourToPlay * -1)) < 8) && ((col - (2*colMod)) > -1) && ((col - (2*colMod)) < 8) && ( board [row - (colourToPlay * -1)][col - colMod] == (board[row][col] * -1) || board [row - (colourToPlay * -1)][col - colMod] == ((board[row][col]/2) * -1) ) &&  board [row - (2*(colourToPlay*-1))][col - (2*colMod)] == 0){//<4>
				thisMove = new Move(row, col, row - (2*(colourToPlay * -1)), col - (2*colMod), this.colourToPlay, true, board);//<4.1>
				thisMove.setLevel(moveLevel);
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				kingJumps(fRow, fCol, row - (2*(colourToPlay * -1)), col - (2*colMod), thisMove, thisMove.getMoveBoard());
			}
			else if (count == 0){
				moveLevel--;
				thisMove = new Move(fRow, fCol, aMove.getToRow(), aMove.getToCol(), this.colourToPlay, true, aMove.getMoveBoard());
				thisMove.setMoveBoard(aMove.getMoveBoard());
				thisMove.setLevel(moveLevel);
				if (moveLevel > maxMoveLevel)
					maxMoveLevel = moveLevel;
				if (mList.contains(fRow, fCol, aMove.getToRow(), aMove.getToCol(), aMove.getMoveBoard()) == true){
					count++;}
				else{
					mList.addMove(thisMove);
					count++;}
				moveLevel++;

			}
			else;
			colMod = colMod + 2;//<5>
		}
		moveLevel--;
	}

	//generates all possible moves by either the white or black side
	public void genAllMoves(){

		int basic = 0, king = 0;//keeps a record of the value of a piece for black or white
		deleteList = new ArrayList<Move>();

		if (colourToPlay == 1){
			basic = 2; king = 4;
		}
		else{
			basic = -2; king = -4;
		}

		for (int row = 0; row < EDGE_LENGTH; row++){
			for (int col = 0; col < EDGE_LENGTH; col++){
				if (board [row][col] == basic || board [row][col] == king)
					moveGenerator(row, col, board, board [row][col]);
			}
		}

		for (int i = 0; i < deleteList.size(); i++){

			mList.containsAndDeletes(deleteList.get(i).getFromRow(), deleteList.get(i).getFromCol(), deleteList.get(i).getToRow(), deleteList.get(i).getToCol(), deleteList.get(i).getMoveBoard());

		}

		for (int i = 0; i < mList.getSize(); i++){
			if (mList.getMove(i).getLevel() == maxMoveLevel){//Keep move in list
				//System.out.println(mList.getMove(i));
			}
			else
				mList.remove(i);
		}
	}

	public Move[] genMoveArray(){

		ArrayList<Move> keepList = new ArrayList<Move>();



		for (int i = 0; i < mList.getSize(); i++){
			if (mList.getMove(i).getLevel() == maxMoveLevel){
				keepList.add(mList.getMove(i));
			}
		}

		Move [] moveArray = new Move [keepList.size()];

		for (int i = 0; i < keepList.size(); i++){
			moveArray[i] = keepList.get(i);
		}

		this.clearMlist();

		mList.setAll(moveArray);

		return moveArray;

	} 

	public Move moveSelector(int pc){

		Move theMove = null;

		Search minimax = new Search(searchDepth);
		history.setMoveBoard(board);
		history.setHistory(true);

		theMove = Search.minimax(history, 0, pc);

		if (theMove.isHistory())
			theMove = null;	

		return theMove;

	}

	public boolean setPiecesLost(int colour, int lost, int [][] board){

		int bk = 0, b = 0, wk = 0, w = 0;

		for (int row = 0; row < EDGE_LENGTH; row++){
			for (int col = 0; col < EDGE_LENGTH; col++){
				if (board[row][col] == 2)
					w++;
				else if (board[row][col] == 4)
					wk++;
				else if (board[row][col] == -2)
					b++;
				else if (board[row][col] == -4)
					bk++;

			}
		}

		if (lost > 0)
			turnsAdvantage = 0;
		else
			turnsAdvantage++;

		if (bk == blackKings && b == blackPieces && wk == whiteKings && w == whitePieces ){

			if (turnsAdvantage == 50){
				JOptionPane.showMessageDialog(null , "Game drawn. 50 moves without change in advantage.","Could be worse.", JOptionPane.INFORMATION_MESSAGE);
				hasNoWinner = false;
				draw = true;
				return false;
			}
			else;
		}
		else if (lost == 0){
			turnsAdvantage = 0;
			blackKings = bk;
			blackPieces = b;
			whiteKings = wk;
			whitePieces = w;
		}
		else{
			blackKings = bk;
			blackPieces = b;
			whiteKings = wk;
			whitePieces = w;
		}

		if (whitePieces == 0 && whiteKings == 0 && playerColour == 1){
			JOptionPane.showMessageDialog(null , "Bad luck. You have lost the game with " + blackPieces + " enemy pieces and " + blackKings +  " enemy kings left.\nPlease select new game from the menu to start again.","Looser :(", JOptionPane.INFORMATION_MESSAGE);
			hasNoWinner = false;
			return false;
		}
		//indicates player has lost
		else if (blackPieces == 0 && blackKings == 0 && playerColour == -1){
			JOptionPane.showMessageDialog(null , "Bad luck. You have lost the game with " + whitePieces + " enemy pieces and " + whiteKings +  " enemy kings left.\nPlease select new game from the menu to start again.","Looser :(", JOptionPane.INFORMATION_MESSAGE);
			hasNoWinner = false;
			return false;
		}
		//indicates player has lost
		else if (whitePieces == 0 && whiteKings == 0 && playerColour == -1){
			JOptionPane.showMessageDialog(null , "Congratulations! You have won the game with " + blackPieces + " pieces and " + blackKings + " kings left.\nPlease select new game from the menu to start again.","Winner :)", JOptionPane.INFORMATION_MESSAGE);
			hasNoWinner = false;
			return false;
		}
		//indicates player has won
		else if (blackPieces == 0 && blackKings == 0 && playerColour == 1){
			JOptionPane.showMessageDialog(null , "Congratulations! You have won the game with " + whitePieces + " pieces and " + whiteKings + " kings left.\nPlease select new game from the menu to start again.","Winner :)", JOptionPane.INFORMATION_MESSAGE);
			hasNoWinner = false;
			return false;
		}
		return true;
	}

	public boolean playerMove(int[][] replacement, int z){

		if (replacement != null){
			board = replacement;
			if (z == 1)
				colourToPlay *= -1;
			return true;
		}
		else{
			return false;
		}
	}

	public boolean compMove(int[][] replacement){

		if (replacement != null){
			board = replacement;
			return true;
		}
		else{
			return false;
		}
	}

	public String getBoardStream(){

		String retStr = "";

		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board.length; j++){

				retStr +=(board [i][j]) + " ";
			}
		}
		retStr += playerColour + " ";
		retStr += colourToPlay;

		return retStr;
	}	
}
