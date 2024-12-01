package gameplay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class Board {
	
	private final int width = 10, height = 20;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	private int count = 0;
	private static int combo = 0;

	public void update() {

	}
	
	public void paint(Graphics g) {
		for (Block block: blocks) {
			block.paint(g, true);
		}
	}
	
	public void setShapeToBoard(Shape shape) {
		for (Block block: shape.getBlocks()) {
			boolean flag = true;
			for (Block otherBlock: blocks) {
				if (block.getX() == otherBlock.getX() && block.getY() == otherBlock.getY()) {
					flag = false;
					otherBlock.setColor(block.getColor());
				}
			}
			if (flag) {
				this.blocks.add(block);
			}
		}
	}
	
	public int checkLine() {
		int score = 0;
		int line[] = new int[20];
		for (int i = 0; i < line.length; i++) {
			line[i] = 0;
		}
		for (Block block: blocks) {
			if (block.getY() >= 0) {
				line[block.getY()]++;
			}
		}
		for (int i = 0; i < line.length; i++) {
			if (line[i] == 10) {
				score++;
				Board.combo++;
				this.count++;
				int j = i;
				blocks.removeIf(n -> n.getY() == j);   // clear completed line
				for (Block block: blocks) {
					if (block.getY() < i) {
						block.setLocal(block.getX(), block.getY() + 1);
					}
				}
			}
		}		
		return score;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static int getCombo() {
		return Board.combo;
	}

	public static void setCombo(int combo) {
		Board.combo = combo;
	}

}
