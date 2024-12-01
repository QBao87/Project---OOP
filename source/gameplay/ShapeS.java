package gameplay;

public class ShapeS extends Shape{

	public ShapeS(InGame inGame, int color) {
		super(inGame, color);
		maxSize = 2;
		blocks.get(0).setLocal(4, -2);
		blocks.get(1).setLocal(5, -2);
		blocks.get(2).setLocal(5, -1);
		blocks.get(3).setLocal(6, -1);
		x = 4;
		y = -2;
	}

}
