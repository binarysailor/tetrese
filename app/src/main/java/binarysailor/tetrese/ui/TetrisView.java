package binarysailor.tetrese.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import binarysailor.tetrese.model.BlockFactory;
import binarysailor.tetrese.model.Board;

public class TetrisView extends SurfaceView implements SurfaceHolder.Callback {

    private TheThread theThread;

    public TetrisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        theThread = new TheThread(holder, context);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        theThread.setRunning(true);
        theThread.createBoard(surfaceHolder.getSurfaceFrame());
        setOnTouchListener(new TouchListener(theThread.board));
        theThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        theThread.surfaceChanged(format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        theThread.setRunning(false);
        try {
            theThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class TheThread extends Thread {

        SurfaceHolder surfaceHolder;
        Context context;

        private volatile boolean running;

        private Paint backgroundPaint;

        private Board board;

        private Integer width, height;

        private CanvasGridAdapter grid;

        TheThread(SurfaceHolder surfaceHolder, Context context) {
            this.surfaceHolder = surfaceHolder;
            this.context = context;
            this.backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 60, 60, 90);
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        board.update();
                        draw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        void draw(Canvas canvas) {
            canvas.drawRect(canvas.getClipBounds(), backgroundPaint);
            grid.setCanvas(canvas);
            synchronized (board) {
                board.getFallingBlock().draw(grid);
                board.getFallenBlocks().forEach(b -> b.draw(grid));
            }
        }

        void setRunning(boolean running) {
            this.running = running;
        }

        void surfaceChanged(int format, int width, int height) {
            Log.d("Tetris", String.format("surfaceChanged: %d, %d, %d", format, width, height ));
            this.width = width;
            this.height = height;
        }

        public void createBoard(Rect surfaceFrame) {
            int widthCells = 18;
            int cellSize = surfaceFrame.width() / widthCells;
            int heightCells = Math.min(surfaceFrame.height() / cellSize, 16 * widthCells / 9);
            board = new Board(widthCells, heightCells, new BlockFactory());
            grid = new CanvasGridAdapter(board);
            board.createFallingBlock();
        }
    }


}
