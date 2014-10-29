package checkers.gui.components;
import java.awt.*;

public abstract class Square {

	int x, y;
	int width, height;
	int row, col;
	
	public Square(int a, int b, int c, int d, int e, int f){
		
		x = a;
		y = b;
		width = c;
		height = d;
		row = e;
		col = f;
	}
	
	public void draw(Graphics2D g){}
	
	public int getRow(){return row;}
	public int getCol(){return col;}
	
	public boolean containsPoint(int x, int y) {

		//tests whether the shape contains point (x,y)
		//Used for determining whether or not the mouse has been clicked on this shape
		if (x >= this.x && x < this.x + width && y >= this.y && y < this.y + height){
			return true;}
		else{
			return false;}
	}
}
