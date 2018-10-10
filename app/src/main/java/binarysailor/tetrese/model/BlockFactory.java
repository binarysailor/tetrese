package binarysailor.tetrese.model;

import java.util.Random;

import binarysailor.tetrese.model.rotation.TopLeftGravityRotator;

public class BlockFactory {
    private final String[][] TEMPLATES = {
            {
                "*...",
                "*...",
                "*...",
                "*..."
            },
            {
                "*...",
                "*...",
                "**..",
                "...."
            },
            {
                "***.",
                ".*..",
                "....",
                "...."
            },
            {
                ".*..",
                ".*..",
                "**..",
                "...."
            },
            {
                "**..",
                "**..",
                "....",
                "...."
            },
            {
                "**..",
                ".**.",
                "....",
                "...."
            },
            {
                ".**.",
                "**..",
                "....",
                "...."
            }
    };

    private final Random random = new Random(System.currentTimeMillis());
    private final ColorPalette palette = new ColorPalette("FF8888", "88FF88", "8888FF");
    private final BlockMatrixParser parser = new BlockMatrixParser('.', '*');

    public Block createRandomBlock(int maxX) {
        String[] template = TEMPLATES[random.nextInt(TEMPLATES.length)];
        return new Block(parser.parseArray(template), new TopLeftGravityRotator(), palette.getRandomColor(), random.nextInt(maxX + 1), 0);
    }

}
