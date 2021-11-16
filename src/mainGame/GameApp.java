package mainGame;

import javax.swing.*;

import Bodies.*;
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
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Bullet> shots = new ArrayList<Bullet>();
	
	public GameApp() {
		setBackground(gCols.bg); 
		setPreferredSize(new Dimension(800,600));
		addKeyListener(keyb);
		setFocusable(true);
		screenBounds = new Area(new Rectangle(0,0,800,600));
		player = new Tank(400, 500, 0, 0, 200, false);
		enemies.add(new Jeep(50,50, Math.PI/4,false));
		enemies.add(new ETank(500,50, 4*Math.PI/6,false));

	}
	public void play() {
		long loopTime;
		long lastFire; //sys time of last shot
	//game setup
		lives = 5; score = 0;
		Random rnd = new Random();
		player.start();
		for(Enemy s:enemies)s.start();
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

			if(keyb.keys[KeyEvent.VK_Q]) System.exit(0);
			if(keyb.keys[KeyEvent.VK_SPACE]) {
				player.fire(shots);
			}
			if(updatePhysics()==1)return;
			repaint();
			while(System.currentTimeMillis()<loopTime);
		}
	}
	public int updatePhysics() {
		player.tStep();
		for(Enemy e:enemies) {
			e.tStep(shots);
		}
	//keeping player on screen
		Rectangle box = player.rectBounds();
		double[] pos = player.pos();
		if(box.getX()<0) {
			pos[0] += 10;
			player.takeDamage(3);
		}
		else if(box.getX()+box.getWidth()>800) {
			pos[0] -= 10;
			player.takeDamage(3);
		}
		if(box.getY()<0) {
			pos[1] += 10;
			player.takeDamage(3);
		}
		else if(box.getY()+box.getHeight()>600) {
			pos[1] -= 10;
			player.takeDamage(3);
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
		Area collide = player.tightBounds();
		for(int j = 0; j < shots.size();j++) {
			Bullet shot = shots.get(j);
			if(collide.contains(shot.tip())) {
				if(player.takeDamage(shot.damage()))return 1;
				shots.remove(j);
				shot = null;
			}
		}
		for(int i = 0; i < enemies.size();i++) {
			Enemy e = enemies.get(i);
			collide = e.tightBounds();
			for(int j = 0; j < shots.size();j++) {
				Bullet shot = shots.get(j);
				if(collide.contains(shot.tip())) {
					if(e.takeDamage(shot.damage())) enemies.remove(i);
					shots.remove(j);
					shot = null;
					e = null;
				}
			}
		}
		return 0;
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
		for(Enemy s:enemies) s.draw(g2d);
		for(Bullet b:shots) b.draw(g2d);
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("SpaceDefender");
		Container cont = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		while(true) {
			GameApp app = new GameApp();
			cont.add(app); 
			frame.pack(); 
			frame.setVisible(true);
			app.play();
		}
	}
}
