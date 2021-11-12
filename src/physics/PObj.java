package physics;

import java.awt.geom.AffineTransform;

public class PObj {
	public  double[] pos = new double[2];
	private double[] vel = new double[2];
	private double[] rPos = new double[2];
	private long lastTime, time;
	protected AffineTransform trans;
	public PObj(double x, double y, double mag, double rad, boolean start) {
		this.pos = new double[] {x, y};
		this.vel = new double[] {mag, rad};
		this.trans = new AffineTransform();
		trans.setToTranslation(pos[0], pos[1]);
		if(start)start();
	}
	public void start() {
		lastTime = System.currentTimeMillis();
	}
	public void update() {tStep();}
	public void tStep() {
		time = System.currentTimeMillis();
		double tdiff = (time-lastTime)/1000d;
		pos[0] = vel[0]*tdiff*Math.cos(vel[1]);
		lastTime = time;
		trans.setToTranslation(pos[0], pos[1]);
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
}
