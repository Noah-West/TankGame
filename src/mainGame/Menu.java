package mainGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import javax.swing.ImageIcon;

public class Menu {
	private ImageIcon tanks;
	private boolean arrow = false;
	private boolean inMenu = false;
	private ArrayList<Integer> scores = new ArrayList<Integer>(5);
	private File scoresF;
	public Menu(){
		//tanks = new ImageIcon("Resource/logo.png"); 
		inMenu = false;
		arrow = false;	
		try{
			scoresF = new File("TankScores");
			if(!scoresF.exists()) {
				for(int i = 0; i<5; i++)scores.add(0);
				rewriteScores(scoresF);
			}
			Scanner sScan = new Scanner(scoresF);
			for(int i = 0; i <5; i++){
				scores.add(sScan.nextInt());
				System.out.println(scores.get(i));
			}
		}catch(FileNotFoundException e) {
			System.out.println("Scores Error");
		}
	}
	private void rewriteScores(File scoresF) {
		PrintWriter outStream;
		try {
			outStream = new PrintWriter(scoresF);
			for(int i = 0; i<5;i++) {
				outStream.println(scores.get(i));
			}
			outStream.close();		
		} catch (FileNotFoundException e) {
			System.out.println("Scores Error");
		}
	}
	public void main(GameApp app) {
		inMenu = true;
		app.repaint();
		while(true) {
			app.keyb.waitForKey();
			switch(app.keyb.lastKey()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_Z:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_X:
				arrow = !arrow;
				break;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_ENTER:
				if(arrow) {
					inMenu = false;
					return;
				}else System.exit(0);
			}
			app.repaint();
		}
	}
	public void highScore(GameApp app) {
		if(app.score<scores.get(4))return;
		for(int i = 0; i <5; i ++) {
			if(app.score>scores.get(i)) {
				scores.add(i, app.score);
				break;
			}
		}
		scores.remove(5);
		rewriteScores(scoresF);
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(0,0,0,200));
		g2d.fillRect(40, 40, 720, 520);
		g2d.setFont(gCols.bFont);
		g2d.setColor(Color.white);
		g2d.drawString("PLAY", 100, 360);
		g2d.drawString("QUIT", 100, 420);
		g2d.drawString(">", 70, arrow?360:420);
		scoreboard(g2d);
		controls(g2d);
		g2d.setFont(gCols.mFont);
		g2d.setColor(Color.gray);
		g2d.drawString("Created by Noah West for 2021 AP JAVA", 115, 540);
	}
	private void controls(Graphics2D g2d) {
		int x = 350, ys = 330, dy = 40;
		g2d.setFont(gCols.mFont);
		g2d.setColor(gCols.reload);
		g2d.drawString("Space - Fire", x+52, ys);
		g2d.drawString("Left/Right - Rotate tank", x, ys+dy);
		g2d.drawString("Up/Down - Drive tank", x+12, ys+dy*2);
		g2d.drawString("Z/X - Rotate Turret", x+94, ys+dy*3);
	}
	private void scoreboard(Graphics2D g2d) {
		g2d.setColor(gCols.plrBlue);
		for(int i = 0; i <5; i++){
			g2d.drawString(String.format("#%d %05d%n", i+1, scores.get(i)), 550, 110+40*i);
		}
	}

	public boolean inMenu() {return inMenu;}
}
