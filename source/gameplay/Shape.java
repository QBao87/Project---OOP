package gameplay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Shape {
	
	protected InGame inGame;
	
	protected List<Block> blocks = new ArrayList<Block>();
	
	protected int color;
	protected int x, y;
	protected int maxSize;
	
	protected int normal = 500, fast = 30;
	protected int delay;
	
	protected long now, lastTime;
	
	protected boolean collision = false;
	
	public Shape(InGame inGame, int color) {
		this.color = color;
		this.inGame = inGame;	
		for (int i = 0; i < 4; i++) {
			blocks.add(new Block(color));			
		}
		
		normal = 500 - (int) (500 * 0.1 * (inGame.getLevel() - 1));
		delay = normal;
		now = System.currentTimeMillis();
		lastTime = now;
	}
	
	public Shape(InGame inGame, int color, List<Block> blocks) {
		this(inGame, color);
		this.blocks = blocks;
	}
	
	public void update() {
		now = System.currentTimeMillis();
		for (Block block: blocks) {
			if ((block.getY() >= inGame.getBoard().getHeight() - 1)) {
				if (now - lastTime > delay / 2) {
					collision = true;
					break;
				}
			}
			if (color != 9) {
				for (Block otherBlock: inGame.getBoard().getBlocks()) {
					if (block.getX() == otherBlock.getX() && block.getY() + 1 == otherBlock.getY()) {
						if (now - lastTime > delay / 2) {
							collision = true;
							break;
						}
					}
				}
			}			
		}
		if (now - lastTime > delay && !collision) {
			lastTime = now;
			y++;
			for (Block block: blocks) {
				block.setLocal(block.getX(), block.getY() + 1);
			}
		}
		if (color == 9) {
			for (Block block: blocks) {
				Random rand = new Random();
				block.setColor(rand.nextInt(9));
			}
		}
	}
	
	public void paint(Graphics g, boolean isCurrentShape) {
		for (Block block: blocks) {
			block.paint(g, isCurrentShape);
		}
	}	
	
	public void rotateShape() {
		ArrayList<Block> tempBlocks = new ArrayList<Block>();
		for (Block block: blocks) {
			tempBlocks.add(new Block(block.getColor(), block.getX() - x, block.getY() - y));
		}		
		for (Block block: tempBlocks) {
			block.setLocal(block.getY(), block.getX());			
			block.setLocal(block.getX(), maxSize - block.getY());
			block.setLocal(block.getX() + x, block.getY() + y);
		}
		boolean flag = true;
		int temp = 0;
		for (Block block: tempBlocks) {
			int abs = 0;   // abscissa
			if (block.getY() >= inGame.getBoard().getHeight()) {
				flag = false;
				break;
			}
			if (block.getX() < 0) {
				abs = 0 - block.getX();
			}				
			if (block.getX() >= inGame.getBoard().getWidth()) {
				abs = inGame.getBoard().getWidth() - block.getX() - 1;
			}
			if (Math.abs(abs) > Math.abs(temp)) {
				temp = abs;
			}
		}
		for (Block block: tempBlocks) {
			block.setLocal(block.getX() + temp, block.getY());
		}
		x += temp;
		if (color != 9) {
			for (Block block: tempBlocks) {
				for (Block otherBlock : inGame.getBoard().getBlocks()) {
					if (block.getX() == otherBlock.getX() && block.getY() == otherBlock.getY()) {
						flag = false;
						break;
					}
				}
			}
		}		
		if (flag) {
			blocks = tempBlocks;
		}
	}
	
	public void moveLeft() {
		moveX(-1);
	}
	
	public void moveRight() {
		moveX(1);
	}
	
	private void moveX(int delta) {
		ArrayList<Block> tempBlocks = new ArrayList<Block>();
		for (Block block: blocks) {
			tempBlocks.add(new Block(block.getColor(), block.getX(), block.getY()));
		}
		for (Block block: tempBlocks) {
			block.setLocal(block.getX() + delta, block.getY());
		}
		boolean flag = true;
		for (Block block: tempBlocks) {
			if (block.getX() < 0 || block.getX() >= inGame.getBoard().getWidth()) {
				flag = false;
				break;
			}
			if (color != 9) {
				for (Block otherBlock : inGame.getBoard().getBlocks()) {
					if (block.getX() == otherBlock.getX() && block.getY() == otherBlock.getY()) {
						flag = false;
						break;
					}
				}
			}			
		}
		if (flag) {
			blocks = tempBlocks;
			x += delta;
		}
	}

	private void moveY(int delta) {     // future update
		ArrayList<Block> tempBlocks = new ArrayList<Block>();
		for (Block block: blocks) {
			tempBlocks.add(new Block(block.getColor(), block.getX(), block.getY()));
		}
		for (Block block: tempBlocks) {
			block.setLocal(block.getX(), block.getY() + delta);
		}
		boolean flag = true;
		for (Block block: tempBlocks) {
			if (block.getY() < 0 || block.getY() >= inGame.getBoard().getHeight()) {
				flag = false;
				break;
			}
			if (color != 9) {
				for (Block otherBlock : inGame.getBoard().getBlocks()) {
					if (block.getX() == otherBlock.getX() && block.getY() == otherBlock.getY()) {
						flag = false;
						break;
					}
				}
			}			
		}
		if (flag) {
			blocks = tempBlocks;
			y += delta;
		}
	}

	public void teleport() {
		delay = 0;
	}

	public void speedUp() {
		delay = fast;
	}
	
	public void speedDown() {
		delay = normal;
	}

	public int getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isCollision() {
		return collision;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public void setColor(int color) {
		this.color = color;
	}

}