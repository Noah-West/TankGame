package mainGame;

import javax.swing.*;

import Bodies.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Area;
public class GameApp extends JPanel{
	private final double rotRate = (Math.PI/30)/5;
	private final long invFps = 1000/60;
	private boolean inBounds = true;
	private Font mFont = new Font("Ariel", Font.BOLD, 20);
	private int lives, score;
	static protected KeyListen keyb = new KeyListen();
	private Area screenBounds;
	private Tank player;
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Bullet> shots = new ArrayList<Bullet>();
	
	public GameApp() {
		setBackground(gCols.bg); 
		setPreferredSize(new Dimension(800,600));
		addKeyListener(keyb);
		setFocusable(true);
		screenBounds = new Area(new Rectangle(0,0,800,600));
		player = new Tank(400, 500, 0, 0, 200, false);
		nextEnemy = System.currentTimeMillis()-2000;
		//enemies.add(new Jeep(50,50, Math.PI/4,false));
		//enemies.add(new ETank(500,50, 4*Math.PI/6,false));

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
		genEnemies();
		for(Enemy e:enemies) {
			e.tStep(shots, player);
		}
	//keeping player on screen
		Rectangle box = player.rectBounds();
		double[] pos = player.pos();
		if(box.getX()<0) {
			pos[0] += 10;
			player.takeDamage(3);
		} else if(box.getX()+box.getWidth()>800) {
			pos[0] -= 10;
			player.takeDamage(3);
		}
		if(box.getY()<0) {
			pos[1] += 10;
			player.takeDamage(3);
		} else if(box.getY()+box.getHeight()>600) {
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
	//handling player hits
		Area collide = player.tightBounds();
		for(int j = 0; j < shots.size();j++) {
			Bullet shot = shots.get(j);
			if(collide.contains(shot.tip())) {
				if(player.takeDamage(shot.damage()))return 1;
				shots.remove(j);
				shot = null;
			}
		}
	//handling enemy hits
		for(int i = 0; i < enemies.size();i++) {//loop though each enemy
			Enemy e = enemies.get(i);
			collide = e.tightBounds();
			for(int j = 0; j < shots.size();j++) {//loop though shots, on each enemy
				Bullet shot = shots.get(j);
				if(collide.contains(shot.tip())) {//if shot hit the enemy
					if(e.takeDamage(shot.damage())) {//if enemy dies
						score += (e instanceof Jeep)?5:10;//5 points for jeep, 10 for tank
						enemies.remove(i);
						e = null;
						shots.remove(j);
						shot = null;
						break; //break out of shot loop
					}
					shots.remove(j);
					shot = null;
				}
			}
		}
		return 0;
	}
	private long nextEnemy;
	private int enemyDel = 2000;
	private double tankProb = 0;
	private double prevEnemyX;
	private static Random rnd = new Random(System.currentTimeMillis());
	/**
	 * generate new enemies based on a set of changing probabilities
	 */
	private void genEnemies() {
		if(System.currentTimeMillis()<nextEnemy)return;
		double x = prevEnemyX + (rnd.nextDouble()-.5)*700;
		prevEnemyX = (x+800)%800;
		double rad = PObj.toMagRad(new double[] {400+clampGaus()*300-x, 450})[1]; //point towards center
		//rad += Math.PI/6*clampGaus(); // add some randomness, up to 45 deg either way
		Enemy newEnemy;
		if(rnd.nextDouble()<tankProb) { //randomly select tank
			boolean tPlr = Math.min(score, 70)<rnd.nextInt(20,100); //after score reaches 20, increasing probability of tank targeting player
			newEnemy = new ETank(x,-50, rad,false, tPlr);
			tankProb -= .15; //decrease probability of next enemy being a tank
		}
		else {
			newEnemy = new Jeep(x,-50,rad,false);
			tankProb += .1; //increase probability of next enemy being a tank
		}					// starts at 0, so hevily biased towards jeeps at beginning
		enemies.add(newEnemy);		//capped at .7 probability of targeting player
		newEnemy.start();
		nextEnemy = System.currentTimeMillis() + enemyDel + (long)(clampGaus()*250);
		enemyDel -= (enemyDel < 1200)?0:30; //decrement delay between enemies until it reaches 600ms
	}
	public static double clampGaus() { //returns gaussian distributed multiplier from -1 to 1
		return Math.max(-1, Math.min(rnd.nextGaussian(0, .33), 1));
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
		g2d.setColor(Color.black);
		g2d.setFont(mFont);
		g2d.drawString(String.format("SCORE: %d",score), 10, 30);
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
