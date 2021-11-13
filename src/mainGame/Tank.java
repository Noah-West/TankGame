package mainGame;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
public class Tank{// implements Drawable{
	private AffineTransform prev;
	private Area bounds, tranBounds;
	private PObj pBody, pTurret;
	private final double maxVel = 120;
	private final double accConst = 3;
	double[] pos, vel; // vel in form [mag, rad]
	static ImageIcon iBody = new ImageIcon("Resource/TankBody.png");
	static ImageIcon iTurret = new ImageIcon("Resource/TankTurret.png");
	public Tank(double x, double y, double mag, double rad, boolean start) {
		pos = new double[] {x, y};
		vel = new double[] {mag, rad};
		pBody = new PObj(pos, vel, start);
		pTurret = new PObj(pos, vel, start);
		pBody.rPos(vel[1]);
		pTurret.rPos(vel[1]);
		pBody.scale(.6);
		pTurret.scale(.6);
		bounds = new Area(new Rectangle(-41, -36, 82, 72));
		tranBounds = bounds;
	}
	public Bullet fire() {
		return new Bullet(pTurret.pos()[0]+25*Math.cos(pTurret.rPos()),pTurret.pos()[1]+25*Math.sin(pTurret.rPos()), pTurret.rPos(), 2, true);
	}
	public void rotate(double rad) {
		vel[1] += rad;
		pBody.rPos(vel[1]);
		rotTurret(rad);
	}
	public void rotTurret(double rad) {
		pTurret.rPos(pTurret.rPos()+rad);
	}
	public void addVel(double mag) {
		vel[0] += mag;
		vel[0] = vel[0] >= maxVel ? maxVel : vel[0];
		vel[0] = vel[0] <= -maxVel ? -maxVel : vel[0];
		pBody.vel(vel);
		pTurret.vel(vel);
	}
	public void acc(boolean dir) {
		if(dir) addVel(3*accConst);
		else addVel(-3*accConst);
	}
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
		g2d.transform(pBody.trans());
		iBody.paintIcon(null, g2d, -41, -36);
		g2d.setColor(Color.red);
		//g2d.drawString(String.format("%.3f",vel[1]), 0, 50);
		g2d.setTransform(pTurret.trans());
		iTurret.paintIcon(null, g2d, -23,-20);
		g2d.setTransform(prev);
		//g2d.draw(tranBounds);
	}
	public void tStep() {
		addVel(vel[0]>0?-2*accConst:(vel[0]<0?2*accConst:0));
		pBody.tStep();
		pTurret.tStep();
		tranBounds = bounds.createTransformedArea(pBody.trans());
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
