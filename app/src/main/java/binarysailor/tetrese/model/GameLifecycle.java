package binarysailor.tetrese.model;

import java.util.ArrayList;
import java.util.List;

public class GameLifecycle {
    public enum State {
        PLAYING, GAME_OVER, MENU_NO_GAME, MENU_PAUSED_GAME
    };

    public interface GameLifecycleListener {
        void stateChanging(State from, State to);
    }

    private State state;
    private final List<GameLifecycleListener> listeners = new ArrayList<>();

    public State getState() {
        return state;
    }

    void gameOver() {
        changeStateTo(State.GAME_OVER);
    }

    public void openMenu() {
        if (state == State.PLAYING) {
            changeStateTo(State.MENU_PAUSED_GAME);
        } else {
            changeStateTo(State.MENU_NO_GAME);
        }
    }

    public void playGame() {
        changeStateTo(State.PLAYING);
    }

    private void changeStateTo(State to) {
        notifyListeners(to);
        state = to;
    }
    public void registerListener(GameLifecycleListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(State newState) {
        listeners.forEach(listener -> listener.stateChanging(state, newState));
    }
}
