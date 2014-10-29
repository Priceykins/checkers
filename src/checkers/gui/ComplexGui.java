package checkers.gui;
import javax.swing.*;

import checkers.core.*;
import checkers.gui.components.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class ComplexGui extends JFrame implements ActionListener {

	private static final int GUI_SIDE_LENGTH = 800, GUI_TOP_LENGTH = 700;

	//General GUI components
	private JPanel south;
	private static final String SOFTWARE_VERSION = "Pinkie Pie";
	private JButton makeMove;
	private JComboBox<String> moveFrom;
	private JLabel moveF, playerToMove, compMove;
	private GridLayout enterMoveLayout = new GridLayout(3, 2, 5, 5);
	private JTextPane boardArea;
	private GraphicBoard grBrd = null;
	private Move chosenMove = null;
	private String helpString = "";
	private String fileType = ".txt";

	//Menu Bar Components
	private JMenuBar mBar;
	private JMenu fileMenu, helpMenu;
	private JMenuItem newG, save, load, exit, help, prntBoard, compVcomp;

	int frRow, frCol, toRow, toCol;

	//board instance held by GUI
	private Board theBoard;

	//array of squares used to create board background
	private Square [][] squares;

	private Piece [][] pieces;

	private int h, w;
	//used to map square numbers to board array row and column coordinates
	private int[][] mappingArray = {{0, 1, 0, 2, 0, 3, 0, 4}, 
			{5, 0, 6, 0, 7, 0, 8, 0},
			{0, 9, 0, 10, 0, 11, 0, 12},
			{13, 0, 14, 0, 15, 0, 16, 0},
			{0, 17, 0, 18, 0, 19, 0, 20},
			{21, 0, 22, 0, 23, 0, 24, 0},
			{0, 25, 0, 26, 0, 27, 0, 28},
			{29, 0, 30, 0, 31, 0, 32, 0}};


	//constructor for GUI
	public ComplexGui(){

		this.setTitle("Checkers version: " + SOFTWARE_VERSION);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(GUI_TOP_LENGTH,GUI_SIDE_LENGTH);
		this.layoutMenuBar();
		this.layoutBoardArea();	
		this.layoutEnterMoveArea();
		this.repaint();
		//this.loadHelp();
		theBoard = new Board();
		//repaint ensures all GUI components display correctly on start
		this.repaint(); 
		this.paintComponents(getGraphics());


		this.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){

			public void ancestorMoved(HierarchyEvent e) {
				System.out.println(e);		}

			public void ancestorResized(HierarchyEvent e) {
				calGraphicBoardArray();
				calGraphicPieceArray(theBoard.getBoard());
				if (grBrd != null)
					grBrd.setAlignmentY((float)w/2);
			}	
		});

	}

	public void layoutBoardArea(){

		boardArea = new JTextPane();
		boardArea.setEditable(false);
		boardArea.setFont(new Font("Courier", Font.PLAIN, 14));
		boardArea.setText("To start a new game, select new game from the file menu.\nSee help for any other questions.");
		this.add(boardArea, BorderLayout.CENTER);
		this.repaint();
	}

	public void calGraphicBoardArray(){

		h = this.getHeight();
		w = this.getWidth();
		int hp = h/11;
		int sqNo = 1;


		squares = new Square [8][8];

		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){
				//if row number is even
				if (row%2 == 0){
					//if column number is even
					if (col%2 == 0){
						squares[row][col] = new WhiteSquare((col * hp), (row * hp), hp, hp, row, col);
					}
					//column is odd
					else{
						squares[row][col] = new BlackSquare((col * hp), (row * hp), hp, hp, row, col, sqNo);
						sqNo++;
					}	
				}
				//row number is odd
				else{
					//if column number is even
					if (col%2 == 0){
						squares[row][col] = new BlackSquare((col * hp), (row * hp), hp, hp, row, col, sqNo);
					}
					//column is odd
					else{
						squares[row][col] = new WhiteSquare((col * hp), (row * hp), hp, hp, row, col);
						sqNo++;
					}	
				}
			}
		}
	}

	public void calGraphicPieceArray(int [][] a){

		int hp = h/11;

		pieces = new Piece [8][8];

		for (int row = 0; row < 8; row++){
			for (int col = 0; col < 8; col++){

				if (a [row][col] == 0 || a [row][col] == -1){
					pieces [row][col] = null;
				}
				else if (a [row][col] == 2){
					pieces [row][col] = new WhitePiece((col * hp), (row * hp), hp, hp, row, col);
				}
				else if (a [row][col] == 4){
					pieces [row][col] = new WhiteKing((col * hp), (row * hp), hp, hp, row, col);
				}
				else if (a [row][col] == -2){
					pieces [row][col] = new BlackPiece((col * hp), (row * hp), hp, hp, row, col);
				}
				else if (a [row][col] == -4){
					pieces [row][col] = new BlackKing((col * hp), (row * hp), hp, hp, row, col);
				}

			}
		}
	}

	public void addGB(){

		grBrd = new GraphicBoard();
		if (boardArea != null){
			this.remove(boardArea);
		}
		boardArea = null;
		add(grBrd, BorderLayout.CENTER);
		this.repaint();
	}

	//lays out the menu bar at the top of the window
	public void layoutMenuBar(){

		mBar = new JMenuBar();
		mBar.setVisible(true);

		fileMenu = new JMenu("File");
		mBar.add(fileMenu);

		newG = new JMenuItem("New Game");
		fileMenu.add(newG);
		newG.addActionListener(this);

		save = new JMenuItem("Save Game");
		fileMenu.add(save);
		save.addActionListener(this);

		load = new JMenuItem("Load Game");
		fileMenu.add(load);
		load.addActionListener(this);

		exit = new JMenuItem("Exit Program");
		fileMenu.add(exit);
		exit.addActionListener(this);

		helpMenu = new JMenu("Help");
		mBar.add(helpMenu);

		help = new JMenuItem("Help");
		helpMenu.add(help);
		help.addActionListener(this);

		setJMenuBar(mBar);
		this.repaint();
	}

	//lays out the bottom section of the gui which allows a user to select which move to make
	public void layoutEnterMoveArea(){




		south = new JPanel();
		south.setLayout(enterMoveLayout);

		compMove = new JLabel();
		compMove.setText("Opponents Move: ");
		south.add(compMove);

		JPanel filler = new JPanel();
		south.add(filler);

		moveF = new JLabel();
		moveF.setText("Move");
		south.add(moveF);

		playerToMove = new JLabel();
		playerToMove.setText("White to move");
		south.add(playerToMove);

		moveFrom = new JComboBox<String>();
		south.add(moveFrom);

		makeMove = new JButton();
		makeMove.setText("Make Move");
		makeMove.addActionListener(this);
		south.add(makeMove);

		add(south, BorderLayout.SOUTH);
		this.repaint();

	}


	public void loadHelp(){

		String fileToOpen = "data/helpDoc.txt";

		try{
			FileReader reader = null;
			Scanner in = null;
			try{
				//reads in from input file
				reader = new FileReader(fileToOpen);
				in = new Scanner(reader);
				while (in.hasNextLine()){
					helpString.concat(in.nextLine() + "\n");
				}

			}
			finally{
				if (reader != null){
					reader.close();
				    in.close();
				}
			}
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(null , "File " + fileToOpen + " not Found. Please ensure file is in correct\ndirectory and try again.","File Not Found", JOptionPane.WARNING_MESSAGE);
		}
	}

	//action listener for the GUI
	public void actionPerformed(ActionEvent e) {


		if (e.getSource() == makeMove){

			int fromInt = 0;
			int toInt = 0;

			String frCoords = (String) moveFrom.getSelectedItem();

			if (frCoords.length() == 22){
				String from = frCoords.substring(14, 16).replaceAll(" ", "");
				String to = frCoords.substring(20, 22).replaceAll(" ", "");
				fromInt = Integer.parseInt(from);
				toInt = Integer.parseInt(to);}
			else if (frCoords.length() == 21){
				String from = frCoords.substring(14, 16).replaceAll(" ", "");
				String to = frCoords.substring(19, 21).replaceAll(" ", "");
				fromInt = Integer.parseInt(from);
				toInt = Integer.parseInt(to);}
			else if (frCoords.length() == 20){
				String from = frCoords.substring(14, 16).replaceAll(" ", "");
				String to = frCoords.substring(18, 20).replaceAll(" ", "");
				fromInt = Integer.parseInt(from);
				toInt = Integer.parseInt(to);}

			for (int row = 0; row < 8; row++){
				for (int col = 0; col < 8; col++){
					if (mappingArray[row][col] == fromInt){
						frRow = row;
						frCol = col;
					}
					else if (mappingArray[row][col] == toInt){
						toRow = row;
						toCol = col;
					}	
				}
			}
			makePlayerMove();
		}
		//must be menu bar choice
		else{
			JMenuItem choice = (JMenuItem) e.getSource();

			if (choice == newG){

				theBoard = new Board();

				theBoard.setColourToMove(-1);

				String [] choices = {"White", "Black"};

				String [] diffChoices = {"Easy", "Medium", "Hard"};

				String colourString = null;

				while (colourString == null){
					colourString = (String) JOptionPane.showInputDialog(null, "Choose which colour you want to play:", "Choose Colour", JOptionPane.INFORMATION_MESSAGE, null, choices, "Choose One");
				}

				String diffString = null;

				while (diffString == null){
					diffString = (String) JOptionPane.showInputDialog(null, "Choose which difficulty level you wish to play:", "Choose Difficulty", JOptionPane.INFORMATION_MESSAGE, null, diffChoices, "Choose One");
				}	

				if (colourString.equals("White")){
					theBoard.setPlayerColour(1);
				}
				else{
					theBoard.setPlayerColour(-1);
				}

				if (diffString.equals("Easy")){
					theBoard.setDifficulty(2);
				}
				else if (diffString.equals("Medium")){
					theBoard.setDifficulty(5);
				}
				else if (diffString.equals("Hard")){
					theBoard.setDifficulty(9);
				}

				theBoard.loadBoard(true, null);

				this.calGraphicBoardArray();
				this.calGraphicPieceArray(theBoard.getBoard());
				this.addGB();

				if (theBoard.getColourToMove() != theBoard.getPlayer()){
					compMove();
				}
				else{
					playerMove();
				}

			}
			else if (choice == load){

				theBoard = new Board();

				theBoard.clear();

				String fileName = null;

				while (fileName == null){
					fileName = JOptionPane.showInputDialog ( "Enter savegame name" );
				}

				if (fileName.substring((fileName.length()-fileType.length()), fileName.length()) != fileType ){
					fileName.concat(fileType);
				}


				theBoard.loadBoard(false, fileName);

				this.calGraphicBoardArray();
				this.calGraphicPieceArray(theBoard.getBoard());
				this.addGB();

				if (theBoard.getColourToMove() != theBoard.getPlayer()){
					compMove();
				}
				else{
					playerMove();
				}

			}
			else if (choice == save){

				String f = null;

				while (f == null){
					f = JOptionPane.showInputDialog ( "Enter file name you wish to save game with");
				}
				f.concat(fileType);
				theBoard.saveBoard(f);

			}
			else if (choice == exit){

				System.exit(0);

			}
			else if (choice == prntBoard){

				String board = theBoard.getBoardStream();
				JFrame display = new JFrame();
				JTextArea dis = new JTextArea();
				dis.setEditable(false);
				dis.setText(board);
				display.add(dis);
				display.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				display.pack();
				display.setVisible(true);
			}
			else if (choice == help){

				JFrame helpFrame = new JFrame();
				helpFrame.setTitle("Checkers Help");
				helpFrame.setLocation(this.getWidth(), 0);
				JTextArea helpText = new JTextArea();
				helpText.setEditable(false);
				helpText.setText(helpString);
				helpFrame.add(helpText);
				helpFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				helpFrame.pack();
				helpFrame.setVisible(true);
			}
			else if (choice == compVcomp){}
		}

	}

	public void playerMove(){

		theBoard.setMaxLevel();

		moveFrom.removeAllItems();

		if (theBoard.getColourToMove() == 1){//sets UI element which details which colour is to move
			playerToMove.setText("White to play");
		}
		else{
			playerToMove.setText("Black to play");
		}

		theBoard.genAllMoves();

		Move[] moveArray = theBoard.genMoveArray();

		String[] moveFromArray = new String[moveArray.length];
		String[] moveToArray = new String[moveArray.length];

		for (int i = 0; i < moveArray.length; i++){
			moveFromArray[i] = moveArray[i].getFromRow() + " " + moveArray[i].getFromCol();
			moveToArray[i] = moveArray[i].getToRow() + " " + moveArray[i].getToCol();
		}

		for (int i = 0; i < moveArray.length; i++){

			String move = "Move Piece at " + mappingArray[moveArray[i].getFromRow()][moveArray[i].getFromCol()] + " to " +  mappingArray[moveArray[i].getToRow()][moveArray[i].getToCol()];

			moveFrom.addItem(move);
		}
	}

	private boolean whiteToMove(boolean cont, Move ChosenMove){
		if (cont == false){
			this.calGraphicPieceArray(chosenMove.getMoveBoard());
			this.repaint();
			return true;
		}
		else
			return false;
	}

	private boolean blackToMove(boolean cont, Move chosenMove){
		if (cont == false){
			this.calGraphicPieceArray(chosenMove.getMoveBoard());
			this.repaint();
			return true;
		}
		else
			return false;
	}

	public void makePlayerMove(){

		System.out.println("MAKE PLAYER MOVE");

		Move chosenMove = null;
		Move[] moveArray = theBoard.genMoveArray();


		if (moveArray.length > 0){
			for (int i = 0; i < moveArray.length; i++){

				if (moveArray[i].getFromRow() == frRow && moveArray[i].getFromCol() == frCol && moveArray[i].getToRow() == toRow && moveArray[i].getToCol() == toCol){
					chosenMove = moveArray[i];

					theBoard.playerMove(chosenMove.getMoveBoard(), 0);

					if (theBoard.getColourToMove() == 1){
						//tells the board that "jumps" black pieces have been lost, cont == false && whiteToMove == true indicates the game is over
						boolean cont = theBoard.setPiecesLost(-1, chosenMove.piecesTaken(), chosenMove.getMoveBoard());
						if (this.whiteToMove(cont, chosenMove)){
							return;	
						}
						theBoard.setColourToMove(-1);
					}
					else{
						//tells the board that "jumps" white pieces have been lost, cont == false && blackToMove == true  indicates the game is over
						boolean cont = theBoard.setPiecesLost(1, chosenMove.piecesTaken(), chosenMove.getMoveBoard());
						if (this.blackToMove(cont, chosenMove)){
							return;	
						}
						theBoard.setColourToMove(1);
					}
				}
			}
		}
		else{
			theBoard.gameOver();
			JOptionPane.showMessageDialog(null , "You have lost by having no moves to make.\nPlease select new game from the menu to start again.","Results", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		calGraphicPieceArray(theBoard.getBoard());
		this.paintComponents(getGraphics());
		theBoard.clearMlist();

		compMove();
	}


	public void compMove(){

		theBoard.setMaxLevel();

		if (theBoard.hasNoWinner() == true){
			//sets UI element which details which colour is to move
			if (theBoard.getColourToMove() == 1){
				playerToMove.setText("White to play");
			}
			else{
				playerToMove.setText("Black to play");
			}
			this.paintComponents(getGraphics());

			chosenMove = theBoard.moveSelector(theBoard.getColourToMove());

			if (chosenMove != null){
				//tells the board that "jumps" black pieces have been lost, returning false indicates the game is over
				if (theBoard.getColourToMove() == 1){
					boolean cont = theBoard.setPiecesLost(-1, chosenMove.piecesTaken(), chosenMove.getMoveBoard());
					if (this.whiteToMove(cont, chosenMove)){
						return;	
					}
					theBoard.setColourToMove(-1);
				}
				//tells the board that "jumps" white pieces have been lost, returning false indicates the game is over
				else{
					boolean cont = theBoard.setPiecesLost(1, chosenMove.piecesTaken(), chosenMove.getMoveBoard());
					if (this.blackToMove(cont, chosenMove)){
						return;	
					}
					theBoard.setColourToMove(1);
				}

				compMove.setText("Opponents Last Move:     " + mappingArray[chosenMove.getFromRow()][chosenMove.getFromCol()] + " to " + mappingArray[chosenMove.getToRow()][chosenMove.getToCol()]);
				theBoard.compMove(chosenMove.getMoveBoard());
				theBoard.clearMlist();
			}
			else{
				theBoard.gameOver();
				JOptionPane.showMessageDialog(null , "You have won by causing your opponent to have no moves.\nPlease select new game from the menu to start again.","Results", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		calGraphicPieceArray(theBoard.getBoard());
		this.paintComponents(getGraphics());
		this.repaint();
		playerMove();
	}

	public void setChosenMove(Move c){
		chosenMove = c;
	}

	//Inner class
	//Allows access to gui specific parameters and collections
	//Generates a board area, for graphical viewing of the game

	public class GraphicBoard extends JPanel{

		public GraphicBoard(){

			setSize((h/11)*8, w );	
		}

		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;

			for (int row = 0; row < 8; row++){
				for (int col = 0; col < 8; col++){

					Square s = squares [row][col];
					s.draw(g2);
					if (pieces [row][col] != null){
						Piece p = pieces [row][col];
						p.draw(g2);
					}
				}
			}
			g2.setColor(Color.BLACK);
			g2.drawRect(0, 0, (h/11)*8, (h/11)*8);
		}
	}
}
