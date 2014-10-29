package checkers.gui.components;
import java.awt.*;

public class WhiteSquare extends Square {

	int x, y;
	int width, height;
	
	public WhiteSquare(int a, int b, int c, int d, int e, int f){
		
		super(a, b, c, d, e, f);
	}

public void draw(Graphics2D g) {
		
		g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
	}
}
