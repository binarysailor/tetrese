package binarysailor.tetrese.ui.menu;

import android.graphics.Point;
import android.graphics.RectF;

class MenuItem {
    private final Object tag;
    private final String text;
    private final RectF bounds;
    private final Point textCoordinates;

    MenuItem(Object tag, String text, RectF bounds, Point textCoordinates) {
        this.tag = tag;
        this.text = text;
        this.bounds = bounds;
        this.textCoordinates = textCoordinates;
    }

    public Object getTag() {
        return tag;
    }

    public String getText() {
        return text;
    }

    public RectF getBounds() {
        return bounds;
    }

    public Point getTextCoordinates() {
        return textCoordinates;
    }

    public boolean containsPoint(Point p) {
        return bounds.contains(p.x, p.y);
    }
}
