package Bodies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import mainGame.gCols;

public class ETank implements Enemy{
	
	protected AffineTransform prev;
	protected Area bounds, tranBounds;
	protected PObj pBody, pTurret;
	protected final double maxVel = 100;
	protected final double accConst = 3;
	protected long fireDel = 800;
	double[] pos, vel; // vel in form [mag, rad]
	private static ImageIcon iBody = new ImageIcon("Resource/eTankBody.png");                
	private static ImageIcon iTurret = new ImageIcon("Resource/eTankTurret.png");  
	protected int health;
	protected long lastFire;
	public ETank(double x, double y, double rad, boolean start) {
		this.health = 100;
		pos = new double[] {x, y};
		vel = new double[] {30, rad};
		pBody = new PObj(pos, vel, start);
		pTurret = new PObj(pos, vel, start);
		pBody.rPos(vel[1]);
		pTurret.rPos(vel[1]);
		pBody.scale(.5);
		pTurret.scale(.5);
		bounds = new Area(new Rectangle(-41, -36, 82, 72));
		tranBounds = bounds;
		lastFire = System.currentTimeMillis();
	}
	private void fire(ArrayList<Bullet> shots) {
		if(System.currentTimeMillis()>lastFire+fireDel) {
			lastFire = System.currentTimeMillis();
			shots.add(new Bullet(pTurret.pos()[0]+25*Math.cos(pTurret.rPos()),pTurret.pos()[1]+25*Math.sin(pTurret.rPos()), pTurret.rPos(), 2, true));
		}
	}
	private void rotTurret(double rad) {
		pTurret.rPos(pTurret.rPos()+rad);
	}
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
	//health bar
		g2d.setColor(gCols.health);
		g2d.fillRect((int)pBody.pos[0]-25,(int)pBody.pos[1]-45,health/2,5);
	//tank sprites
		g2d.transform(pBody.trans());
		iBody.paintIcon(null, g2d, -41, -36);
		g2d.setTransform(pTurret.trans());
		iTurret.paintIcon(null, g2d, -23,-20);
		g2d.setTransform(prev);
	}
	public boolean takeDamage(int damage) {
		health -= damage;
		return health < 0;
	}
	public void tStep(ArrayList<Bullet> shots) {
		pBody.tStep();
		pTurret.tStep();
		tranBounds = bounds.createTransformedArea(pBody.trans());
		fire(shots);
	}
	public void start() {
		pBody.start();
		pTurret.start();
	}
	public Area tightBounds() {
		return tranBounds;
	}
	public Rectangle rectBounds() {
		return tranBounds.getBounds();
	}
	public void pos(double[] pos) {
		this.pos = pos;
		pBody.pos(pos);
		pTurret.pos(pos);
	}
	/**
	 * @return the pos
	 */
	public double[] pos() {
		return pos;
	}
	/**
	 * @return the vel
	 */
	public double[] vel() {
		return vel;
	}
}
