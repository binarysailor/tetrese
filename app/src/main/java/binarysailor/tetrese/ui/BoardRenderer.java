package binarysailor.tetrese.ui;

import android.graphics.Canvas;
import android.graphics.Color;
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
        int textY = clipBounds.bottom - 10;
        canvas.drawText("Score: " + board.getScore(), 10, textY, textPaint);
        canvas.drawText("Next: ", clipBounds.width() - 300, textY, textPaint);

        drawBlock(board.getFallingBlock(), canvas);
        board.getFallenBlocks().forEach(b -> drawBlock(b, canvas));

        drawNextBlockPreview(canvas);
    }

    private void drawBlock(Block block, Canvas target) {
        block.forEachOccupiedCell((x, y, color) -> {
            drawCell(x, y, color, target);
        });
    }

    private void drawCell(int x, int y, int color, Canvas target) {
        float cellSize = (float) target.getWidth() / board.getWidthCells();
        blockPaint.setColor(color);
        target.drawRect(
                x * cellSize + 1,
                y * cellSize + 1,
                (x + 1) * cellSize - 2,
                (y + 1) * cellSize - 2,
                blockPaint);
    }

    private void drawNextBlockPreview(Canvas target) {
        Block nextFalling = board.getNextFallingBlock();
        if (nextFalling != null) {
            int originX = target.getWidth() - 102;
            int originY = target.getClipBounds().height() - 102;
            int cellsize = 25;
            nextFalling.forEachOccupiedCell((x, y, color) -> {
                blockPaint.setARGB(255, 180, 180, 180);
                int relativeX = x - nextFalling.getX();
                int relativeY = y - nextFalling.getY();
                target.drawRect(
                        originX + relativeX * cellsize + 1,
                        originY + relativeY * cellsize + 1,
                        originX + (relativeX + 1) * cellsize - 2,
                        originY + (relativeY + 1) * cellsize - 2,
                        blockPaint);
            });
        }
    }
}
