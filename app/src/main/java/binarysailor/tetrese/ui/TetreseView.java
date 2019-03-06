package binarysailor.tetrese.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import binarysailor.tetrese.model.GameLifecycle;
import binarysailor.tetrese.ui.board.BoardScene;
import binarysailor.tetrese.ui.menu.MenuScene;

public class TetreseView extends SurfaceView implements SurfaceHolder.Callback, GameLifecycle.GameLifecycleListener {

    private final GameLifecycle lifecycle;
    private final TetreseThread tetreseThread;

    private Scene boardScene, menuScene;
    private Scene currentScene;

    public TetreseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        lifecycle = new GameLifecycle();
        lifecycle.registerListener(this);

        tetreseThread = new TetreseThread(holder);

        setFocusable(true);
    }

    @Override
    public void stateChanging(GameLifecycle.State from, GameLifecycle.State to) {
        switch (to) {
            case MENU_NO_GAME:
            case MENU_PAUSED_GAME:
                currentScene = menuScene;
                break;
            case PLAYING:
                currentScene = boardScene;
                break;
            case GAME_OVER:
                currentScene = boardScene;
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initScenes(surfaceHolder.getSurfaceFrame());

        setOnTouchListener((view, motionEvent) -> {
            return currentScene.onTouch(view, motionEvent);
        });

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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        currentScene.draw(canvas);
    }

    private void initScenes(Rect surfaceFrame) {
        Dimensions dimensions = Dimensions.calculate(surfaceFrame);

        this.boardScene = new BoardScene(getResources(), dimensions, lifecycle);
        this.menuScene = new MenuScene(getResources(), dimensions, lifecycle);

        lifecycle.openMenu();
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
                        currentScene.update();
                        currentScene.draw(canvas);
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
