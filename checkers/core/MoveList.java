package checkers.core;
import java.util.*;

import checkers.evaluator.V7.*;

/**
 * The Class holds a moveList for a particular square on the board
 * A move generator will be used to create the moves added to this list
 * 
 * @author Chris Price
 */
public class MoveList {

	/** The list of moves */
	private ArrayList<Move> moveList;

	/** Declares if list is a list of jumps */
	private boolean isJumpList = false;

	/**
	 * Instantiates a new move list
	 */
	public MoveList(){

		moveList = new ArrayList<Move>();
	}

	/**
	 * Adds a move If the moveList is a list of jumps, no
	 * non jump moves can be added If a move that is a jump
	 * is added to a moveList that is not a jump list, the
	 * jumpList method is ran on the moveList to turn it into
	 * a jump list
	 * 
	 * @param aMove one possible move that can be made
	 */
	public void addMove(Move aMove){

		if (isJumpList){
			if (aMove.isJump())
				moveList.add(aMove);
		}
		else{
			// if the move to be added is a jump, and the list is not a jump list already
			// the list is turned into a jump list
			if (aMove.isJump())
				jumpList();

			moveList.add(aMove);
		}

	}

	/**
	 * Turns the moveList into a jumpList by removing all non jump moves
	 *
	 *@param iterator this parameter keeps track of which list element the iteration is at, for the purposes of removal
	 *		 of the correct non jump elements
	 */
	public void jumpList(){

		int iterator = 0;
		if (isJumpList == false){
			isJumpList = true;

			while (iterator < moveList.size()){

				Move aMove = moveList.get(iterator);

				if (aMove.isJump())
					iterator++;
				else
					moveList.remove(iterator);

			}
		}
		else;
	}

	//returns the size of the move list
	public int getSize(){
		return moveList.size();
	}

	public Move getMove(int i){

		return moveList.get(i);
	}

	public void reset(){

		isJumpList = false;
		moveList.clear();
	}

	public boolean isJump(){return isJumpList;}

	public boolean remove(Move a){return moveList.remove(a);}

	public void remove(int i){moveList.remove(i);}

	public boolean contains(int fRow, int fCol, int toRow, int toCol, int [][] a) {

		for (int i = 0; i < moveList.size(); i++){

			if (moveList.get(i).getFromRow() == fRow && moveList.get(i).getFromCol() == fCol && moveList.get(i).getToRow() == toRow && moveList.get(i).getToCol() == toCol){

				for (int p = 0; p < 8; p++){
					for (int q = 0; q < 8; q++){
						if (a [p][q] == moveList.get(i).getResultSq(p, q));
						else return false;

					}
				}
				return true;
			}		
		}

		return false;
	}

	public boolean containsAndDeletes(int fRow, int fCol, int toRow, int toCol, int [][] a) {

		for (int i = 0; i < moveList.size(); i++){

			if (moveList.get(i).getFromRow() == fRow && moveList.get(i).getFromCol() == fCol && moveList.get(i).getToRow() == toRow && moveList.get(i).getToCol() == toCol){

				for (int p = 0; p < 8; p++){
					for (int q = 0; q < 8; q++){
						if (a [p][q] == moveList.get(i).getResultSq(p, q));
						else return false;

					}
				}
				moveList.remove(i);
				return true;
			}		
		}

		return false;
	}

	public void setAll(Move[] a){

		moveList.clear();

		for (int i = 0; i < a.length; i++)
			moveList.add(a[i]);
	}

	public ArrayList<Move> getAll(){return moveList;}

	public void setList(ArrayList<Move> list){moveList = list;}
}
