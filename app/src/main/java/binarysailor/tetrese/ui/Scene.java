package binarysailor.tetrese.ui;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public interface Scene {
    void update();
    void draw(Canvas canvas);
    boolean onTouch(View view, MotionEvent motionEvent);
}
