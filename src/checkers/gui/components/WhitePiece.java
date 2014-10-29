package checkers.gui.components;
import java.awt.*;

public class WhitePiece extends Piece {

	public WhitePiece(int a, int b, int c, int d, int e, int f) {
		super(a, b, c, d, e, f);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillOval(x,y,width,height);
		g.setColor(Color.BLACK);
		g.drawOval(x,y,width,height);
		}

}
