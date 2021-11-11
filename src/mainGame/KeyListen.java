package mainGame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListen implements KeyListener{
	public boolean[] keys = new boolean[100];
	private volatile boolean keyPress;
	public KeyListen() {
	}
	public void waitForKey() {
		keyPress = false;
		while(keyPress == false);
		System.out.println("end waitkey");
	}
	public void keyPressed(KeyEvent e) {
		keyPress = true;
		if(e.getKeyCode()<100)keys[e.getKeyCode()]=true;
		System.out.println("keypress "+e.getKeyCode());
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()<100)keys[e.getKeyCode()]=false;
		System.out.println("keyrelease "+e.getKeyCode());

	}
	public void keyTyped(KeyEvent e) {
	}
}