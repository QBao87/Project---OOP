package gameplay;

public class ShapeI extends Shape{

	public ShapeI(InGame inGame, int color) {
		super(inGame, color);
		maxSize = 3;
		blocks.get(0).setLocal(3, -1);
		blocks.get(1).setLocal(4, -1);
		blocks.get(2).setLocal(5, -1);
		blocks.get(3).setLocal(6, -1);
		x = 3;
		y = -2;
	}

}
