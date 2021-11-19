package mainGame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyListen implements KeyListener{
	private final boolean DEBUG = false;
	public boolean[] keys = new boolean[100];
	private int lastKey = 0;
	private volatile boolean keyPress;
	public KeyListen() {
	}
	public void waitForKey() {
		keyPress = false;
		while(keyPress == false);
		if(DEBUG)System.out.println("end waitkey");
	}
	public int lastKey() {
		return lastKey;
	}
	public void keyPressed(KeyEvent e) {
		keyPress = true;
		lastKey = e.getKeyCode();
		if(e.getKeyCode()<100)keys[e.getKeyCode()]=true;
		if(DEBUG)System.out.println("keypress "+e.getKeyCode());
	}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()<100)keys[e.getKeyCode()]=false;
		if(DEBUG)System.out.println("keyrelease "+e.getKeyCode());

	}
	public void keyTyped(KeyEvent e) {
	}
}