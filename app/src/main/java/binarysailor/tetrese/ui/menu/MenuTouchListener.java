package binarysailor.tetrese.ui.menu;

import android.view.MotionEvent;
import android.view.View;

import java.util.Optional;

import binarysailor.tetrese.model.GameLifecycle;

class MenuTouchListener implements View.OnTouchListener {

    private final GameLifecycle lifecycle;
    private final Menu menu;

    MenuTouchListener(Menu menu, GameLifecycle lifecycle) {
        this.menu = menu;
        this.lifecycle = lifecycle;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);

        Optional<MenuItem> itemTouched = menu.getItemAt((int) x, (int) y);
        itemTouched.ifPresent(item -> {
            MenuAction action = (MenuAction) item.getTag();
            switch (action) {
                case PLAY:
                    lifecycle.playGame();
                    break;
                case OPTIONS:
                    // TODO
            }
        });
        return true;
    }
}
