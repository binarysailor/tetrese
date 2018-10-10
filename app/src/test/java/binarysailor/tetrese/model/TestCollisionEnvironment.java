package binarysailor.tetrese.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class TestCollisionEnvironment implements CollisionEnvironment {

    private final int widthCells;
    private final int heightCells;

    private final Set<Coords> occupiedCells = new HashSet<>();

    public TestCollisionEnvironment(int widthCells, int heightCells) {
        this.widthCells = widthCells;
        this.heightCells = heightCells;
    }

    @Override
    public int getWidthCells() {
        return widthCells;
    }

    @Override
    public int getHeightCells() {
        return heightCells;
    }

    public void occupy(int x, int y) {
        occupiedCells.add(new Coords(x, y));
    }

    @Override
    public boolean isOccupied(int x, int y) {
        return occupiedCells.contains(new Coords(x, y));
    }

    static class Coords {
        int x, y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Coords)) return false;
            Coords other = (Coords)obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
