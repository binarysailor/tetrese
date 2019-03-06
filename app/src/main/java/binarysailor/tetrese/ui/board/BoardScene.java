package binarysailor.tetrese.ui.board;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import binarysailor.tetrese.model.BlockFactory;
import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.GameLifecycle;
import binarysailor.tetrese.ui.Dimensions;
import binarysailor.tetrese.ui.Scene;

public class BoardScene implements Scene, GameLifecycle.GameLifecycleListener {
    private final Board board;
    private final GameLifecycle lifecycle;
    private BoardRenderer boardRenderer;
    private GameOverRenderer gameOverRenderer;
    private View.OnTouchListener touchListener;

    public BoardScene(Dimensions dimensions, GameLifecycle lifecycle) {
        this.lifecycle = lifecycle;
        board = new Board(
                dimensions.getWidthCells(), dimensions.getHeightCells(), new BlockFactory(), lifecycle);
        boardRenderer = new BoardRenderer(board, dimensions);
        gameOverRenderer = new GameOverRenderer();
        touchListener = new BoardTouchListener(this, board, lifecycle);
        lifecycle.registerListener(this);
    }
    @Override
    public void update() {
        board.update();
    }

    @Override
    public void draw(Canvas canvas) {
        boardRenderer.render(canvas);
        if (lifecycle.getState() == GameLifecycle.State.GAME_OVER) {
            gameOverRenderer.render(canvas);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return touchListener.onTouch(view, motionEvent);
    }

    boolean isCommunicationArea(float x, float y) {
        return boardRenderer.isCommunicationArea(x, y);
    }

    BoardQuarter resolveBoardQuarter(float x, float y) {
        return boardRenderer.resolveBoardQuarter(x, y);
    }

    @Override
    public void stateChanging(GameLifecycle.State from, GameLifecycle.State to) {
        if (to == GameLifecycle.State.PLAYING && from == GameLifecycle.State.MENU_NO_GAME) {
            board.initialize();
        }
    }
}
