package binarysailor.tetrese.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import binarysailor.tetrese.model.rotation.RotationDirection;

public class Board implements CollisionEnvironment {
    private int interval = 200; // ms

    private int widthCells;
    private int heightCells;

    private Block fallingBlock;
    private final Collection<Block> fallenBlocks = new ArrayList<>();

    private final BlockFactory blockFactory;

    private final CollisionDetector collisionDetector;

    private long lastTick = System.nanoTime();

    public Board(int widthCells, int heightCells, BlockFactory blockFactory) {
        this.widthCells = widthCells;
        this.heightCells = heightCells;
        this.blockFactory = blockFactory;
        this.collisionDetector = new CollisionDetector(this);
    }

    public void createFallingBlock() {
        fallingBlock = blockFactory.createRandomBlock(widthCells - 4);
    }

    public synchronized void update() {
        long currentTime = System.nanoTime();
        if (currentTime - lastTick > interval * 1000 * 1000) {
            fall();
            lastTick = currentTime;
        }
    }

    private void fall() {
        if (fallingBlock != null) {
            if (collisionDetector.canMoveDown(fallingBlock)) {
                fallingBlock.moveDown();
            } else {
                fallenBlocks.add(fallingBlock);
                createFallingBlock();
            }
        }
    }

    public Block getFallingBlock() {
        return fallingBlock;
    }

    public synchronized boolean tryMoveLeft() {
        if (fallingBlock == null || !collisionDetector.canMoveLeft(fallingBlock)) {
            return false;
        }
        fallingBlock.moveLeft();
        return true;
    }

    public synchronized boolean tryMoveRight() {
        if (fallingBlock == null || !collisionDetector.canMoveRight(fallingBlock)) {
            return false;
        }
        fallingBlock.moveRight();
        return true;
    }

    public synchronized boolean tryRotateClockwise() {
        if (fallingBlock == null || !collisionDetector.canRotateClockwise(fallingBlock)) {
            return false;
        }
        fallingBlock.rotate(RotationDirection.CLOCKWISE);
        return true;
    }

    public synchronized boolean tryRotateCounterclockwise() {
        if (fallingBlock == null || !collisionDetector.canRotateCounterClockwise(fallingBlock)) {
            return false;
        }
        fallingBlock.rotate(RotationDirection.COUNTERCLOCKWISE);
        return true;
    }

    public Collection<Block> getFallenBlocks() {
        return fallenBlocks;
    }

    public int getWidthCells() {
        return widthCells;
    }

    public int getHeightCells() {
        return heightCells;
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return fallenBlocks.stream().anyMatch(b -> b.occupies(x, y));
    }
}
