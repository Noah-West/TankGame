package mainGame;

import javax.swing.ImageIcon;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
public class Tank{// implements Drawable{
	private AffineTransform prev;
	private Area bounds;
	private PObj pBody, pTurret;
	private final double maxVel = 150;
	private final double accConst = 3;
	double[] pos, vel; // vel in form [mag, rad]
	static ImageIcon Ibody = new ImageIcon("Resource/TankBody.png");
	static ImageIcon Iturret = new ImageIcon("Resource/TankTurret.png");
	public Tank(double x, double y, double mag, double rad, boolean start) {
		pos = new double[] {x, y};
		vel = new double[] {mag, rad};
		pBody = new PObj(pos, vel, start);
		pTurret = new PObj(pos, vel, start);
		pBody.rPos(vel[1]);
		pTurret.rPos(vel[1]);
		pBody.scale(.6);
		pTurret.scale(.6);

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
		Ibody.paintIcon(null, g2d, -41, -36);
		g2d.setColor(Color.red);
		g2d.drawString(String.format("%.0f",vel[0]), 0, 50);
		g2d.setTransform(pTurret.trans());
		Iturret.paintIcon(null, g2d, -23,-20);
		g2d.setTransform(prev);
	}
	public void tStep() {
		addVel(vel[0]>0?-2*accConst:(vel[0]<0?2*accConst:0));
		pBody.tStep();
		pTurret.tStep();
	}
	public void start() {
		pBody.start();
		pTurret.start();
	}
}
