package binarysailor.tetrese.model;

public interface CollisionEnvironment {
    int getWidthCells();
    int getHeightCells();
    boolean isOccupied(int x, int y);
}
