package checkers.gui.components;
import java.awt.*;

public abstract class Piece {

	int x, y;
	int width, height;
	int row, col;
	
	public Piece(int a, int b, int c, int d, int e, int f){
		
		x = a + (d/5);
		y = b + (d/5);
		width = c - (d/5);
		height = d - (d/5);
		row = e;
		col = f;
	}
	
	public void draw(Graphics2D g){}
	
	public boolean containsPoint(int x, int y) {
		// Check whether point (x,y) is inside the circle, using the
		// mathematical equation of an ellipse
		double rx = width/2.0;   // horizontal radius of ellipse
		double ry = height/2.0;  // vertical radius of ellipse 
		double cx = this.x + rx;   // x-coord of center of ellipse
		double cy = this.y + ry;    // y-coord of center of ellipse
		if ( (ry*(x-cx))*(ry*(x-cx)) + (rx*(y-cy))*(rx*(y-cy)) <= rx*rx*ry*ry )
			return true;
		else
			return false;
	}
	
	public void setXY(int a, int b){
		
		x = a;
		y = b;
	}
}
