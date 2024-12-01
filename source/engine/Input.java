package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener {
	
	private Game game;
	
	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLast = new boolean[NUM_KEYS];
	private long[] lastTimeKeyHold = new long[NUM_KEYS];
	private int delayHold[] = new int[NUM_KEYS];
	
	private final int NUM_BUTTONS = 6;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];
	
	private int mouseX, mouseY;
	private int lastMouseX, lastMouseY;
	
	public Input(Game game) {
		this.game = game;
		mouseX = 0;
		mouseY = 0;
		lastMouseX = 0;
		lastMouseY = 0;
		
		game.getWindow().getCanvas().addKeyListener(this);
		game.getWindow().getCanvas().addMouseListener(this);
		game.getWindow().getCanvas().addMouseMotionListener(this);
	}
	
	public void update() {
		for (int i = 0; i < NUM_KEYS; i++) {
			keysLast[i] = keys[i];
		}
		for (int i = 0; i < NUM_BUTTONS; i++) {
			buttonsLast[i] = buttons[i];
		}
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public boolean isKey(int keyCode) {
		return keys[keyCode];
	}
	
	public boolean isKeyHold(int keyCode) {
		if (isKeyDown(keyCode)) {
			delayHold[keyCode] = 600;
		}
		if (System.currentTimeMillis() - lastTimeKeyHold[keyCode] > delayHold[keyCode] || isKeyDown(keyCode)) {
			lastTimeKeyHold[keyCode] = System.currentTimeMillis();
			delayHold[keyCode] = delayHold[keyCode] - 300 < 50 ? 50 : delayHold[keyCode] - 300;
			return keys[keyCode];
		} 
		else {
			return false;
		}
	}
	
	public boolean isKeyUp(int keyCode) {
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	public boolean isKeyDown(int keyCode) {
		return keys[keyCode] && !keysLast[keyCode];
	}
	
	public boolean isButton(int button) {
		return buttons[button];
	}
	
	public boolean isButtonUp(int button) {
		return !buttons[button] && buttonsLast[button];
	}
	
	public boolean isButtonDown(int button) {
		return buttons[button] && !buttonsLast[button];
	}
	
	public boolean isButton(int button, int x, int y, int width, int height) {
		return buttons[button] && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean isButtonUp(int button, int x, int y, int width, int height) {
		return !buttons[button] && buttonsLast[button] && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean isButtonDown(int button, int x, int y, int width, int height) {
		return buttons[button] && !buttonsLast[button] && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean isMouseMove() {
		return (mouseX != lastMouseX || mouseY != lastMouseY) ? true : false;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int) (e.getX() / game.getScale());
		mouseY = (int) (e.getY() / game.getScale());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int) (e.getX() / game.getScale());
		mouseY = (int) (e.getY() / game.getScale());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() <= 6) {
			buttons[e.getButton()] = true;
		}		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() <= 6) {
			buttons[e.getButton()] = false;
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() <= 256) {
			keys[e.getKeyCode()] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() <= 256) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

}