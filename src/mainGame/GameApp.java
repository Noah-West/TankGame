package mainGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import physics.*;


public class GameApp extends JPanel{
	private PhysImg egoBod, egoTurret;
	private ArrayList<PhysImg> enemies = new ArrayList<PhysImg>();
	private ArrayList<PhysImg> shots = new ArrayList<PhysImg>();
	
	
	
	public GameApp() {
		setBackground(Color.blue); 
		setPreferredSize(new Dimension(800,600));
		egoBod = new PhysImg(new double[] {400, 500}, new ImageIcon("Resource/TankBody.png"), false);
		ImageIcon turr = new ImageIcon("Resource/TankTurret.png");
		egoTurret = new PhysImg(new double[] {400, 500}, new int[] {turr.getIconWidth()/2, turr.getIconWidth()/2}, turr, false);

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
	}
}
