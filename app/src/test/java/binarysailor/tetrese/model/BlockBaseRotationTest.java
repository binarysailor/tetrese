package binarysailor.tetrese.model;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import binarysailor.tetrese.model.rotation.RotationDirection;
import binarysailor.tetrese.model.rotation.Rotator;
import binarysailor.tetrese.model.rotation.TopLeftGravityRotator;


public class BlockBaseRotationTest {

    private final Rotator rotator = new TopLeftGravityRotator();

    @Test
    public void shouldRotateIBlockCorrectly() {
        executeTests("IBlock");
    }

    @Test
    public void shouldRotateLBlockCorrectly() {
        executeTests("LBlock");
    }

    @Test
    public void shouldRotateTBlockCorrectly() {
        executeTests("TBlock");
    }

    private void executeTests(String fileName) {
        RotationTestSpecParser parser = new RotationTestSpecParser();
        try (InputStream is = getClass().getResourceAsStream(fileName + ".txt")) {
            RotationTestSpecParser.MatrixPair[] pairs = parser.parse(is);
            for (RotationTestSpecParser.MatrixPair pair : pairs) {
                testClockwise(pair);
                testCounterClockwise(pair);
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void testClockwise(RotationTestSpecParser.MatrixPair pair) {
        Block b = new Block(pair.first, rotator, 0, 0, 0);
        b.rotate(RotationDirection.CLOCKWISE);
        Assert.assertEquals(new Block(pair.second, rotator, 0, 0, 0), b);
    }

    private void testCounterClockwise(RotationTestSpecParser.MatrixPair pair) {
        Block b = new Block(pair.second, rotator, 0, 0, 0);
        b.rotate(RotationDirection.COUNTERCLOCKWISE);
        Assert.assertEquals(new Block(pair.first, rotator, 0, 0, 0), b);
    }
}
