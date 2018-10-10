package binarysailor.tetrese.model.rotation;

import binarysailor.tetrese.model.BlockMatrix;

public class TopLeftGravityRotator implements Rotator {

    @Override
    public void rotate(BlockMatrix matrix, RotationDirection direction) {
        for (int layer = 0; layer < matrix.side() / 2; layer++) {
            rotateLayer(matrix, layer, direction);
        }
        moveTopLeft(matrix);
    }

    private void rotateLayer(BlockMatrix matrix, int layer, RotationDirection direction) {
        final int side = matrix.side();
        final int layerSideLen = side - 2 * layer;
        final int layerSideStartOffset = layer;

        for (int offset = layerSideStartOffset; offset < layerSideStartOffset + layerSideLen - 1; offset++) {
            short store;

            int leftx = layer, lefty = side - 1 - offset;
            int topx = offset, topy = layer;
            int rightx = side - 1 - layer, righty = offset;
            int bottomx = side - 1 - offset, bottomy = side - 1 - layer;

            switch (direction) {
                case CLOCKWISE:
                    // store right
                    store = matrix.get(rightx, righty);
                    // top -> right
                    copy(matrix, topx, topy, rightx, righty);
                    // left -> top
                    copy(matrix, leftx, lefty, topx, topy);
                    // bottom -> left
                    copy(matrix, bottomx, bottomy, leftx, lefty);
                    // storage -> bottom
                    matrix.set(bottomx, bottomy, store);
                    break;
                case COUNTERCLOCKWISE:
                    // store left
                    store = matrix.get(leftx, lefty);
                    // top -> left
                    copy(matrix, topx, topy, leftx, lefty);
                    // right -> top
                    copy(matrix, rightx, righty, topx, topy);
                    // bottom -> right
                    copy(matrix, bottomx, bottomy, rightx, righty);
                    // storage -> bottom
                    matrix.set(bottomx, bottomy, store);
            }
        }

    }

    private void copy(BlockMatrix matrix, int srcx, int srcy, int dstx, int dsty) {
        matrix.set(dstx, dsty, matrix.get(srcx, srcy));
    }

    private void moveTopLeft(BlockMatrix matrix) {
        int row = 0;
        while (hasEmptyRow(matrix, row)) {
            row++;
        }

        int topEmptyRows = row;

        if (topEmptyRows > 0) {
            moveUp(matrix, topEmptyRows);
        }

        int col = 0;
        while (hasEmptyCol(matrix, col)) {
            col++;
        }

        int leftEmptyCols = col;

        if (leftEmptyCols > 0) {
            moveLeft(matrix, leftEmptyCols);
        }
    }

    private boolean hasEmptyRow(BlockMatrix matrix, int row) {
        return matrix.row(row).allMatch(s -> s == 0);
    }

    private boolean hasEmptyCol(BlockMatrix matrix, int col) {
        return matrix.column(col).allMatch(s -> s == 0);
    }

    private void moveUp(BlockMatrix matrix, int offset) {
        for (int row = 0; row < matrix.side() - offset; row++) {
            for (int col = 0; col < matrix.side(); col++) {
                matrix.set(col, row, matrix.get(col, row + offset));
            }
        }
        for (int row = matrix.side() - offset; row < matrix.side(); row++) {
            for (int col = 0; col < matrix.side(); col++) {
                matrix.set(col, row, (short)0);
            }
        }
    }

    private void moveLeft(BlockMatrix matrix, int offset) {
        for (int col = 0; col < matrix.side() - offset; col++) {
            for (int row = 0; row < matrix.side(); row++) {
                matrix.set(col, row, matrix.get(col + offset, row));
            }
        }
        for (int col = matrix.side() - offset; col < matrix.side(); col++) {
            for (int row = 0; row < matrix.side(); row++) {
                matrix.set(col, row, (short)0);
            }
        }
    }
}
