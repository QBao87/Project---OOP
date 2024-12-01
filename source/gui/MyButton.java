package gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import engine.Game;

public class MyButton {
	private Game game;
	
	private int x, y;
	private int width, height;
	private BufferedImage image;
	
	public MyButton(Game game, BufferedImage image, int x, int y, int width, int height) {
		this.game = game;
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void update() {
		
	}
	
	public void paint(Graphics g) {
		if (!isMouseOn() || game.getInput().isButton(MouseEvent.BUTTON1, x, y, width, height)) {
			g.drawImage(image, x, y, width, height, null);
		} 
		else {
			g.drawImage(image, (int) (x - width * 0.1), (int)(y - height * 0.1), (int) (width * 1.2), 
			(int) (height * 1.2), null);
		}		
	}
	
	private boolean isMouseOn() {
		return game.getInput().getMouseX() >= x && game.getInput().getMouseX() <= x + width
				&& game.getInput().getMouseY() >= y && game.getInput().getMouseY() <= y + height;
	}
	
	public boolean isMouse() {
		return game.getInput().isButton(MouseEvent.BUTTON1, x, y, width, height);
	}
	
	public boolean isMouseDown() {
		return game.getInput().isButtonDown(MouseEvent.BUTTON1, x, y, width, height);
	}
	
	public boolean isMouseUp() {
		return game.getInput().isButtonUp(MouseEvent.BUTTON1, x, y, width, height);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
}