package mainGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import physics.*;


public class GameApp extends JPanel{
	private PhysImg ego;
	private ArrayList<PhysImg> enemies = new ArrayList<PhysImg>();
	private ArrayList<PhysImg> shots = new ArrayList<PhysImg>();
	
	
	
	public GameApp() {
		setBackground(Color.black); 
		setPreferredSize(new Dimension(800,600));
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
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
