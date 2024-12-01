package engine;

import gui.Window;

public class Game implements Runnable {
	
	private Window window;
	private Input input;
	
	private boolean running = false;
	private final double UPDATE_CAP = 1.0 / 60.0;
	
	private int width = 700, height = 600;
	private float scale = 1f;
	private String title = "Tetris";
	private static int fps;
	
	private static int count = 0;
	private static Clock clock = new Clock();
	private static Thread clockThread = new Thread(clock);

	public Game() {
		
	}
	
	public void start() {
		window = new Window(this);
		input = new Input(this);
		
		final Thread thread = new Thread(this);
		thread.start();
	}
	
	public void stop() {

	}
	
	public void run() {
		running = true;
		
		boolean render = false;
		double now = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		
		double frameTime = 0;
		int frames = 0;
		fps = 0;
		
		while (running) {
			if (window.getScreen() == Window.Screen.Game && Game.count == 0) {
				clockThread.start();
				count++;
			}
			
			render = false;
		
			now = System.nanoTime() / 1000000000.0;
			passedTime = now - lastTime;
			lastTime = now;	
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			while (unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;
				// update game		
				window.update();
				input.update();
				if (frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					setFps(fps);
					frames = 0;
				}
			}
			if (render) {
			// render game
				try {
					window.paint();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				frames++;
			}
			else {
				try {
					Thread.sleep(0);
				} 
				catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
		
		dispose();
	}
	
	private void dispose() {
		
	}

	public Window getWindow() {
		return window;
	}

	public Input getInput() {
		return input;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getScale() {
		return scale;
	}

	public String getTitle() {
		return title;
	}

	public static int getFps() {
		return fps;
	}

	public static void setFps(int fps) {
		Game.fps = fps;
	}
	
}
