package checkers.gui.components;
import java.awt.*;

public class BlackSquare extends Square {
	
	String sqNo;
	
	public BlackSquare(int a, int b, int c, int d, int e, int f, int g){
		
		super(a, b, c, d, e, f);
		
		sqNo = "" + g;
	}

public void draw(Graphics2D g) {
		
		g.setFont(new Font("Courier", Font.PLAIN, height/4));
		g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(Color.YELLOW);
        g.drawString(sqNo, x + 2, y + height/4);
	}
}
