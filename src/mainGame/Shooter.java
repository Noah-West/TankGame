package mainGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

public interface Shooter {
	public Bullet fire();
	public void rotate(double rad);
	public void rotTurret(double rad);
	public void addVel(double mag);
	public void acc(boolean dir);
	public void draw(Graphics2D g2d);
	public void tStep();
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
