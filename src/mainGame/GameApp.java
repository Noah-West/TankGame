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
	private ArrayList<Shooter> shooters = new ArrayList<Shooter>();
	private ArrayList<ETank> eTanks = new ArrayList<ETank>();
	private ArrayList<Shooter> eJeeps = new ArrayList<Shooter>();
	private ArrayList<Shooter> eMen = new ArrayList<Shooter>();

	private ArrayList<Bullet> shots = new ArrayList<Bullet>();
	
	public GameApp() {
		setBackground(gCols.bg); 
		setPreferredSize(new Dimension(800,600));
		addKeyListener(keyb);
		setFocusable(true);
		screenBounds = new Area(new Rectangle(0,0,800,600));
		player = new Tank(400, 500, 0, 0, 100, false);
		shooters.add(player);
		shooters.add(new ETank(50,50, 0,0,100,false));
	}
	public void play() {
		long loopTime;
		long lastFire; //sys time of last shot
	//game setup
		lives = 5; score = 0;
		Random rnd = new Random();
		for(Shooter s:shooters)s.start();
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
				Bullet shot = player.fire();
				if(shot!= null)shots.add(shot);
			}
			updatePhysics();
			repaint();
			while(System.currentTimeMillis()<loopTime);
		}
	}
	public void updatePhysics() {
		for(Shooter s:shooters) s.tStep();
	//keeping player on screen
		Rectangle box = player.rectBounds();
		double[] pos = player.pos();
		if(box.getX()<0) {
			pos[0] += 10;
			player.takeDamage(1);
		}
		else if(box.getX()+box.getWidth()>800) {
			pos[0] -= 10;
			player.takeDamage(1);
		}
		if(box.getY()<0) {
			pos[1] += 10;
			player.takeDamage(1);
		}
		else if(box.getY()+box.getHeight()>600) {
			pos[1] -= 10;
			player.takeDamage(1);
		}
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
	//handling hits
		for(int i = 0; i < shooters.size();i++) {
			Shooter s = shooters.get(i);
			Area collide = s.tightBounds();
			for(int j = 0; j < shots.size();j++) {
				Bullet shot = shots.get(j);
				if(collide.contains(shot.tip())) {
					if(s.takeDamage(shot.damage())) shooters.remove(i);
					shots.remove(j);
					shot = null;
				}
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
		for(Shooter s:shooters) s.draw(g2d);
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
