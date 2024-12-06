package gameplay;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import screen.GameScreen;

public class InGame {
	
	private GameScreen gameScreen;
	private Board board;	
	private Shape currentShape, nextShape;
	
	private ArrayList<Block> nextShapeBlocks = new ArrayList<Block>();
	
	private final int blockSize = 30;
	private static boolean gameOver = false;
	
	private int level;
	private long count;
	private int score;
	
	public InGame(GameScreen gameScreen, int level) {
		this.gameScreen = gameScreen;
		this.level = level;
		
		count = 0;
		score = 0;
		board = new Board();
		
		setNextShape();
		setCurrentShape();
	}

	public void update() throws InterruptedException {
		currentShape.update();
		if (currentShape.isCollision()) {
			board.setShapeToBoard(currentShape);     // keep incomplete lines
			score += board.checkLine() * (80 + level * 20) * Board.getCombo();     // score formula	
			checkGameOver();
			if (!gameOver) 
				setCurrentShape();
		}
		keyUpdate();
	}

	public void paint(Graphics g) {
		board.paint(g);
		currentShape.paint(g, true);
		
		if (nextShape.getColor() == 9) {
			for (Block block: nextShapeBlocks) {
				Random rand = new Random();
				block.setColor(rand.nextInt(9)); 
			}
		}		
		for (Block block: nextShapeBlocks) {
			block.paint(g, false);
		}	
	}
	
	private void keyUpdate() {
		if (gameScreen.getGame().getInput().isKeyDown(KeyEvent.VK_UP)) {
			currentShape.rotateShape();
		}
		if (gameScreen.getGame().getInput().isKeyDown(KeyEvent.VK_DOWN)) {
			currentShape.speedUp();
		}
		if (gameScreen.getGame().getInput().isKeyDown(KeyEvent.VK_SPACE)) {
			currentShape.teleport();
		}
		if (gameScreen.getGame().getInput().isKeyUp(KeyEvent.VK_DOWN)) {
			currentShape.speedDown();
		}
		if (gameScreen.getGame().getInput().isKeyHold(KeyEvent.VK_RIGHT)) {
			currentShape.moveRight();
		}
		if (gameScreen.getGame().getInput().isKeyHold(KeyEvent.VK_LEFT)) {
			currentShape.moveLeft();
		}
	}

	private void checkGameOver() {
		for (Block block: board.getBlocks()) {
			if (block.getY() == 0) {
				gameOver = true;
			}
		}
	}
	
	private void setCurrentShape() {
		currentShape = nextShape;
		currentShape.setLastTime(System.currentTimeMillis());
		setNextShape();
	}
	
	private void setNextShape() {
		Random rand = new Random();
		int index = rand.nextInt(7);
		switch (index) {
			case 0:
				nextShape = new ShapeO(this, rand.nextInt(9));			
				break;
			case 1:
				nextShape = new ShapeT(this, rand.nextInt(9));
				break;
			case 2:
				nextShape = new ShapeL(this, rand.nextInt(9));
				break;
			case 3:
				nextShape = new ShapeJ(this, rand.nextInt(9));
				break;
			case 4:
				nextShape = new ShapeS(this, rand.nextInt(9));
				break;
			case 5:
				nextShape = new ShapeZ(this, rand.nextInt(9));
				break;
			case 6:
				nextShape = new ShapeI(this, rand.nextInt(9));
				break;
			default:
				break;
		}
		count++;
		if (count % 10 == 0) {
			nextShape.setColor(9);
		}	
		nextShapeBlocks = new ArrayList<Block>();
		for (Block block: nextShape.getBlocks()) {
			nextShapeBlocks.add(new Block(block.getColor(), block.getX() - nextShape.getX() + 11, block.getY() - nextShape.getY() + 2));
		}
	}

	public Board getBoard() {
		return board;
	}

	public int getBlockSize() {
		return blockSize;
	}
	
	public static boolean isGameOver() {
		return gameOver;
	}
	
	public void setGameOver(boolean gameOver) {
		InGame.gameOver = gameOver;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevel() {
		return level;
	}

}
