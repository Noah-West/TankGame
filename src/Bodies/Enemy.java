package Bodies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;

public interface Enemy {
	public void draw(Graphics2D g2d);
	public void tStep(ArrayList<Bullet> shots);
	public void start();
	public Area tightBounds();
	public Rectangle rectBounds();
	public void pos(double[] pos);
	/**
	 * @return the pos
	 */
	public double[] pos();
	/**
	 * @return the vel
	 */
	public double[] vel();
	public boolean takeDamage(int damage);
	
}
