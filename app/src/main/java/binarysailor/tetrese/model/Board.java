package binarysailor.tetrese.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.IntStream;

import binarysailor.tetrese.model.rotation.RotationDirection;

public class Board implements CollisionEnvironment {
    private int interval = 400; // ms

    private int widthCells;
    private int heightCells;

    private Block fallingBlock, nextFallingBlock;
    private final Collection<Block> fallenBlocks = new ArrayList<>();

    private int score;

    private GameLifecycle lifecycle;
    private final BlockFactory blockFactory;

    private final CollisionDetector collisionDetector;

    private long lastTick = System.nanoTime();

    public Board(int widthCells, int heightCells, BlockFactory blockFactory, GameLifecycle lifecycle) {
        this.widthCells = widthCells;
        this.heightCells = heightCells;
        this.blockFactory = blockFactory;
        this.collisionDetector = new CollisionDetector(this);
        this.lifecycle = lifecycle;
    }

    public void createFallingBlock() {
        if (nextFallingBlock != null) {
            fallingBlock = nextFallingBlock;
        } else {
            fallingBlock = blockFactory.createRandomBlock(widthCells - 4);
        }
        nextFallingBlock = blockFactory.createRandomBlock(widthCells - 4);
    }

    public synchronized void update() {
        if (lifecycle.getState() == GameLifecycle.State.PLAYING) {
            long currentTime = System.nanoTime();
            if (currentTime - lastTick > interval * 1000 * 1000) {
                descend();
                lastTick = currentTime;
            }
        }
    }

    private boolean descend() {
        if (fallingBlock != null) {
            if (collisionDetector.canMoveDown(fallingBlock)) {
                fallingBlock.moveDown();
                return true;
            } else {
                fallenBlocks.add(fallingBlock);
                collapseIfPossible();
                createFallingBlock();
                if (collisionDetector.inCollision(fallingBlock)) {
                    gameOver();
                }
                return false;
            }
        }

        return false;
    }

    public void gameOver() {
        lifecycle.gameOver();
    }

    public void startGame() {
        lifecycle.restartGame();
        fallenBlocks.clear();
        score = 0;
        createFallingBlock();
    }

    void collapseIfPossible() {
        Set<Integer> potentialRows = new HashSet<>();
        for (Block block : fallenBlocks) {
            block.forEachOccupiedCell((x, y, color) -> potentialRows.add(y));
        }
        potentialRows.stream().filter(this::isRowFull).forEach(this::collapseRow);
    }

    private boolean isRowFull(int row) {
        return IntStream.range(0, widthCells).allMatch(x -> isOccupied(x, row));
    }

    private void collapseRow(int row) {
        for (Iterator<Block> blocks = fallenBlocks.iterator(); blocks.hasNext(); ) {
            Block block = blocks.next();
            block.collapseRow(row);
            if (block.getY() >= heightCells) {
                blocks.remove();
                score += 10;
            }
        }
        score += 100;
    }

    public Block getFallingBlock() {
        return fallingBlock;
    }

    public Block getNextFallingBlock() {
        return nextFallingBlock;
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

    public void dropFallingBlock() {
        boolean d;
        do {
            d = descend();
        } while (d);
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

    public int getScore() {
        return score;
    }

}
