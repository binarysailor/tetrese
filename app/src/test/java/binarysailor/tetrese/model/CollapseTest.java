package binarysailor.tetrese.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CollapseTest {
    private Board board;

    @Before
    public void setup() {
        board = new Board(4, 8, null, new GameLifecycle());
    }

    @Test
    public void shouldCollapseTwoFullRows() {
        for (int x = 0; x < 4; x +=2) {
            Block block = new Block(new short[][]{
                    {1, 1},
                    {1, 1},
            }, null, 0, x, 6);
            board.getFallenBlocks().add(block);
        }
        /*
        |ooee|
        |ooee|
         */

        board.collapseIfPossible();

        Assert.assertTrue(board.getFallenBlocks().isEmpty());
    }

    @Test
    public void shouldCollapseOneFullRowAndLeaveOnePartial() {
        Block block1 = new Block(new short[][]{
            {1, 1},
            {1, 1},
        }, null, 0, 0, 6);
        board.getFallenBlocks().add(block1);

        Block block2 = new Block(new short[][]{
                {1, 1},
                {0, 0},
        }, null, 0, 2, 7);
        board.getFallenBlocks().add(block2);

        /*
        |oo..|
        |ooee|
         */

        board.collapseIfPossible();

        Assert.assertEquals(1, board.getFallenBlocks().size());

        Block remainingBlock = board.getFallenBlocks().iterator().next();
        Assert.assertSame(block1, remainingBlock);
        Assert.assertEquals(7, remainingBlock.getY());
    }

    @Test
    public void shouldCollapseFullRowAndAboveNotTouchingBelow() {
        Block block1 = new Block(new short[][]{
                {1, 1},
                {1, 1},
        }, null, 0, 0, 5);
        board.getFallenBlocks().add(block1);

        Block block2 = new Block(new short[][]{
                {1, 1},
                {0, 1},
        }, null, 0, 2, 6);
        board.getFallenBlocks().add(block2);

        Block block3 = new Block(new short[][]{
                {1, 1},
                {0, 0},
        }, null, 0, 0, 7);
        board.getFallenBlocks().add(block3);

        Block block4 = new Block(new short[][]{
                {1, 0},
                {1, 0},
        }, null, 0, 3, 4);
        board.getFallenBlocks().add(block4);

        /*
        4:|...4|
        5:|11.4|
        6:|1122|
        7:|33.2|
         */

        board.collapseIfPossible();

        Assert.assertEquals(4, board.getFallenBlocks().size());

        Assert.assertEquals(6, block1.getY());
        Assert.assertEquals(2, getOccupiedCellCount(block1));

        Assert.assertEquals(7, block2.getY());
        Assert.assertEquals(1, getOccupiedCellCount(block2));

        Assert.assertEquals(7, block3.getY());
        Assert.assertEquals(2, getOccupiedCellCount(block3));

        Assert.assertEquals(5, block4.getY());
        Assert.assertEquals(2, getOccupiedCellCount(block4));

    }

    private int getOccupiedCellCount(Block b) {
        class Counter {
            int count;
            void inc() { count++; }
        }
        Counter counter = new Counter();
        b.forEachOccupiedCell((x,y) -> counter.inc());

        return counter.count;
    }

}
