package screen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import engine.Clock;
import engine.FileLoader;
import engine.Game;
import gameplay.Board;
import gameplay.InGame;
import gui.MyButton;
								
public class GameScreen extends Screen implements Runnable {

	private static BufferedImage background;
	private BufferedImage success;
	private BufferedImage failed;
	private BufferedImage sadEmoji;
	private BufferedImage star;
	private BufferedImage happyEmoji;
	private BufferedImage smileEmoji;
	private BufferedImage starEmoji;
	private BufferedImage backToMenu;
	private BufferedImage speaker;
	private BufferedImage pause;
	private BufferedImage pauseScreen;
	private BufferedImage playAgain;
	private BufferedImage next;
	private BufferedImage result;

	private static Clip clear;
	private static Clip win;
	private static Clip lose;

	private InGame inGame;
	private ArrayList<MyButton> buttons = new ArrayList<MyButton>();
	private MyButton buttonBackToMenu;
	private MyButton buttonSpeaker;
	private MyButton buttonPause;
	private MyButton buttonNext;

	private static boolean isPause = false;
	private boolean nextStage = false;
	private boolean isFailed = false;
	private static boolean isPlayAgain = false;
	private static boolean newClock = false;
	private int level = 1;
	private int count = 0;
	private int combo = 0;
	private int winLoop = 0;
	private int loseLoop = 0;

	private static int minuteStarter = 0; 
	private static int secondStarter = 0;
	private static int[] listOfLines = new int[] {1, 5, 15, 20, 25, 30, 35, 40, 45, 50};
	private static int stage = 0;
	
				
	public GameScreen(Game game) {
		super(game);
	}

	@Override
	public void run() {
		inGame = new InGame(this, level);

		success = FileLoader.loadImage("/success.png");
		failed = FileLoader.loadImage("/failed.png");
		star = FileLoader.loadImage("/star.png");
		backToMenu = FileLoader.loadImage("/back-to-menu.png");
		pause = FileLoader.loadImage("/pause-play.png");
		pauseScreen = FileLoader.loadImage("/pause.png");
		speaker = FileLoader.loadImage("/speak-mute.png");
		playAgain = FileLoader.loadImage("/play-again.png");
		next = FileLoader.loadImage("/next.png");
		result = FileLoader.loadImage("/result.png");
		star = FileLoader.loadImage("/star.png");
		sadEmoji = FileLoader.loadImage("/0-star.png");
		happyEmoji = FileLoader.loadImage("/1-star.png");
		smileEmoji = FileLoader.loadImage("/2-stars.png");
		starEmoji = FileLoader.loadImage("/3-stars.png");
		
		buttonBackToMenu = new MyButton(game, backToMenu, 580, 520, 40, 40);
		buttonSpeaker = new MyButton(game, speaker.getSubimage(0, 0, 512, 512), 580, 440, 40, 40);
		buttonPause = new MyButton(game, pause.getSubimage(0, 0, 512, 512), 580, 360, 40, 40);
		buttonNext = new MyButton(game, next, 580, 360, 56, 40);
		
		buttons.add(buttonBackToMenu);
		buttons.add(buttonPause);
		buttons.add(buttonSpeaker);

		background = FileLoader.loadImage("/bg.jpg");
	}

	@Override
	public void update() {
		if (!InGame.isGameOver() && !isPause) {
			try { 
				inGame.update();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (InGame.isGameOver() && !isPlayAgain && !game.getWindow().isMute()) {
			MenuScreen.getMusic().stop();
			if (!isFailed && winLoop == 0) {
				winLoop++;
				win = FileLoader.LoadSound("/win.wav");
				win.loop(0);
			}
			else if (isFailed && loseLoop == 0) {
				loseLoop++;
				lose = FileLoader.LoadSound("/lose.wav");
				lose.loop(0);
			}
		}
		if (buttons != null) {
			for (MyButton button: buttons) {
				button.update();
			}
		}
		if (buttonSpeaker.isMouseUp()) {
			game.getWindow().setMute(!game.getWindow().isMute());
			buttonSpeaker.setImage(speaker.getSubimage(512 * (game.getWindow().isMute() ? 1 : 0), 0, 512, 512));
			if (game.getWindow().isMute()) {
				winLoop++;
				loseLoop++;
				MenuScreen.getMusic().stop();
				if (!isFailed && InGame.isGameOver() && !isPlayAgain && win != null)
					win.stop();
				else if (isFailed && InGame.isGameOver() && !isPlayAgain && lose != null)
					lose.stop();
			}
			else {
				MenuScreen.getMusic().start();
				MenuScreen.getMusic().loop(Clip.LOOP_CONTINUOUSLY);
				if (InGame.isGameOver() && !isPlayAgain) {
					MenuScreen.getMusic().stop();
					if (!isFailed && winLoop == 0) {
						winLoop++;
						win = FileLoader.LoadSound("/win.wav");
						win.loop(0);
					}
					else if (isFailed && loseLoop == 0) {
						loseLoop++;
						lose = FileLoader.LoadSound("/lose.wav");
						lose.loop(0);
					}
				}
			}
		}
		if (buttonPause.isMouseUp()) {
			if (!InGame.isGameOver()) {
				isPause = !isPause;
				buttonPause.setImage(pause.getSubimage(512 * (isPause ? 1 : 0), 0, 512, 512));
			} 
			else {
				if (stage == 9)
					stage = -1;
				Clock clock = new Clock();
				Thread clockThread = new Thread(clock);
				clockThread.start();
				setNewGame(level);
			}
		}
		if (buttonNext.isMouseUp() && nextStage) {
			nextStage = false;
			if (stage < 10) {
				stage++;			
				setNewGame(level);
			}
			else if (stage == 10) 
				inGame.setGameOver(true);
		}
		if (buttonBackToMenu.isMouseUp()) {
			if (!InGame.isGameOver()) 
				isPause = true;
			else {
				newClock = true;
				if (!MenuScreen.getMusic().isRunning() && !game.getWindow().isMute())
					MenuScreen.getMusic().start();
			}
			game.getWindow().setScreen(gui.Window.Screen.Menu);
		}
	}
								
	@Override
	public void paint(Graphics g) {
		int width = game.getWindow().getCanvas().getWidth();
		int height = game.getWindow().getCanvas().getHeight();
		g.drawImage(background, 0, 0, width, height, null);
		
		// Draw board
		Graphics2D g2d = (Graphics2D) g;
		int boardWidth = inGame.getBoard().getWidth();
		int boardHeight = inGame.getBoard().getHeight();
		int blockSize = inGame.getBlockSize();
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(new Color(255, 255, 255,80));		
		for (int i = 0; i <= boardHeight; i++) { 
			g2d.drawLine(200, i * blockSize, boardWidth * blockSize + 200, i * blockSize);
		}
		for (int j = 0; j <= boardWidth; j++) {
			g2d.drawLine(j * blockSize + 200, 0, j * blockSize + 200, boardHeight * blockSize);
		}

		// Display group name
		g.setFont(new Font("Chopsic", Font.PLAIN, 15));    
		g.setColor(Color.WHITE);
		g.drawString("Group:\nLone Wolves", 20, 30);

		// Display line and stage
		int line = inGame.getBoard().getCount();
		g.setColor(Color.WHITE);
		g.setFont(new Font("Chopsic", Font.PLAIN, 30));    
		if (MenuScreen.getMode() != 3)
				g.drawString("\nLINE: " + line, 20, 200);
		else {
			if (stage < 10) {
				if (stage < 0)
					stage = 0;
				g.drawString("\nLINE: " + line + "/" +  listOfLines[stage], 20, 200);
				g.setFont(new Font("Chopsic", Font.PLAIN, 30));    
				g.setColor(Color.WHITE);
				g.drawString("STAGE " + (stage + 1), 20, 130);	
			}
		}

		// Display score and combo
		int score = inGame.getScore();
		g.setFont(new Font("Chopsic", Font.PLAIN, 30));  
		g.setColor(Color.WHITE);
		if (Board.getCombo() == 0 && count == 0)
			combo = 0;
		else if (Board.getCombo() >= 1 && count > 0) {
			if (!game.getWindow().isMute()) {
				clear = FileLoader.LoadSound("/clear-line.wav");
				clear.loop(0);
			}
			combo = Board.getCombo();
		}
	
		g.drawString("COMBO", 20, 300);
		g.drawString("SCORE", 20, 390);
		g.setColor(Color.RED);
		g.setFont(new Font("Chopsic", Font.PLAIN, 30));  
		g.drawString("X" + combo, 20, 330);
		g.drawString(score + "", 20, 420);   
		Board.setCombo(0);

		// Display timer
		if (minuteStarter == 0 && secondStarter <= 10 && MenuScreen.getMode() == 2)
			g.setColor(Color.RED);
		else
			g.setColor(Color.decode("#0fff6b"));
		g.setFont(new Font("Chopsic", Font.BOLD, 35));
		g.drawString("TIME", 20, 520);
		g.setFont(new Font("Digital Display", Font.BOLD, 50));
		
		// Clock settings
		if (secondStarter == 60) {
			secondStarter = 0;
			if (MenuScreen.getMode() != 2)
				minuteStarter++;
			else
				minuteStarter--;
		}
		if (minuteStarter > 0 && MenuScreen.getMode() == 2) {
			if (secondStarter < 0) {
				secondStarter = 59;
				minuteStarter--; 
			}
		}
		else if (minuteStarter == 0 && secondStarter == 0 && MenuScreen.getMode() == 2) {
			Clock.suspend();
			inGame.setGameOver(true);
		}
		if (minuteStarter <= 9)
			g.drawString("0" + minuteStarter + ":", 20, 570);
		else
			g.drawString(minuteStarter + ":", 20, 570);
		if (secondStarter <= 9)
			g.drawString("0" + secondStarter, 100, 570);
		else
			g.drawString(secondStarter + "", 100, 570);
		
		// Display "NEXT"
		g.setFont(new Font("Chopsic", Font.PLAIN, 35));
		g.setColor(Color.WHITE);
		g.drawString("NEXT", game.getWidth() - 160, 50);

		// Display level
		g.drawString("LEVEL  " + level, game.getWidth() - 160, 280);
		
		// Display block and next block
		inGame.paint(g);

		if (MenuScreen.getMode() == 3 && line >= listOfLines[stage]) {
			inGame.setGameOver(true);
			nextStage = true;
		}

		// Display result
		if (!InGame.isGameOver()) {
			buttonPause.setImage(pause.getSubimage(512 * (isPause ? 1 : 0), 0, 512, 512));
			if (isPause)
				g.drawImage(pauseScreen, 198, 0, 306, 600, null);
		}
		else {
			int emoji = 0;
			GameScreen.isPlayAgain = false;
			int time = minuteStarter * 60 + secondStarter;
			if (MenuScreen.getMode() == 1) {
				buttonPause.setImage(playAgain);
				if (line == 0) 
					isFailed = true;
				else {
					isFailed = false;
					g.drawImage(result, 200, 0, 300, 600, null);
					g.drawImage(success, 193, 120, 314, 360, null);
					g.drawImage(star, 228, 213, 70, 70, null);
					emoji = 1;
					if (line > 20) {
						g.drawImage(star, 316, 187, 70, 70, null);
						emoji = 2;
					}
					if (line > 50) {
						g.drawImage(star, 401, 213, 70, 70, null);
						emoji = 3;
					}
				}
			}
			else if (MenuScreen.getMode() == 2) {
				buttonPause.setImage(playAgain);
				if (line == 0 || (minuteStarter == 0 && secondStarter > 0)) 
					isFailed = true;
				else {
					isFailed = false;
					g.drawImage(result, 200, 0, 300, 600, null);
					g.drawImage(success, 193, 120, 314, 360, null);
					g.drawImage(star, 228, 213, 70, 70, null);
					emoji = 1;
					if (line > 2) {
						g.drawImage(star, 316, 187, 70, 70, null);
						emoji = 2;
					}
					if (line > 3) {
						g.drawImage(star, 401, 213, 70, 70, null);
						emoji = 3;
					}
				}
			}
			else if (MenuScreen.getMode() == 3 && stage < 10) {
				if (line >= listOfLines[stage]) {
					isFailed = false;
					if (stage < 9)
						buttonPause.setImage(next);
					else
						buttonPause.setImage(playAgain); 
					g.drawImage(result, 200, 0, 300, 600, null);
					g.drawImage(success, 193, 120, 314, 360, null);
					g.drawImage(star, 228, 213, 70, 70, null);
					emoji = 1;
					if ((line * 60) / time >= 2) {
						g.drawImage(star, 316, 187, 70, 70, null);
						emoji = 2;
					}
					if ((line * 60) / time >= 3) {
						g.drawImage(star, 401, 213, 70, 70, null);
						emoji = 3;
					}
				}
				else {
					buttonPause.setImage(playAgain);
					isFailed = true;
				}
			}
			if (isFailed) {
				g.drawImage(result, 200, 0, 300, 600, null);
				g.drawImage(failed, 193, 120, 314, 360, null);
				g.drawImage(sadEmoji, 300, 310, 100, 100, null);
			}
			else {
				if (emoji == 1)
					g.drawImage(happyEmoji, 300, 310, 100, 100, null);
				else if (emoji == 2)
					g.drawImage(smileEmoji, 300, 310, 100, 100, null);
				else if (emoji == 3)
					g.drawImage(starEmoji, 300, 310, 100, 100, null);
			}
			Clock.suspend();

			String path = "Tetris\\source\\main\\data.txt";
			String scoreString = "", lineString = "", timeString = "";
			try {
				File file = new File(path);
				List<String> listOfData = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
				int size = listOfData.size();
				
				if (size == 0) {
					scoreString = score + "";
					lineString = line + "";
					timeString = time + "";
				}
				else {
					if (score > Integer.parseInt(listOfData.get(0)))
						scoreString = score + "";
					else
						scoreString = listOfData.get(0);
					if (line > Integer.parseInt(listOfData.get(1)))
						lineString = line + "";
					else
						lineString = listOfData.get(1);
					if (time > Integer.parseInt(listOfData.get(2)))
						timeString = time + "";
					else
						timeString = listOfData.get(2);
				}
				PrintWriter pwriter = new PrintWriter(path);
				pwriter.println(scoreString);
				pwriter.println(lineString);
				pwriter.println(timeString);
				pwriter.flush();
				pwriter.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
		buttonSpeaker.setImage(speaker.getSubimage(512 * (game.getWindow().isMute() ? 1 : 0), 0, 512, 512));
		
		if (buttons != null) {
			for (MyButton button: buttons) {
				button.paint(g);
			}
		}
		count++;
	}

	public void setNewGame(int level) {
		inGame.setGameOver(false);
		if (!isPlayAgain) {
			isPlayAgain = true;
		}
		if (!game.getWindow().isMute()) {
			MenuScreen.getMusic().start();
			MenuScreen.getMusic().loop(Clip.LOOP_CONTINUOUSLY);
		}
		this.level = level;
		isPause = false;
		count = 0;
		if (MenuScreen.getMode() == 2) 
			minuteStarter = 1;
		else
			minuteStarter = 0;
		secondStarter = 0;
		winLoop = 0;
		loseLoop = 0;
		inGame = new InGame(this, level);
	}

	public static int decreaseSeconds() {
		return secondStarter--;
	}

	public static int increaseSeconds() {
		return secondStarter++;
	}

	public static boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		GameScreen.isPause = isPause;
	}

	public static boolean playAgain() {
		return isPlayAgain;
	}

	public static boolean isNewClock() {
		return newClock;
	}

	public static void setStage(int stage) {
		GameScreen.stage = stage;
	}
}
