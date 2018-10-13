package binarysailor.tetrese.model;

import java.util.Objects;
import java.util.function.BiConsumer;

import binarysailor.tetrese.model.rotation.RotationDirection;
import binarysailor.tetrese.model.rotation.Rotator;

public class Block {
    protected int x, y; // top left corner
    private int color;
    private final BlockMatrix matrix;
    private final Rotator rotator;

    Block(BlockMatrix matrix, Rotator rotator, int color, int x, int y) {
        this.matrix = matrix;
        this.rotator = rotator;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    protected Block(short[][] matrix, Rotator rotator, int color, int x, int y) {
        this(new BlockMatrix(matrix), rotator, color, x, y);
    }

    Block(Block other) {
        this(new BlockMatrix(other.matrix), other.rotator, other.color, other.x, other.y);
    }

    public void copyFrom(Block other) {
        this.x = other.x;
        this.y = other.y;
        this.matrix.copyFrom(other.matrix);
    }

    public void draw(Grid target) {
        forEachOccupiedCell((xc, yc) -> target.drawCell(xc, yc, color));
    }

    public void rotate(RotationDirection direction) {
        rotator.rotate(matrix, direction);
    }

    public void forEachOccupiedCell(BiConsumer<Integer, Integer> consumer) {
        matrix.forEachOccupiedCell((xc, yc) -> consumer.accept(x + xc, y + yc));
    }

    public boolean occupies(int x, int y) {
        if (x < this.x) {
            return false;
        }
        if (y < this.y) {
            return false;
        }
        return matrix.occupies(x - this.x, y - this.y);
    }

    public void moveDown() {
        y++;
    }

    public void moveUp() {
        y--;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void collapseRow(int boardRow) {
        if (boardRow >= y && boardRow < y + matrix.side()) {
            // the row that gets collapsed is actually cutting through this block
            matrix.collapseRow(boardRow - y);
            y++;
        } else if (boardRow > y) {
            // the row that gets collapsed is disjoint with this block, but it's below, so this block moves down one cell
            y++;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Block other = (Block) obj;
        return other.x == this.x && other.y == this.y && other.matrix.equals(this.matrix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, matrix.hashCode());
    }


}
