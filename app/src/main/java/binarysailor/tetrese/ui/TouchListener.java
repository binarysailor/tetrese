package binarysailor.tetrese.ui;

import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.GameLifecycle;

class TouchListener implements View.OnTouchListener {

    private final Board board;
    private final GameLifecycle lifecycle;
    private final TetreseView tetreseView;

    public TouchListener(TetreseView view, Board board, GameLifecycle lifecycle) {
        this.board = board;
        this.lifecycle = lifecycle;
        this.tetreseView = view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        switch (lifecycle.getState()) {
            case PLAYING:
                return onTouchPlaying(view, motionEvent);
            case GAME_OVER:
                return onTouchGameOver(view, motionEvent);
        }
        return true;
    }

    private boolean onTouchPlaying(View view, MotionEvent motionEvent) {

        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);

        if (tetreseView.isCommunicationArea(x, y)) {
            board.dropFallingBlock();
            return true;
        }

        BoardQuarter quarter = tetreseView.resolveBoardQuarter(x, y);
        switch (quarter) {
            case NORTH:
                board.tryRotateClockwise();
                break;
            case EAST:
                board.tryMoveRight();
                break;
            case WEST:
                board.tryMoveLeft();
        }

        return true;
    }

    private boolean onTouchGameOver(View view, MotionEvent motionEvent) {
        board.startGame();
        return true;
    }
}
