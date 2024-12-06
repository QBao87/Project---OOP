package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;


import engine.Game;
import screen.GameScreen;
import screen.MenuScreen;

public class Window {

	public enum Screen {
		Menu,
		Game
	}
	
	private Screen screen = Screen.Menu;
	public static Game game;

	private JFrame frame;
	public static JProgressBar bar = new JProgressBar();
	private ImageIcon icon = new ImageIcon("resources/logo.png");
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;
	
	private boolean isMute = false;
	
	private MenuScreen menuScreen;

	public Window(Game game) {
		canvas = new Canvas();
		Dimension s = new Dimension((int) (game.getWidth() * game.getScale()), (int) (game.getHeight() * game.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);

		bar.setValue(0);
		bar.setBounds(0, game.getHeight() - 20, game.getWidth(), 20);
		bar.setFont(new Font("Pricedown Bl", Font.BOLD, 15));
		bar.setStringPainted(true);
		bar.setForeground(Color.RED);
		bar.setBackground(Color.WHITE);
		bar.setVisible(false);

		frame = new JFrame(game.getTitle());
		frame.setIconImage(icon.getImage());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(bar);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();

		Window.game = game;
		menuScreen = new MenuScreen(game);
	}
	
	public void update() {
		if (screen == Screen.Game) 
			MenuScreen.gameScreen.update();
		else 
			menuScreen.update();
	}
	
	public void paint() throws InterruptedException {
		if (screen == Screen.Game) {
			MenuScreen.gameScreen.paint(g);
		} 
		else {
			menuScreen.paint(g);
		}
		bs.show();
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}
	
	public Screen getScreen() {
		return screen;
	}

	public GameScreen getGameScreen() {
		return MenuScreen.gameScreen;
	}

	public boolean isMute() {
		return isMute;
	}

	public void setMute(boolean isMute) {
		this.isMute = isMute;
	}

}