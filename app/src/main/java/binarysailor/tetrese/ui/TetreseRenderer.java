package binarysailor.tetrese.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import binarysailor.tetrese.model.Board;

class TetreseRenderer {
    private Board board;
    private Dimensions dimensions;

    private Paint backgroundPaint;
    private Paint borderPaint;
    private Paint textPaint;

    private CanvasGridAdapter grid;

    TetreseRenderer(Board board, Dimensions dimensions) {
        this.board = board;
        this.grid = new CanvasGridAdapter(board);
        this.dimensions = dimensions;

        initGraphicsObjects();
    }

    private void initGraphicsObjects() {
        backgroundPaint = new Paint();
        backgroundPaint.setARGB(255, 85, 4, 44);
        borderPaint = new Paint();
        borderPaint.setARGB(255, 0, 0, 0);
        textPaint = new Paint();
        textPaint.setARGB(255, 255, 220, 220);
        textPaint.setTextSize(60);
        textPaint.setTypeface(Typeface.MONOSPACE);
    }

    void render(Canvas canvas) {
        Rect clipBounds = canvas.getClipBounds();
        canvas.drawRect(clipBounds, backgroundPaint);
        canvas.drawRect(0, dimensions.getCellSize() * dimensions.getHeightCells() + 1,
                dimensions.getCellSize() * dimensions.getWidthCells(), clipBounds.bottom,
                borderPaint);
        canvas.drawText("Score: " + board.getScore(), 10, clipBounds.bottom - 10, textPaint);

        grid.setCanvas(canvas);
        board.getFallingBlock().draw(grid);
        board.getFallenBlocks().forEach(b -> b.draw(grid));
    }
}
