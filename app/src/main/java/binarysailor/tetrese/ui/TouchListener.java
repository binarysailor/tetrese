package binarysailor.tetrese.ui;

import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.GameLifecycle;

class TouchListener implements View.OnTouchListener {

    private final Board board;
    private final GameLifecycle lifecycle;

    public TouchListener(Board board, GameLifecycle lifecycle) {
        this.board = board;
        this.lifecycle = lifecycle;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (lifecycle.getState()) {
            case PLAYING:
                return onTouchPlaying(view, motionEvent);
            case GAME_OVER:
                return onTouchGameOver(view, motionEvent);
        }
        return true;
    }

    private boolean onTouchPlaying(View view, MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (motionEvent.getPointerCount() > 1) {
            return false;
        }

        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);

        float distLeft = x;
        float distRight = view.getWidth() - x;
        float distTop = y;

        if (distLeft < distRight) {
            if (distLeft < distTop) {
                board.tryMoveLeft();
            } else {
                board.tryRotateClockwise();
            }
        } else {
            if (distRight < distTop) {
                board.tryMoveRight();
            } else {
                board.tryRotateClockwise();
            }
        }

        return true;
    }

    private boolean onTouchGameOver(View view, MotionEvent motionEvent) {
        board.startGame();
        return true;
    }
}
