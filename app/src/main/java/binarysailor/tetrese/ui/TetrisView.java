package binarysailor.tetrese.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
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
        private Paint borderPaint;
        private Paint textPaint;

        private Dimensions dimensions;
        private Board board;

        private CanvasGridAdapter grid;

        TheThread(SurfaceHolder surfaceHolder, Context context) {
            this.surfaceHolder = surfaceHolder;
            this.context = context;
            backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 85, 4, 44);
            borderPaint = new Paint();
            borderPaint.setARGB(255, 0, 0, 0);
            textPaint = new Paint();
            textPaint.setARGB(255, 255, 220, 220);
            textPaint.setTextSize(60);
            textPaint.setTypeface(Typeface.MONOSPACE);
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
            Rect clipBounds = canvas.getClipBounds();
            canvas.drawRect(clipBounds, backgroundPaint);
            canvas.drawRect(0, dimensions.getCellSize() * dimensions.getHeightCells() + 1,
                    dimensions.getCellSize() * dimensions.getWidthCells(), clipBounds.bottom,
                    borderPaint);
            canvas.drawText("Score: " + board.getScore(), 10, clipBounds.bottom - 10, textPaint);

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
        }

        public void createBoard(Rect surfaceFrame) {
            dimensions = Dimensions.calculate(surfaceFrame);

            board = new Board(
                    dimensions.getWidthCells(), dimensions.getHeightCells(), new BlockFactory());
            grid = new CanvasGridAdapter(board);
            board.createFallingBlock();
        }
    }
}
