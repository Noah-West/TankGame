
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;


public class test2 extends JPanel{
	private Area border;
	private Area colliding;
	private double[] pos;
	private Area tranBounds;
	private AffineTransform Otrans;
	private Area obj;
	public test2() {
		setBackground(Color.black);
		setPreferredSize(new Dimension(800,600));
		border = new Area(new Rectangle(0,0,800,600));
		border.subtract(new Area(new Rectangle(50,50,700,500)));
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.red);
		g2d.fill(border);
		g2d.setColor(Color.blue);
		g2d.fill(obj);
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("SpaceDefender");
		Container cont = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test2 app = new test2();
		cont.add(app);
		frame.setUndecorated(true);
		frame.pack(); 
		frame.setVisible(true);
		app.test();
	}
	private void test() {
		obj = new Area(new Rectangle(100,45,50,60));
		Otrans = new AffineTransform();
		Otrans.translate(0, 1);
		colliding = new Area();
		addCollide(border);
		while(true) {
			if(resolveCollide())break;
			repaint();
			int x = 1;
			long i = System.currentTimeMillis()+1000;
			while(System.currentTimeMillis()<i);
		}
		repaint();
	}
	public boolean resolveCollide() {
		obj.transform(Otrans);
		colliding.intersect(obj);
		return colliding.isEmpty();
	}
	public void addCollide(Area bounds) {//when this object is "responsible" for the collision
		colliding.add(bounds);
	}
}
