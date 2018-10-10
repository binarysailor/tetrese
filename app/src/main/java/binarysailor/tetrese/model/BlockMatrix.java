package binarysailor.tetrese.model;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class BlockMatrix {
    private short[][] m;

    public BlockMatrix(short[][] matrix) {
        if (Arrays.stream(matrix).anyMatch(row -> row.length != matrix.length)) {
            throw new IllegalArgumentException("The matrix should be square");
        }
        this.m = new short[matrix.length][matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            System.arraycopy(matrix[row], 0, this.m[row], 0, matrix[row].length);
        }
    }

    public BlockMatrix(BlockMatrix other) {
        this(other.m);
    }

    public void forEachOccupiedCell(BiConsumer<Integer, Integer> consumer) {
        for (int x = 0; x < side(); x++) {
            for (int y = 0; y < side(); y++) {
                if (get(x, y) == 1) {
                    consumer.accept(x, y);
                }
            }
        }
    }

    public short get(int x, int y) {
        return m[y][x];
    }

    public void set(int x, int y, short value) {
        m[y][x] = value;
    }

    public int side() {
        return m.length;
    }

    public Stream<Short> row(int row) {
        Stream.Builder<Short> builder = Stream.builder();
        for (int col = 0; col < m[row].length; col++) {
            builder.add(get(col, row));
        }
        return builder.build();
    }

    public Stream<Short> column(int col) {
        Stream.Builder<Short> builder = Stream.builder();
        for (int row = 0; row < m.length; row++) {
            builder.add(get(col, row));
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockMatrix)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        BlockMatrix other = (BlockMatrix) obj;

        int thisLen = m.length;
        int otherLen = other.m.length;
        if (thisLen != otherLen) {
            return false;
        }

        for (int i = 0; i < thisLen; i++) {
            if (!Arrays.equals(m[i], other.m[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int[] rowHashCodes = new int[m.length];
        for (int i = 0; i < m.length; i++) {
            rowHashCodes[i] = Arrays.hashCode(m[i]);
        }

        return Arrays.hashCode(rowHashCodes);
    }

    public void copyFrom(BlockMatrix other) {
        for (int row = 0; row < m.length; row++) {
            for (int column = 0; column < m[row].length; column++) {
                this.m[row][column] = other.m[row][column];
            }
        }
    }

    public boolean occupies(int x, int y) {
        if (x >= side() || y >= side()) {
            return false;
        }
        return get(x, y) == 1;
    }
}
