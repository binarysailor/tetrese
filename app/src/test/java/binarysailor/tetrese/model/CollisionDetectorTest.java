package binarysailor.tetrese.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import binarysailor.tetrese.model.rotation.TopLeftGravityRotator;

public class CollisionDetectorTest {
    private TestCollisionEnvironment environment;
    private CollisionDetector detector;

    @Before
    public void setUp() {
        environment = new TestCollisionEnvironment(10, 20);
        detector = new CollisionDetector(environment);
    }

    @Test
    public void shouldDetectBottomBoundaryIShapeHoriz() {
        Block block = createBlock(new short[][] {
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        },0,18);

        Assert.assertTrue(detector.canMoveDown(block));

        block.moveDown();

        Assert.assertFalse(detector.canMoveDown(block));
    }

    @Test
    public void shouldDetectBottomBoundaryIShapeVert() {
        Block block = createBlock(new short[][] {
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0}
        },0, 15);

        Assert.assertTrue(detector.canMoveDown(block));

        block.moveDown();

        Assert.assertFalse(detector.canMoveDown(block));
    }

    @Test
    public void shouldDetectBottomBoundaryLShapeVert() {
        Block block = createBlock(new short[][] {
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 1, 0, 0},
                {0, 0, 0, 0}
        },0,16);

        Assert.assertTrue(detector.canMoveDown(block));

        block.moveDown();

        Assert.assertFalse(detector.canMoveDown(block));
    }

    @Test
    public void shouldDetectLeftBoundaryIShapeVert() {
        Block block = createBlock(new short[][] {
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0},
                {1, 0, 0, 0}
        },0,5);

        Assert.assertFalse(detector.canMoveLeft(block));

        block.moveRight();

        Assert.assertTrue(detector.canMoveLeft(block));
    }

    @Test
    public void shouldDetectLeftBoundaryIShapeVertOffset() {
        Block block = createBlock(new short[][] {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        }, 0, 5);

        Assert.assertTrue(detector.canMoveLeft(block));

        block.moveLeft();

        Assert.assertFalse(detector.canMoveLeft(block));
    }

    @Test
    public void shouldDetectRightBoundaryLShapeHoriz() {
        Block block = createBlock(new short[][] {
                {0, 0, 1, 0},
                {1, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        }, 7, 5);

        Assert.assertFalse(detector.canMoveRight(block));

        block.moveLeft();

        Assert.assertTrue(detector.canMoveRight(block));
    }

    @Test
    public void shouldDetectRightBoundarySquareShape() {
        Block block = createBlock(new short[][] {
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        },5,2);

        Assert.assertTrue(detector.canMoveRight(block));

        block.moveRight();

        Assert.assertTrue(detector.canMoveRight(block));

        block.moveRight();

        Assert.assertFalse(detector.canMoveRight(block));
    }

    @Test
    public void shouldDetectObstacle() {
        Block block = createBlock(new short[][] {
                {0, 1, 1, 0},
                {0, 1, 1, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        },0,0);

        environment.occupy(2, 2);

        Assert.assertFalse(detector.canMoveDown(block));
    }

    private Block createBlock(short[][] matrix, int x, int y) {
        return new Block(matrix, new TopLeftGravityRotator(), 0, x, y);
    }
}
