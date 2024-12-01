package engine;

import gameplay.InGame;
import screen.GameScreen;
import screen.MenuScreen;

public class Clock implements Runnable {

    public static boolean pause = false;

    @Override
    public void run() {
        if (GameScreen.playAgain()) 
            resume();

        while (!InGame.isGameOver()) {
            if (!GameScreen.isPause()) {
                if (MenuScreen.getMode() == 2) 
                GameScreen.decreaseSeconds();
                else
                    GameScreen.increaseSeconds();
                
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (GameScreen.isPause()) {
                suspend();
            }
        }
    }

    public static synchronized void suspend() {
        pause = true;
    }

    public static synchronized void resume() {
        pause = false;
    }
}
