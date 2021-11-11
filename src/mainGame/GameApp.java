package mainGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Area;

import physics.*;


public class GameApp extends JPanel{
	private final long invFps = 1000/60;
	private final int egoAcc = 10;
	private int lives, score;
	static protected KeyListen keyb = new KeyListen();
	
	private PhysImg egoBod, egoTurret;
	private ArrayList<PhysImg> enemies = new ArrayList<PhysImg>();
	private ArrayList<PhysImg> shots = new ArrayList<PhysImg>();
	private long fireDel;
	
	public GameApp() {
		setBackground(Color.blue); 
		setPreferredSize(new Dimension(800,600));
		addKeyListener(keyb);
		setFocusable(true);
		egoBod = new PhysImg(new double[] {400, 500}, new ImageIcon("Resource/TankBody.png"), false);
		egoBod.collideObj = new Rectangle(egoBod.img.getIconWidth()/2, img.getIconHeight()/2);
		ImageIcon turr = new ImageIcon("Resource/TankTurret.png");
		egoTurret = new PhysImg(new double[] {400, 500}, new int[] {turr.getIconWidth()/2, turr.getIconWidth()/2}, turr, false);

	}
	public void play() {
		long loopTime;
		long lastFire; //sys time of last shot
	//game setup
		lives = 5; score = 0;
		Random rnd = new Random();
		lastFire = System.currentTimeMillis();
		egoBod.start();
		egoTurret.start();
		for(PhysObj o:enemies) o.start();
		while(true) {
			loopTime = System.currentTimeMillis()+invFps;
			
			if(lives<=0)return;
		//key handling
			double[] acc = new double[2];
			if(keyb.keys[KeyEvent.VK_LEFT]&&egoBod.collideObj.intersects(-1,0, 0, 600)) acc[0] = -egoAcc;
			if(keyb.keys[KeyEvent.VK_RIGHT]&&egoBod.collideObj.intersects(800,0, 801, 600)) acc[0] += egoAcc;
			if(keyb.keys[KeyEvent.VK_UP]&&egoBod.collideObj.intersects(0,-1, 800, 0)) acc[1] = -egoAcc;
			if(keyb.keys[KeyEvent.VK_DOWN]&&egoBod.collideObj.intersects(0,600, 800, 601)) acc[1] += egoAcc;
			if(keyb.keys[KeyEvent.VK_Q]) return;
			if(keyb.keys[KeyEvent.VK_SPACE]) {
				if(System.currentTimeMillis()>lastFire+fireDel) {
					
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
		PhysImg s, t;
		egoBod.update();
		egoTurret.update();
		for(int i = 0; i < shots.size(); i++) {
			s = shots.get(i);
			s.update();
			if(!inBounds(s.collideObj))shots.remove(s);
		}
		for(int i = 0; i < enemies.size(); i++) {
			t = enemies.get(i);
			t.update();
			for(int j = 0; j<shots.size(); j++) {
				s = shots.get(j);
				if(s.pos[0]+s.offset[0]>t.pos[0]-t.offset[0]&&s.pos[0]-s.offset[0]<t.pos[0]+t.offset[0]&&s.pos[1]-s.offset[1]<t.pos[1]+t.offset[1]) {
					shots.remove(j);
					enemies.remove(i);
					t=null;
					score += 10;
					break;
				}
			}
			if(t==null)continue;
			if(t.pos[1]>550) {
				enemies.remove(t);
				lives --;
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
		egoBod.draw(g2d);
		egoTurret.draw(g2d);
		
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
