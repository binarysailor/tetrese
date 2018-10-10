package binarysailor.tetrese.model.rotation;

import binarysailor.tetrese.model.BlockMatrix;

public interface Rotator {
    void rotate(BlockMatrix matrix, RotationDirection direction);
}
