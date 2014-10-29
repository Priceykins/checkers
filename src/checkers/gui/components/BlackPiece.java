package checkers.gui.components;
import java.awt.*;

public class BlackPiece extends Piece {

	public BlackPiece(int a, int b, int c, int d, int e, int f) {
		super(a, b, c, d, e, f);
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillOval(x,y,width,height);
		g.setColor(Color.WHITE);
		g.drawOval(x,y,width,height);
		}

}