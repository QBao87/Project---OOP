package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Clip;

import engine.Clock;
import engine.FileLoader;
import engine.Game;
import gui.MyButton;
import gui.Window;

public class MenuScreen extends Screen {

	private BufferedImage loading;
	private BufferedImage background;
	private BufferedImage tetris;
	private BufferedImage records;
	private BufferedImage speaker;
	private BufferedImage guide1;
	private BufferedImage guide2;
	private BufferedImage guide3;
	private BufferedImage guide4;
	private BufferedImage guide5;
	private BufferedImage guide6;
	private BufferedImage guide7;
	private BufferedImage guide8;
	private BufferedImage guide9;
	private BufferedImage quitPopup;
	private BufferedImage selectMode;
	private BufferedImage selectLevel;
	
	private ArrayList<MyButton> buttons = new ArrayList<MyButton>();
	private static Clip music;

	private MyButton buttonRecords;
	private MyButton buttonCancelRecords;
	private MyButton buttonSpeaker;
	private MyButton buttonNewGame;
	private MyButton buttonContinue;
	private MyButton buttonHowToPlay;
	private MyButton buttonQuit;
	private MyButton buttonExit;
	private MyButton buttonCancelExit;
	private MyButton buttonCasual;
	private MyButton buttonMarathon;
	private MyButton buttonAdventure;
	private MyButton buttonCancelMode;
	private MyButton buttonLeft;
	private MyButton buttonRight;
	private MyButton buttonBackward;
	private MyButton buttonForward;
	private MyButton buttonOK;
	private MyButton buttonCancelLevel;
	public static GameScreen gameScreen;

	private int percent = 0;
	private int guide = 0;
	private int level = 1;
	private static int mode = 0;     // [0: undefined, 1: casual, 2: marathon, 3: adventure]
	private int count = 0;

	private boolean isRecords = false;
	private boolean isGuide = false;
	private boolean isSelectLevel = false;
	private boolean isSelectMode = false;
	private boolean isExit = false;
	private int width;
	private int height;

	public MenuScreen(Game game) {
		super(game);
		loading = FileLoader.loadImage("/home-screen.png");
		background = FileLoader.loadImage("/home-screen.png");
		tetris = FileLoader.loadImage("/tetris.png");
		records = FileLoader.loadImage("/records.png");
		speaker = FileLoader.loadImage("/speak-mute.png");
		guide1 = FileLoader.loadImage("/guide-1.png");
		guide2 = FileLoader.loadImage("/guide-2.png");
		guide3 = FileLoader.loadImage("/guide-3.png");
		guide4 = FileLoader.loadImage("/guide-4.png");
		guide5 = FileLoader.loadImage("/guide-5.png");
		guide6 = FileLoader.loadImage("/guide-6.png");
		guide7 = FileLoader.loadImage("/guide-7.png");
		guide8 = FileLoader.loadImage("/guide-8.png");
		guide9 = FileLoader.loadImage("/guide-9.png");
		quitPopup = FileLoader.loadImage("/quit-popup.png");
		selectMode = FileLoader.loadImage("/select-mode.png");
		selectLevel = FileLoader.loadImage("/level.png");

		buttonRecords = new MyButton(game, FileLoader.loadImage("/leaderboard.png"), 20, 20, 78, 50);
		buttonCancelRecords = new MyButton(game, FileLoader.loadImage("/1.png"), 243, 487, 211, 68);
		buttonSpeaker = new MyButton(game, speaker.getSubimage(0, 0, 512, 512), 635, 30, 40, 40);
		buttonContinue = new MyButton(game, FileLoader.loadImage("/4.png"), 177, 100, 345, 100);
		buttonNewGame = new MyButton(game, FileLoader.loadImage("/11.png"), 177, 200, 345, 100);
		buttonHowToPlay = new MyButton(game, FileLoader.loadImage("/8.png"), 177, 300, 345, 100);
		buttonQuit = new MyButton(game, FileLoader.loadImage("/12.png"), 177, 395, 345, 100);
		buttonExit = new MyButton(game, FileLoader.loadImage("/6.png"), 140, 338, 200, 80);
		buttonCancelExit = new MyButton(game, FileLoader.loadImage("/1.png"), 361, 338, 200, 80);
		buttonCasual = new MyButton(game, FileLoader.loadImage("/2.png"), 110, 325, 180, 58);
		buttonMarathon = new MyButton(game, FileLoader.loadImage("/10.png"), 265, 325, 180, 58);
		buttonAdventure = new MyButton(game, FileLoader.loadImage("/9.png"), 418, 325, 180, 58);
		buttonCancelMode = new MyButton(game, FileLoader.loadImage("/1.png"), 260, 390, 180, 65);
		buttonLeft = new MyButton(game, FileLoader.loadImage("/backward.png"), 130, 355, 35, 35);
		buttonRight = new MyButton(game, FileLoader.loadImage("/forward.png"), 534, 355, 35, 35);
		buttonBackward = new MyButton(game, FileLoader.loadImage("/backward.png"), 240, 358, 35, 35);
		buttonForward = new MyButton(game, FileLoader.loadImage("/forward.png"), 423, 358, 35, 35);
		buttonOK = new MyButton(game, FileLoader.loadImage("/OK.png"), 317, 323, 66, 66);
		buttonCancelLevel = new MyButton(game, FileLoader.loadImage("/1.png"), 264, 420, 165, 68);

		buttons.add(buttonContinue);
		buttons.add(buttonNewGame);
		buttons.add(buttonHowToPlay);
		buttons.add(buttonQuit);
		buttons.add(buttonSpeaker);

		Window.bar.setVisible(true);
		gameScreen = new GameScreen(Window.game);
		Thread gameThread = new Thread(gameScreen);
		gameThread.start();
	}

	@Override
	public void update() {
		if (percent == 100) {
			count++;
			if (count == 1) {
				music = FileLoader.LoadSound("/music.wav");
				music.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		// Mode selection
		if (isSelectMode) {
			if (buttonCasual.isMouseUp() || buttonMarathon.isMouseUp() || buttonAdventure.isMouseUp()) {
				isSelectMode = false;
				isSelectLevel = true;
				if (buttonCasual.isMouseUp())
					mode = 1;
				else if (buttonMarathon.isMouseUp())
					mode = 2;
				else if (buttonAdventure.isMouseUp())
					mode = 3;
			}
			else if (buttonCancelMode.isMouseUp()) {
				isSelectMode = false;
			}
		}
		else if (isSelectLevel) {
			if (buttonOK.isMouseUp()) {
				gameScreen.setNewGame(level);
				game.getWindow().setScreen(gui.Window.Screen.Game);
				if (GameScreen.isNewClock()) {
					Clock clock = new Clock();
					Thread clockThread = new Thread(clock); 
					clockThread.start();
				}
				isSelectLevel = false;
				if (mode == 3)
					GameScreen.setStage(0);
			}
			else if (buttonCancelLevel.isMouseUp()) {
				isSelectLevel = false;
				isSelectMode = true;
			}
			else if (buttonBackward.isMouseUp()) {
				level--;
				if (level == 0)
					level = 9;
			}
			else if (buttonForward.isMouseUp()) {
				level++;
				if (level == 10)
					level = 1; 
			}
		} 
		else if (isGuide) {
			if (buttonCancelMode.isMouseUp()) 
				isGuide = false;
			if (buttonRight.isMouseUp())
				guide++;
			else if (buttonLeft.isMouseUp())
				guide--;
			if (guide < 0)
				guide = 0;
			else if (guide > 8)
				guide = 8;
		}
		else if (isExit) {
			if (buttonExit.isMouseUp())
				System.exit(0);
			if (buttonCancelExit.isMouseUp())
				isExit = false;
		}
		else if (isRecords) {
			if (buttonCancelRecords.isMouseUp())
				isRecords = false;
		}
		else {
			if (buttons != null) {
				for (MyButton button: buttons) {
					if (button == buttonContinue) {
						if (GameScreen.isPause()) {
							button.update();
						}
					} 
					else {
						button.update();
					}				
				}
			}
			if (buttonQuit.isMouseUp()) {
				isExit = true;
			}
			if (buttonHowToPlay.isMouseUp()) {
				isGuide = true;
			}
			if (buttonNewGame.isMouseUp()) {
				isSelectMode = true;
			}
			if (buttonContinue.isMouseUp() && GameScreen.isPause()) {
				gameScreen.setPause(false);
				game.getWindow().setScreen(gui.Window.Screen.Game);
			}
			if (buttonSpeaker.isMouseUp()) {
				game.getWindow().setMute(!game.getWindow().isMute());
				buttonSpeaker.setImage(speaker.getSubimage(512 * (game.getWindow().isMute() ? 1 : 0), 0, 512, 512));
				if (game.getWindow().isMute()) {
					music.stop();
				}
				else {
					music.start();
					music.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
			if (buttonRecords.isMouseUp()) {
				isRecords = true;
			}		
		}
	}

	@Override
	public void paint(Graphics g) {
		width = game.getWindow().getCanvas().getWidth();
		height = game.getWindow().getCanvas().getHeight();

		if (percent <= 100) {
			g.drawImage(loading, 0, 0, width, height - 15, null);
			g.setColor(Color.CYAN);
			g.setFont(new Font("My Girl Is Retro", Font.PLAIN, 15));
			try {
				Thread.sleep(90);
				percent++;
				Window.bar.setValue(percent);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if (percent == 100) {
				try {
					Thread.sleep(500);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			Window.bar.setVisible(false);
			g.drawImage(background, 0, 0, width, height, null);

			if (isSelectMode) {
				g.drawImage(selectMode, 12, 30, 670, 440, null);
				buttonCasual.paint(g);
				buttonMarathon.paint(g);
				buttonAdventure.paint(g);
				buttonCancelMode.paint(g);
			}
			else if (isSelectLevel) {
				g.drawImage(selectLevel, 182, 26, 336, 468, null);
				buttonBackward.paint(g);
				buttonForward.paint(g);
				buttonOK.paint(g);
				buttonCancelLevel.paint(g);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Game Battles 2", Font.BOLD, 70));
				g.drawString("" + level, 330, 229);
			} 
			else if (isGuide) {
				BufferedImage[] listOfGuides = {guide1, guide2, guide3, guide4, guide5, guide6, guide7, guide8, guide9};
				g.drawImage(listOfGuides[guide], 50, 70, 590, 400, null);
				buttonCancelMode.paint(g);
				buttonLeft.paint(g);
				buttonRight.paint(g);
			}
			else if (isRecords) {
				String score = "", line = "";
				int  minute = 0, second = 0;
				String stringMinute = "", stringSecond = "";
				g.drawImage(records, 140, 15, 420, 555, null);
				buttonCancelRecords.paint(g);
				File file = new File("Tetris\\source\\main\\data.txt");
				try {
					List<String> listOfData = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
					if (listOfData.size() > 0) {
						score = listOfData.get(0);
						line = listOfData.get(1);
						minute = Integer.parseInt(listOfData.get(2)) / 60;
						second = Integer.parseInt(listOfData.get(2)) - minute * 60;
					}
					else {
						score = "0";
						line = "0";
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
				g.setFont(new Font("Friz Quadrata Std", Font.BOLD, 45));
				g.setColor(Color.decode("#00FF00"));
				g.drawString(score, 330, 215);
				g.setColor(Color.decode("#FF00FF"));
				g.drawString(line, 330, 322);
				if (minute <= 9)
					stringMinute = "0" + minute;
				else
					stringMinute = minute + "";
				if (second <= 9)
					stringSecond = "0" + second;
				else
					stringSecond = second + "";
				g.setColor(Color.decode("#FF0000"));
				g.drawString(stringMinute + ":" + stringSecond, 330, 430);
			}
			else if (isExit) {
				g.drawImage(quitPopup, 115, 80, 470, 390, null);
				buttonExit.paint(g);
				buttonCancelExit.paint(g);
			}
			else {
				buttonRecords.paint(g);
				buttonSpeaker.setImage(speaker.getSubimage(512 * (game.getWindow().isMute() ? 1 : 0), 0, 512, 512));
				if (buttons != null) {
					for (MyButton button: buttons) {
						if (button == buttonContinue) {
							if (GameScreen.isPause()) 
								button.paint(g);
							else
								g.drawImage(tetris, 210, 70, 312, 86, null);
						} 
						else {
							button.paint(g);
						}
					}
				}
			}
		}	
	}

	public static Clip getMusic() {
		return MenuScreen.music;
	}

	public static int getMode() {
		return MenuScreen.mode;
	}
}
