package mainGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Area;
public class GameApp extends JPanel{
	private final long invFps = 1000/60;
	private final double rotRate = (Math.PI/30)/3;
	private int lives, score;
	static protected KeyListen keyb = new KeyListen();
	private Area screenBounds;
	private Tank player;
	private boolean inBounds = true;
	//private ArrayList<PhysImg> enemies = new ArrayList<PhysImg>();
	private ArrayList<Bullet> shots = new ArrayList<Bullet>();
	private long fireDel = 200;
	
	public GameApp() {
		setBackground(new Color(0x33421f)); 
		setPreferredSize(new Dimension(800,600));
		addKeyListener(keyb);
		setFocusable(true);
		screenBounds = new Area(new Rectangle(0,0,800,600));
		player = new Tank(400, 500, 0, 0, false);
		
	}
	public void play() {
		long loopTime;
		long lastFire; //sys time of last shot
	//game setup
		lives = 5; score = 0;
		Random rnd = new Random();
		lastFire = System.currentTimeMillis();
		player.start();
		//for(PhysObj o:enemies) o.start();
		while(true) {
			loopTime = System.currentTimeMillis()+invFps;
			
			if(lives<=0)return;
		//key handling
			if(keyb.keys[KeyEvent.VK_LEFT]) player.rotate(-rotRate);
			if(keyb.keys[KeyEvent.VK_RIGHT]) player.rotate(rotRate);
			if(keyb.keys[KeyEvent.VK_UP]) player.acc(true);
			if(keyb.keys[KeyEvent.VK_DOWN]) player.acc(false);
			if(keyb.keys[KeyEvent.VK_Z]) player.rotTurret(-rotRate);
			if(keyb.keys[KeyEvent.VK_X]) player.rotTurret(rotRate);

			if(keyb.keys[KeyEvent.VK_Q]) return;
			if(keyb.keys[KeyEvent.VK_SPACE]) {
				if(System.currentTimeMillis()>lastFire+fireDel) {
					shots.add(player.fire());
					lastFire = System.currentTimeMillis();
				}
			}
			/*if(targs.size()<(score/50+1)&&System.currentTimeMillis()>nextTargTime) {
				PhysImg t = new PhysImg(new double[] {rnd.nextInt(450)+50, 70}, enemyS, false);
				if(t.pos[0]-t.offset[0]<50)t.pos[0]= 50+t.offset[0];
				if(t.pos[0]+t.offset[0]>500)t.pos[0]=500-t.offset[0];
				t.setVel(0, 100);
				targs.add(t);
				targDel = Math.max(targDel-10, 500);
				vShip = Math.min(vShip+10, 300);
				fireDel = Math.max(fireDel-3, 100);
				nextTargTime = System.currentTimeMillis()+targDel;
				t.start();
				System.out.println(targs.size());
			}*/
			updatePhysics();
			repaint();
			while(System.currentTimeMillis()<loopTime);
		}
	}
	public void updatePhysics() {
		player.tStep();
	//keeping player on screen
		Rectangle box = player.rectBounds();
		double[] pos = player.pos();
		if(box.getX()<0)pos[0] += 10;
		else if(box.getX()+box.getWidth()>800)pos[0] -= 10;
		if(box.getY()<0)pos[1] += 10;
		else if(box.getY()+box.getHeight()>600)pos[1] -= 10;
		player.pos(pos);
	//stepping/deleting shots
		long t = System.currentTimeMillis();
		Bullet b;
		for(int i = 0; i < shots.size();i++) {
			b = shots.get(i);
			b.tStep();
			if(t>b.endTime()) {
				shots.remove(i);
				b = null;
			}
		}
		
	}
	private boolean inBounds(Shape obj) {
		Area o = new Area(obj);
		Area bound = new Area(new Rectangle(0,0,800,600));
		bound.intersect(o);
		return o.equals(bound);
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		player.draw(g2d);
		for(Bullet b:shots) b.draw(g2d);
		
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("SpaceDefender");
		Container cont = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameApp app = new GameApp();
		cont.add(app); 
		frame.pack(); 
		frame.setVisible(true);
		app.play();
	}
}
