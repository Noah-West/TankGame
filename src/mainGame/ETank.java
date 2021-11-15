package mainGame;

import javax.swing.ImageIcon;

public class ETank extends Tank{
	public ETank(double x, double y, double mag, double rad, int health, boolean start) {
		super(x, y, mag, rad, start);
		this.health = health;
		iBody = new ImageIcon("Resource/eTankBody.png");                
		iTurret = new ImageIcon("Resource/eTankTurret.png");            
		// TODO Auto-generated constructor stub
	}

}
