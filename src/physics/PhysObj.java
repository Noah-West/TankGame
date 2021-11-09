package physics;
import java.awt.*;
import java.awt.geom.AffineTransform;
/** All units in pixels/second^x
 */
public abstract class PhysObj{
	public  double[] pos = new double[2];
	private double[] vel = new double[2];
	private double[] acc = new double[2];
	private double[] rVel = new double[2];
	private double[] rAcc = new double[2];
	private long lastTime, time;
	private boolean velOnly, fill;
	protected AffineTransform prev, trans;
	public PhysObj(double[] pos, double[] vel, double[] acc,boolean velOnly) {
		this.pos = pos.clone();
		this.vel = vel.clone();
		this.acc = acc.clone();
		this.velOnly = velOnly;
		this.trans = new AffineTransform();
	}
	public abstract void draw(Graphics2D g2d);
	public void start() {
		lastTime = System.currentTimeMillis();
	}
	public void update() {tStep();}
	public void tStep() {
		time = System.currentTimeMillis();
		vel = vAdd(vel, sMultiply(acc, (time-lastTime)/1000d));
		pos = vAdd(pos, sMultiply(vel, (time-lastTime)/1000d));
		lastTime = time;
	}
	public void transform() {
		trans.setToTranslation(pos[0], pos[1]);
	}
	private double[] sMultiply(double[] x, double s) { // scalar multiply
		double[] o = new double[x.length];
		for(int i = 0;i<x.length;i++) o[i] = x[i]*s;
		return o;
	}
	private double[] vAdd(double[] x, double[] y) {
		if(x.length != y.length) throw new IllegalArgumentException("Cannot add arrays of differnt length");
		double[] out = new double[x.length];
		for(int i = 0; i < x.length; i++) {
			out[i] = x[i] + y[i];
		}
		return out;
	}
	private int[] vAdd(int[] x, double[] y) {
		if(x.length != y.length) throw new IllegalArgumentException("Cannot add arrays of differnt length");
		int[] out = new int[x.length];
		for(int i = 0; i < x.length; i++) {
			out[i] = (int)(x[i] + y[i]);
		}
		return out;
	}
	public static double[] toXY(double mag, double rad){
		double[] out = new double[2];
		out[0] = mag*Math.sin(rad);
		out[1] = mag*Math.cos(rad);
		return out;
	}
	public void setPos(double[] in) {
		pos = in;
	}
	public void setPos(double x, double y) {
		pos[0] = x;
		pos[1] = y;
	}
	public void setVel(double[] in) {
		vel = in.clone();
	}
	public void setVel(double x, double y) {
		vel[0] = x;
		vel[1] = y;
	}
	public void setAcc(double[] in) {
		if(velOnly) throw new IllegalArgumentException("object not acceleration controlled");
		acc = in.clone();
	}
	public void setAcc(double x, double y) {
		setAcc(new double[] {x, y});
	}
	/**
	 * @return the pos
	 */
	public double[] p() {
		return pos;
	}
	/**
	 * @return the vel
	 */
	public double[] v() {
		return vel;
	}
	/**
	 * @return the acc
	 */
	public double[] a() {
		return acc;
	}
	/**
	 * @return the rVel
	 */
	public double[] rV() {
		return rVel;
	}
	/**
	 * @return the rAcc
	 */
	public double[] rA() {
		return rAcc;
	}
	/**
	 * @return the velOnly
	 */
	public boolean isVelOnly() {
		return velOnly;
	}
}
