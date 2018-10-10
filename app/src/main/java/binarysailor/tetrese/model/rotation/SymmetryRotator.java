package binarysailor.tetrese.model.rotation;

import binarysailor.tetrese.model.BlockMatrix;

public class SymmetryRotator implements Rotator {

    private static final SymmetryRotator instance = new SymmetryRotator();

    public static SymmetryRotator getInstance() {
        return instance;
    }

    public void rotate(BlockMatrix matrix, RotationDirection direction) {
        for (int layer = 0; layer < matrix.side() / 2; layer++) {
            rotateLayer(matrix, layer, direction);
        }
    }

    private void rotateLayer(BlockMatrix matrix, int layer, RotationDirection direction) {
        final int side = matrix.side();
        final int layerSideLen = side - 2 * layer;
        final int layerSideStartOffset = layer;

        for (int offset = layerSideStartOffset; offset < layerSideStartOffset + layerSideLen; offset++) {
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
}
