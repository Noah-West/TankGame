package physics;
import java.awt.*;
import java.awt.geom.AffineTransform;
public class PhysShape extends PhysObj{
	private Color col;
	public Shape obj;
	private boolean fill;
	public PhysShape(double x, double y, Shape obj, Color col, boolean fill,boolean velOnly){
		super(new double[] {x, y}, new double[] {0,0}, new double[] {0,0}, velOnly);
		this.obj = obj;
		this.col = col;
		this.fill = fill;
  }
	public PhysShape(double[] p, Shape obj, boolean velOnly, boolean fill, Color col) {
		super(p.clone(), new double[] {0,0}, new double[] {0,0}, velOnly);
		this.obj = obj;
		this.fill = fill;
		this.col = col;
	}
	public PhysShape(double[] pos, double[] vel, double[] acc, Shape obj, boolean fill, boolean velOnly, Color col) {
		super(pos.clone(), vel.clone(), acc.clone(), velOnly);
		this.obj = obj;
		this.fill = fill;
		this.col = col;
	}
	public void draw(Graphics2D g2d){
			AffineTransform prev = g2d.getTransform();
			g2d.setColor(col);
			if(fill)g2d.fill(obj);
			else g2d.draw(obj);
			//add transform
			g2d.setTransform(prev);
		}
}
