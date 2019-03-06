package binarysailor.tetrese.ui.menu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import binarysailor.tetrese.ui.Dimensions;

class MenuRenderer {

    private final Dimensions dimensions;
    private final MenuGraphicalObjects graphicalObjects;

    private final Menu menu;

    MenuRenderer(Dimensions dimensions, Menu menu, MenuGraphicalObjects graphicalObjects) {
        this.dimensions = dimensions;
        this.graphicalObjects = graphicalObjects;
        this.menu = menu;
    }

    void render(Canvas canvas) {
        Rect clipBounds = canvas.getClipBounds();
        canvas.drawRect(clipBounds, graphicalObjects.backgroundPaint);
        for (MenuItem menuItem : menu.getItems()) {
            RectF bounds = menuItem.getBounds();
            canvas.drawRoundRect(bounds, 20, 20, graphicalObjects.optionBackgroundPaint);
            Point textCoordinates = menuItem.getTextCoordinates();
            canvas.drawText(menuItem.getText(), textCoordinates.x, textCoordinates.y, graphicalObjects.optionTextPaint);
        }
    }
}
