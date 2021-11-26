package Bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

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
		
		//making castle bounding box
		AffineTransform tmpTrans = new AffineTransform();
		tmpTrans.translate(-w/2, -h/2);
		Area temp = new Area();
		temp.add(new Area(new Rectangle(61, 68,727, 183)));
		temp.add(new Area(new Rectangle(290, 13,270, 75)));
		temp.add(new Area(new Ellipse2D.Double(0, 6, 143, 146)));
		temp.add(new Area(new Ellipse2D.Double(710, 6, 143, 146)));
		temp.add(new Area(new Ellipse2D.Double(263, 0, 57, 57)));
		temp.add(new Area(new Ellipse2D.Double(530, 0, 57, 57)));
		temp.transform(tmpTrans);
		tranBounds = temp.createTransformedArea(tran);
	}
	
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
		g2d.setColor(Color.red);
		//g2d.draw(tranBounds);
		g2d.transform(tran);
		iBody.paintIcon(null, g2d, -w/2, -h/2);
		g2d.setColor(Color.red);
		g2d.fillRect((int)-100, 20, (int)(health/2.5), 15);
		g2d.drawRect((int)-100, 20, (int)(500/2.5), 15);

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
