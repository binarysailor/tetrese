package binarysailor.tetrese.ui;

import android.graphics.Canvas;
import android.graphics.Paint;

import binarysailor.tetrese.model.Board;
import binarysailor.tetrese.model.Grid;

public class CanvasGridAdapter implements Grid {
    private Canvas canvas;
    private Board board;
    private int widthCells;
    private Paint paint = new Paint();

    public CanvasGridAdapter(Board board) {
        this.board = board;
        this.widthCells = board.getWidthCells();
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;

    }

    @Override
    public void drawCell(int x, int y, int color) {
        float cellSize = (float)canvas.getWidth() / widthCells;
        paint.setColor(color);
        canvas.drawRect(x * cellSize + 1, y * cellSize + 1, (x + 1) * cellSize - 2, (y + 1) * cellSize - 2, paint);
    }
}
