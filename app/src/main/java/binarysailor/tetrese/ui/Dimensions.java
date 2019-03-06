package binarysailor.tetrese.ui;

import android.graphics.Rect;

public class Dimensions {

    private static final int WIDTH_CELLS = 10;

    private Rect surfaceRect;
    private int cellSize, heightCells;
    private int widthPx, heightPx;

    static Dimensions calculate(Rect frame) {

        int cellSize = frame.width() / WIDTH_CELLS;
        int heightCells = Math.min(
                frame.height() / cellSize,
                16 * WIDTH_CELLS / 9);

        int minStatusPanelHeight = (int)(1.5 * cellSize);
        while (frame.height() - heightCells * cellSize < minStatusPanelHeight) {
            heightCells--;
        }

        Dimensions dimensions = new Dimensions();
        dimensions.surfaceRect = frame;
        dimensions.cellSize = cellSize;
        dimensions.heightCells = heightCells;
        dimensions.widthPx = dimensions.getWidthCells() * cellSize;
        dimensions.heightPx = heightCells * cellSize;

        return dimensions;
    }

    public Rect getSurfaceRect() {
        return surfaceRect;
    }

    public int getWidthCells() {
        return WIDTH_CELLS;
    }

    public int getHeightCells() {
        return heightCells;
    }

    public int getCellSize() {
        return cellSize;
    }

    public int getWidthPx() {
        return widthPx;
    }

    public int getHeightPx() {
        return heightPx;
    }
}
