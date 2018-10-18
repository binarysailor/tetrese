package binarysailor.tetrese.model;

public class GameLifecycle {
    public enum State {
        PLAYING, GAME_OVER, MENU
    };

    private State state = State.PLAYING;

    public State getState() {
        return state;
    }

    void gameOver() {
        state = State.GAME_OVER;
    }

    void restartGame() {
        state = State.PLAYING;
    }

    void openMenu() {
        state = State.MENU;
    }
}
