package Bodies;
import javax.swing.ImageIcon;

import mainGame.GameApp;
import mainGame.gCols;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;

public class Tank {
	private final double accConst = 1000d/GameApp.fps;
	private double accRate = 0;
	protected AffineTransform prev;
	protected Area bounds, tranBounds;
	public PObj pBody;
	//protected final double maxVel = 90;
	protected PObj pTurret;
	
	protected long fireDel = 400;
	double[] pos, vel; // vel in form [mag, rad]
	private static ImageIcon iBody = new ImageIcon(ClassLoader.getSystemResource("plrTankBody.png"));                
	private static ImageIcon iTurret = new ImageIcon(ClassLoader.getSystemResource("plrTankTurret.png"));  
	protected int health;
	protected long lastFire;
	public Tank(double x, double y, double mag, double rad, int health, boolean start) {
		this.health = health;
		          
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
		lastFire = System.currentTimeMillis();
	}

	public void fire(ArrayList<Bullet> shots) {
		if(System.currentTimeMillis()>lastFire+fireDel) {
			lastFire = System.currentTimeMillis();
			shots.add(new Bullet(pTurret.pos()[0]+25*Math.cos(pTurret.rPos()),pTurret.pos()[1]+25*Math.sin(pTurret.rPos()), pTurret.rPos(), 0, true));
		}
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
		//vel[0] = Math.max(-maxVel, Math.min(vel[0], maxVel));
		pBody.vel(vel);
		pTurret.vel(vel);
	}
	
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
	//health bar
		g2d.setColor(gCols.health);
		g2d.fillRect((int)pos[0]-25,(int)pos[1]-45,health/4,5);
	//reload bar
		g2d.setColor(gCols.reload);
		g2d.fillRect((int)pos[0]-26, (int)pos[1]-37, (int)Math.min((System.currentTimeMillis()-lastFire)/10,50), 5);
	//tank sprites
		g2d.transform(pBody.trans());
		iBody.paintIcon(null, g2d, -41, -36);
		g2d.setTransform(prev);
		g2d.transform(pTurret.trans());
		iTurret.paintIcon(null, g2d, -23,-20);
		g2d.setTransform(prev);
	}
	public boolean takeDamage(int damage) {
		health -= damage;
		return health < 0;
	}
	public void tStep() {
		addVel(accRate-.01*accConst*vel[0]);
		pBody.tStep();
		pTurret.tStep();
		pos = pBody.pos();
		tranBounds = bounds.createTransformedArea(pBody.trans());
		accRate = 0;
	}
	public void start() {
		pBody.start();
		pTurret.start();
	}
	public Area tightBounds() {
		return (Area)tranBounds.clone();
	}
	public Rectangle rectBounds() {
		return tranBounds.getBounds();
	}
	
	public void resolveCollide(Area colliding) {
		double xStep, yStep;
		double[] lPos = pos();
		double[] sPos = lPos.clone();
		if(vel[0]>0) {//move backwards to resolve
			xStep = -2*Math.cos(vel[1]);
			yStep = -2*Math.sin(vel[1]);
		}else {//move forwards to resolve
			xStep = 2*Math.cos(vel[1]);
			yStep = 2*Math.sin(vel[1]);
		}
		for(int i = 0; i < 15; i++) {
			lPos[0] += xStep;
			lPos[1] += yStep;
			pos(lPos);
			tranBounds = bounds.createTransformedArea(pBody.trans());
			colliding.intersect(tranBounds);
			if(colliding.isEmpty())return;
		}
		lPos = sPos.clone();
		xStep = -xStep;
		yStep = -yStep;
		for(int i = 0; i < 15; i++) {
			lPos[0] += xStep;
			lPos[1] += yStep;
			pos(lPos);
			tranBounds = bounds.createTransformedArea(pBody.trans());
			colliding.intersect(tranBounds);
			if(colliding.isEmpty())return;
		}
		lPos = sPos;
		pos(lPos);
		tranBounds = bounds.createTransformedArea(pBody.trans());
	}
	
	public void pos(double[] posIn) {
		this.pos = posIn;
		pBody.pos(posIn);
		pTurret.pos(posIn);
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

	public void acc(boolean b) {
		accRate = b?accConst:-accConst;
	}


}
