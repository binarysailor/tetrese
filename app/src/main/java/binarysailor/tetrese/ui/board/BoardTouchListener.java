package binarysailor.tetrese.ui.board;

import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.GameLifecycle;

public class BoardTouchListener implements View.OnTouchListener {

    private final GameLifecycle lifecycle;
    private final BoardScene scene;
    private final Board board;

    public BoardTouchListener(BoardScene scene, Board board, GameLifecycle lifecycle) {
        this.scene = scene;
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
            default:
                return false;
        }
    }

    private boolean onTouchPlaying(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }

        float x = motionEvent.getX(0);
        float y = motionEvent.getY(0);

        if (scene.isCommunicationArea(x, y)) {
            board.dropFallingBlock();
            return true;
        }

        BoardQuarter quarter = scene.resolveBoardQuarter(x, y);
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
        lifecycle.openMenu();
        return true;
    }
}
