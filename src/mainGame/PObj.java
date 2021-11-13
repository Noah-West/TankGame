package mainGame;

import java.awt.geom.AffineTransform;

public class PObj {
	public  double[] pos = new double[2];
	private double[] vel = new double[2];
	private double rPos, rVel;
	private long lastTime, time;
	protected AffineTransform trans;
	private double scale;
	public PObj(double x, double y, double mag, double rad, boolean start) {
		this(new double[] {x, y}, new double[] {mag, rad}, start);
	}
	public PObj(double[] pos, double[] vel, boolean start) {
		this.pos = pos;
		this.vel = vel;
		this.rPos = 0;
		this.rVel = 0;
		this.scale = 1;
		trans = new AffineTransform();
		trans.setToTranslation(pos[0], pos[1]);
		if(start)start();
	}
	public void start() {
		lastTime = System.currentTimeMillis();
	}
	public void tStep() {
		time = System.currentTimeMillis();
		double tdiff = (time-lastTime)/1000d;
		pos[0] += vel[0]*tdiff*Math.cos(vel[1]);
		pos[1] += vel[0]*tdiff*Math.sin(vel[1]);
		rPos += rVel;
		trans.setToTranslation(pos[0], pos[1]);
		trans.scale(scale, scale);
		trans.rotate(rPos);
		lastTime = time;
	}
	public static double[] toXY(double mag, double rad){
		double[] out = new double[2];
		out[0] = mag*Math.cos(rad);
		out[1] = mag*Math.sin(rad);
		return out;
	}
	public static double[] toMagRad(double[] in){
		double[] out = new double[2];
		out[0] = Math.sqrt(in[0]*in[0]+in[1]*in[1]);
		out[1] = Math.atan(in[1]/in[0]);
		return out;
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
	/**
	 * @return the rPos
	 */
	public double rPos() {
		return rPos;
	}
	/**
	 * @return the rVel
	 */
	public double rVel() {
		return rVel;
	}
	/**
	 * @param pos the pos to set
	 */
	public void pos(double[] pos) {
		this.pos = pos;
	}
	/**
	 * @param vel the vel to set
	 */
	public void vel(double[] vel) {
		this.vel = vel;
	}
	/**
	 * @param rPos the rPos to set
	 */
	public void rPos(double rPos) {
		this.rPos = rPos;
	}
	/**
	 * @param rVel the rVel to set
	 */
	public void rVel(double rVel) {
		this.rVel = rVel;
	}
	/**
	 * @return the trans
	 */
	public AffineTransform trans() {
		return trans;
	}
	/**
	 * @param trans the trans to set
	 */
	public void trans(AffineTransform trans) {
		this.trans = trans;
	}
	/**
	 * @return the scale
	 */
	public double scale() {
		return scale;
	}
	/**
	 * @param scale the scale to set
	 */
	public void scale(double scale) {
		this.scale = scale;
	}
	
}
