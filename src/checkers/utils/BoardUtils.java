package checkers.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import checkers.core.Board;

public class BoardUtils {


	//where the initialisation is located
	private final static String NEW_GAME_FILE = "data//checkers Input.txt";

	//where save files are located
	private final static String SAVE_GAME_DIR = "saveGames//";

	private final static String FILE_EXTENSION = ".txt";
	
	private final static int EDGE_LENGTH = 8; 
	
	private int playerColour, colourToPlay, blackPieces, whitePieces, turnsAdvantage, searchDepth;
	private int [][] board;
	private boolean newGame;

	public BoardUtils(int [][] b){
		
		playerColour = 0;
		colourToPlay = 0;
		blackPieces = 0;
		whitePieces = 0;
		turnsAdvantage = 0;
		searchDepth = 0;
		board = b;
		
	}
	
	public int getPlayerColour(){
		return playerColour;
	}
	
	public int getColourToPlay(){
		return colourToPlay;
	}
	
	public int getBlackPieces(){
		return blackPieces;
	}
	
	public int getWhitePieces(){
		return whitePieces;
	}
	
	public int getTurnsAdvantage(){
		return turnsAdvantage;
	}
	
	public int getSearchDepth(){
		return searchDepth;
	}

	public int [][] newBoard(){
		
		newGame = true;
		
		this.fileReader(NEW_GAME_FILE);
		
		return board;
		}

	public int [][] loadBoard(String fileName){
		
		newGame = false;
		
		String fileToOpen = SAVE_GAME_DIR.concat(fileName).concat(FILE_EXTENSION);
		
		this.fileReader(fileToOpen);
		
		return board;
		}

	public boolean saveBoard(String fileName, String boardStream, int bp, int wp, int ta, int sd){
		
		this.fileWriter(fileName, boardStream, bp, wp, ta, sd);
		
		return true;
		
	}


	private boolean fileReader(String fileName){
		int counter = 1;

		try{
			FileReader reader = null;
			Scanner in = null;
			try{
				//reads in from input file
				reader = new FileReader(fileName); 
				in = new Scanner(reader);
				while (in.hasNextInt() && counter < 64){
					for (int row = 0; row < EDGE_LENGTH; row++){
						for (int col = 0; col < EDGE_LENGTH; col++){
							board[row][col] = in.nextInt();
							counter++;
						}
					}
				}
				if (in.hasNext() && !newGame)
					playerColour = in.nextInt();

				if (in.hasNext()&& !newGame)
					colourToPlay = in.nextInt();

				if (in.hasNext()&& !newGame)
					blackPieces = in.nextInt();

				if (in.hasNext()&& !newGame)
					whitePieces = in.nextInt();

				if (in.hasNext()&& !newGame)
					turnsAdvantage = in.nextInt();

				if (in.hasNext()&& !newGame)
					searchDepth = in.nextInt();
			}
			finally{
				if (reader != null){
					reader.close();
					in.close();
				}
			}
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null , "File " + fileName + " not Found. Please ensure file is in correct\ndirectory and try again.","File Not Found", JOptionPane.WARNING_MESSAGE);
			return false;
		}


		return true;
	}

	private boolean fileWriter(String fileName, String boardStream, int bp, int wp, int ta, int sd){
		
		String saveName = SAVE_GAME_DIR.concat(fileName).concat(FILE_EXTENSION);
		String saveString = boardStream;
		saveString.concat(" " + bp).concat(" " + wp).concat(" " + ta).concat(" " + sd).concat("\n");

		try{
			FileWriter writer = null;
			try{
				writer = new FileWriter(saveName);
				writer.write(saveString);

			}
			finally{
				if (writer != null)
					writer.close();
			}
		}
		catch(IOException e){
		}
		
		return true;
		}

}
