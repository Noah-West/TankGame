package Bodies;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

public class Castle {
	private AffineTransform prev;
	private double[] pos;
	private Area tranBounds;
	private static ImageIcon iBody = new ImageIcon("Resource/eJeep.png");                
	private int health = 1000;
	private double scale = 1;
	public Castle(double x, double y) {
		pos = new double[] {x, y};
		tranBounds = new Area(new Rectangle2D.Double(x-41, y-36, x+82, y+72));
	}
	
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
	
		g2d.translate(pos[0], pos[1]);
		g2d.scale(scale, scale);
		iBody.paintIcon(null, g2d, -41, -36);
		g2d.setTransform(prev);
	}
	public boolean takeDamage(int damage) {
		health -= damage;
		return health <= 0;
	}
	public Area tightBounds() {
		return tranBounds;
	}
	public Rectangle2D rectBounds() {
		return tranBounds.getBounds();
	}
	
	public void pos(double[] pos) {
		pos = pos.clone();
	}
	/**
	 * @return the pos
	 */
	public double[] pos() {
		return pos;
	}
}
