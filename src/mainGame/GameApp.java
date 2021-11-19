package mainGame;

import Bodies.*;
import FScale.FScale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Area;

public class GameApp extends JPanel{
	private final double rotRate = (Math.PI/30)/5;
	protected final long invFps = 1000/60;
	
	protected KeyListen keyb = new KeyListen();
	private FScale windScaler;
	private Menu menu;
	private SFX sfx;
	protected int score;
	private Tank player;
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Bullet> shots = new ArrayList<Bullet>();
	
	
	public GameApp() {
		sfx = new SFX();
		menu = new Menu();
		setBackground(gCols.bg); 
		windScaler = new FScale(this, 800, 600);
		addKeyListener(keyb);
		setFocusable(true);
		player = new Tank(400, 500, 0, -Math.PI/2, 200, false);
		nextEnemy = System.currentTimeMillis()-2000;
	}
	public void play() {
		long loopTime; //time at start of last loop
	//game setup
		score = 0;
		Random rnd = new Random();
		player.start();
		for(Enemy s:enemies)s.start();
		while(true) {
			loopTime = System.currentTimeMillis()+invFps;
			
		//key handling
			if(keyb.keys[KeyEvent.VK_LEFT]) player.rotate(-rotRate);
			if(keyb.keys[KeyEvent.VK_RIGHT]) player.rotate(rotRate);
			if(keyb.keys[KeyEvent.VK_UP]) player.acc(true);
			if(keyb.keys[KeyEvent.VK_DOWN]) player.acc(false);
			if(keyb.keys[KeyEvent.VK_Z]) player.rotTurret(-rotRate);
			if(keyb.keys[KeyEvent.VK_X]) player.rotTurret(rotRate);
			if(keyb.keys[KeyEvent.VK_SPACE]) {
				player.fire(shots);
				//sfx.shot();
			}
			if(keyb.keys[KeyEvent.VK_P]) menu.main(this);
			if(keyb.keys[KeyEvent.VK_Q]) {
				return;

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
				sfx.hit();
				shots.remove(j);
				if(player.takeDamage(shot.damage()))return 1;
				shot = null;
			}
		}
	//handling enemy hits
		for(int i = 0; i < enemies.size();i++) {//loop though each enemy
			Enemy e = enemies.get(i);
			collide = e.tightBounds();
			for(int j = 0; j < shots.size();j++) {//loop though shots, on each enemy
				Bullet shot = shots.get(j);
				if(collide.contains(shot.tip())&&shot.type()==0) {//if shot hit the enemy
					if(e.takeDamage(shot.damage())) {//if enemy dies
						sfx.eHit();
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
	private int enemyDel = 2500;
	private double tankProb = 0;
	private double genEnemyX;
	private static Random rnd = new Random(System.currentTimeMillis());
	/**
	 * generate new enemies based on a set of changing probabilities
	 */
	private void genEnemies() {
		if(System.currentTimeMillis()<nextEnemy)return;
		genEnemyX = (genEnemyX + rnd.nextDouble()*700)%800;
		double rad = PObj.toMagRad(new double[] {400-genEnemyX, 450})[1]; //point towards center   -clampGaus()*300
		rad += Math.PI/6*clampGaus(); // add some randomness, up to 45 deg either way
		rad = Math.max(Math.PI/4, Math.min(rad, 3*Math.PI/4));
		Enemy newEnemy;
		if(rnd.nextDouble()<tankProb) { //randomly select tank
			boolean tPlr = Math.min(score, 70)>(rnd.nextInt(120)+20); //after score reaches 20, increasing probability of tank targeting player
			newEnemy = new ETank(genEnemyX,-50, rad,false, tPlr);
			tankProb -= .15; //decrease probability of next enemy being a tank
		}
		else {
			newEnemy = new Jeep(genEnemyX,-50,rad,false);
			tankProb += .1; //increase probability of next enemy being a tank
		}					// starts at 0, so hevily biased towards jeeps at beginning
		enemies.add(newEnemy);		//capped at .7 probability of targeting player
		newEnemy.start();
		nextEnemy = System.currentTimeMillis() + enemyDel + (long)(clampGaus()*250);
		enemyDel = (enemyDel < 1200)?enemyDel:(int)(enemyDel*.996); //decrement delay between enemies until it reaches 600ms
	}
	public static double clampGaus() { //returns gaussian distributed multiplier from -1 to 1
		return Math.max(-1, Math.min(rnd.nextGaussian(), 1));
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = windScaler.scale(g);
		for(Enemy s:enemies) s.draw(g2d);
		player.draw(g2d);
		for(Bullet b:shots) b.draw(g2d);
		g2d.setColor(Color.black);
		g2d.setFont(gCols.sFont);
		g2d.drawString(String.format("SCORE: %d eDel: %d",score, enemyDel), 10, 30);
		if(menu.inMenu()) menu.draw(g2d);
	}
	public static void main(String[] args) {
		while(true) {
		JFrame frame = new JFrame("SpaceDefender");
		Container cont = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		GameApp app;
			app = new GameApp();
			cont.add(app); 
			frame.pack(); 
			frame.setVisible(true);
			app.player.tStep();
			app.menu.main(app);
			app.play();
			app.menu.highScore(app);
		}
	}
}
