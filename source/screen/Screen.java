package screen;

import java.awt.Graphics;

import engine.Game;

public abstract class Screen {
	
	protected Game game;
	
	protected Screen(Game game) {
		this.game = game;
	}
	
	public abstract void update();
	
	public abstract void paint(Graphics g);

	public Game getGame() {
		return game;
	}

}
