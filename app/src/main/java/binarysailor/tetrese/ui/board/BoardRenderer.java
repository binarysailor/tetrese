package binarysailor.tetrese.ui.board;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import binarysailor.tetrese.model.Block;
import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.ui.Dimensions;

class BoardRenderer {
    private final Board board;
    private final Dimensions dimensions;

    private final Paint blockPaint;
    private final Paint backgroundPaint;
    private final Paint borderPaint;
    private final Paint textPaint;

    BoardRenderer(Board board, Dimensions dimensions) {
        this.board = board;
        this.dimensions = dimensions;

        this.blockPaint = new Paint();
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setARGB(255, 85, 4, 44);
        this.borderPaint = new Paint();
        this.borderPaint.setARGB(255, 0, 0, 0);
        this.textPaint = new Paint();
        this.textPaint.setARGB(255, 255, 220, 220);
        this.textPaint.setTextSize(60);
        this.textPaint.setTypeface(Typeface.MONOSPACE);
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

        synchronized (board) {
            if (board.getFallingBlock() != null) {
                drawBlock(board.getFallingBlock(), canvas);
            }
            board.getFallenBlocks().forEach(b -> drawBlock(b, canvas));

            drawNextBlockPreview(canvas);
        }
    }

    private void drawBlock(Block block, Canvas target) {
        block.forEachOccupiedCell((x, y, color) -> drawCell(x, y, color, target));
    }

    private void drawCell(int x, int y, int color, Canvas target) {
        float cellSize = (float) target.getWidth() / board.getWidthCells();
        blockPaint.setColor(color);
        float top = y * cellSize + 1;
        float bottom = (y + 1) * cellSize - 2;
        float left = x * cellSize + 1;
        float right = (x + 1) * cellSize - 2;
        target.drawRect(left, top, right, bottom, blockPaint);

        int highlightThickness = 7;
        // highlight
        blockPaint.setARGB(100,255, 255, 255);
        for (int i = 0; i < highlightThickness; i++) {
            target.drawLine(left, top + i, right, top + i, blockPaint);
            target.drawLine(left + i, top + highlightThickness, left + i, bottom, blockPaint);
        }
        // shadow
        blockPaint.setARGB(100,0, 0, 0);
        for (int i = 0; i < highlightThickness; i++) {
            target.drawLine(right - i, top + highlightThickness, right - i, bottom, blockPaint);
            target.drawLine(left + highlightThickness, bottom - i, right, bottom - i, blockPaint);
        }
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

    boolean isCommunicationArea(float x, float y) {
        return y > dimensions.getCellSize() * dimensions.getHeightCells();
    }

    BoardQuarter resolveBoardQuarter(float x, float y) {
        if (isCommunicationArea(x, y)) {
            return null;
        }

        float ratio = (float)dimensions.getHeightPx() / dimensions.getWidthPx();
        BoardQuarter result;
        if (x * ratio > y) {
            // top-right half
            if (dimensions.getHeightPx() - x * ratio > y) {
                result = BoardQuarter.NORTH;
            } else {
                result = BoardQuarter.EAST;
            }
        } else {
            // bottom-left half
            if (dimensions.getHeightPx() - x * ratio > y) {
                result = BoardQuarter.WEST;
            } else {
                result = BoardQuarter.SOUTH;
            }
        }

        Log.d("resolveBoardQuarter", String.format("Ratio: %.2f. (%.1f, %.1f) resolved to %s", ratio, x, y, result));
        return result;
    }
}
