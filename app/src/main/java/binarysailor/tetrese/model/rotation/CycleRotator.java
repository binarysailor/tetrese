package binarysailor.tetrese.model.rotation;

import binarysailor.tetrese.model.BlockMatrix;

public class CycleRotator implements Rotator {
    private final short[][][] matrices;
    private int current;

    public CycleRotator(short[][] ... matrices) {
        this.matrices = matrices;
    }

    @Override
    public void rotate(BlockMatrix matrix, RotationDirection direction) {
        switch (direction) {
            case CLOCKWISE:
                current++;
                if (current > matrices.length - 1) {
                    current = 0;
                }
                break;
            case COUNTERCLOCKWISE:
                current--;
                if (current < 0) {
                    current = matrices.length - 1;
                }
        }

        apply(matrices[current], matrix);
    }

    private void apply(short[][] matrix, BlockMatrix target) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                target.set(x, y, matrix[y][x]);
            }
        }
    }
}
