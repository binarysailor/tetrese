package binarysailor.tetrese.model;

import java.util.function.Consumer;

import binarysailor.tetrese.model.rotation.RotationDirection;

public class CollisionDetector {

    private CollisionEnvironment environment;

    public CollisionDetector(CollisionEnvironment environment) {
        this.environment = environment;
    }

    public synchronized boolean canMoveDown(Block block) {
        return !transformAndCheckCollision(block, Block::moveDown);
    }

    public boolean canMoveUp(Block block) {
        return !transformAndCheckCollision(block, Block::moveUp);
    }

    public boolean canMoveLeft(Block block) {
        return !transformAndCheckCollision(block, Block::moveLeft);
    }

    public boolean canMoveRight(Block block) {
        return !transformAndCheckCollision(block, Block::moveRight);
    }

    public boolean canRotateClockwise(Block block) {
        return !transformAndCheckCollision(block, b -> b.rotate(RotationDirection.CLOCKWISE));
    }

    public boolean canRotateCounterClockwise(Block block) {
        return !transformAndCheckCollision(block, b -> b.rotate(RotationDirection.COUNTERCLOCKWISE));
    }

    private boolean inCollision(Block block) {
        boolean[] collided = new boolean[1];
        block.forEachOccupiedCell((x, y) -> {
            if (x < 0 || x >= environment.getWidthCells()) {
                collided[0] = true;
                return;
            }
            if (y < 0 || y >= environment.getHeightCells()) {
                collided[0] = true;
                return;
            }
            collided[0] |= environment.isOccupied(x, y);
        });

        return collided[0];
    }

    private boolean transformAndCheckCollision(Block block, Consumer<Block> transform) {
        Block hypothetical = new Block(block);
        transform.accept(hypothetical);
        return inCollision(hypothetical);
    }
}
