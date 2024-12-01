package main;

import engine.Game;
public class PlayTetris {

	public static void main(String[] args) {
		System.out.print("\033[H\033[2J");
		System.out.flush();
		Game game = new Game();
		game.start();
	}

}
