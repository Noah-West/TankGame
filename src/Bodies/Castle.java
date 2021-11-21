package Bodies;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import javax.swing.ImageIcon;

import mainGame.gCols;

public class Castle {
	private AffineTransform prev, tran;
	private double[] pos;
	private Area tranBounds;
	private static ImageIcon iBody = new ImageIcon(ClassLoader.getSystemResource("Castle.png"));                
	private int health = 500;
	private double scale = .6;
	private int w, h;
	public Castle(double x, double y) {
		pos = new double[] {x, y};
		w = iBody.getIconWidth();
		h = iBody.getIconHeight();
		tran = new AffineTransform();
		tran.translate(x, y);
		tran.scale(scale, scale);
		tranBounds = new Area(new Rectangle2D.Double(-w/2, -h/2, w, h)).createTransformedArea(tran);
	}
	
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
		g2d.setColor(Color.red);
		//g2d.draw(tranBounds);
		g2d.transform(tran);
		iBody.paintIcon(null, g2d, -w/2, -h/2);
		g2d.setColor(Color.red);
		g2d.fillRect((int)-100, 20, (int)(health/2.5), 15);
		g2d.setColor(Color.black);
		g2d.drawRect((int)-100, 20, (int)(health/2.5), 15);

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
