package binarysailor.tetrese.ui;

import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.model.Board;

class TouchListener implements View.OnTouchListener {

    Board board;

    public TouchListener(Board board) {
        this.board = board;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
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
}
