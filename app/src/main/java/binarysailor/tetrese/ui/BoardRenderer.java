package binarysailor.tetrese.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import binarysailor.tetrese.model.Block;
import binarysailor.tetrese.model.Board;

class BoardRenderer {
    private Board board;
    private Dimensions dimensions;

    private Paint blockPaint;
    private Paint backgroundPaint;
    private Paint borderPaint;
    private Paint textPaint;

    BoardRenderer(Board board, Dimensions dimensions) {
        this.board = board;
        this.dimensions = dimensions;

        initGraphicsObjects();
    }

    private void initGraphicsObjects() {
        blockPaint = new Paint();
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

        drawBlock(board.getFallingBlock(), canvas);
        board.getFallenBlocks().forEach(b -> drawBlock(b, canvas));
    }

    private void drawBlock(Block block, Canvas target) {
        block.forEachOccupiedCell((x, y, color) -> {
            drawCell(x, y, color, target);
        });
    }

    private void drawCell(int x, int y, int color, Canvas target) {
        float cellSize = (float) target.getWidth() / board.getWidthCells();
        blockPaint.setColor(color);
        target.drawRect(x * cellSize + 1, y * cellSize + 1, (x + 1) * cellSize - 2, (y + 1) * cellSize - 2, blockPaint);
    }
}
