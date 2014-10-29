package checkers.gui.components;
import java.awt.*;

public class WhiteKing extends Piece {

	public WhiteKing(int a, int b, int c, int d, int e, int f) {
		super(a, b, c, d, e, f);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawOval(x, y, width, height);
		g.setColor(Color.YELLOW);
		g.fillOval(x + (width/4), y + (height/4), width/2, height/2);
		}

}