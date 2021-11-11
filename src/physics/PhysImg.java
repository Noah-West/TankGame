package physics;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;
public class PhysImg extends PhysObj{
	private Color col;
	private ImageIcon img;
	public int[] offset;
	public PhysImg(double[] pos, int[] offset, ImageIcon img, boolean velOnly) {
		super(pos, new double[]{0,0}, new double[] {0,0}, velOnly);
		this.offset = offset.clone();
		this.img = img;
	}
	public PhysImg(double[] pos, ImageIcon img, boolean velOnly) {
		super(pos, new double[]{0,0}, new double[] {0,0}, velOnly);
		this.offset = new int[] {img.getIconWidth()/2, img.getIconHeight()/2};
		this.img = img;
	}
	public void draw(Graphics2D g2d) {
		prev = g2d.getTransform();
		g2d.transform(trans);
		g2d.setColor(col);
		img.paintIcon(null, g2d, -offset[0], -offset[1]);
		g2d.setTransform(prev);
	}
	public Rectangle getBounds(boolean transform) {
		int hw, hh;
		hw = img.getIconWidth()/2;
		hh = img.getIconHeight()/2;
		return transform?new Rectangle(-hw, -hh, hw, hh):new Rectangle((int)pos[0]-hw,(int)pos[1]-hh,(int)pos[0]+hw,(int)pos[1]+hh);
	}
}
