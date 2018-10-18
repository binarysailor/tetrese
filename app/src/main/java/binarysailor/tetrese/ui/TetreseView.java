package binarysailor.tetrese.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import binarysailor.tetrese.model.BlockFactory;
import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.GameLifecycle;

public class TetreseView extends SurfaceView implements SurfaceHolder.Callback {

    private final GameLifecycle lifecycle;
    private final TetreseThread tetreseThread;
    private Board board;
    private BoardRenderer boardRenderer;
    private GameOverRenderer gameOverRenderer;

    public TetreseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        lifecycle = new GameLifecycle();
        tetreseThread = new TetreseThread(holder);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initBoard(surfaceHolder.getSurfaceFrame());
        setOnTouchListener(new TouchListener(board, lifecycle));
        tetreseThread.setRunning(true);
        tetreseThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        tetreseThread.surfaceChanged(format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        tetreseThread.setRunning(false);
        try {
            tetreseThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateBoard() {
        board.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        switch (lifecycle.getState()) {
            case PLAYING:
                boardRenderer.render(canvas);
                break;
            case GAME_OVER:
                boardRenderer.render(canvas);
                gameOverRenderer.render(canvas);
                break;
        }
    }

    private void initBoard(Rect surfaceFrame) {
        Dimensions dimensions = Dimensions.calculate(surfaceFrame);

        board = new Board(
                dimensions.getWidthCells(), dimensions.getHeightCells(), new BlockFactory(), lifecycle);
        boardRenderer = new BoardRenderer(board, dimensions);
        gameOverRenderer = new GameOverRenderer();

        board.startGame();
    }

    class TetreseThread extends Thread {

        SurfaceHolder surfaceHolder;

        private volatile boolean running;

        TetreseThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        if (lifecycle.getState() == GameLifecycle.State.PLAYING) {
                            updateBoard();
                        }
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void setRunning(boolean running) {
            this.running = running;
        }

        void surfaceChanged(int format, int width, int height) {
            Log.d("Tetris", String.format("surfaceChanged: %d, %d, %d", format, width, height ));
        }

    }
}
