package binarysailor.tetrese.model;

import android.graphics.Color;

import java.util.Random;

public class ColorPalette {
    private final Random random = new Random(System.currentTimeMillis());
    private final int[] colors;

    public ColorPalette(String... hexColors) {
        colors = new int[hexColors.length];
        for (int i = 0; i < hexColors.length; i++) {
            colors[i] = toColor(hexColors[i]);
        }
    }

    private int toColor(String hexColor) {
        if (hexColor.length() != 6) {
            throw new IllegalArgumentException("A hex color must be 6 characters long (RRGGBB)");
        }

        return Color.parseColor("#" + hexColor);
    }

    public int getRandomColor() {
        return colors[random.nextInt(colors.length)];
    }
}
