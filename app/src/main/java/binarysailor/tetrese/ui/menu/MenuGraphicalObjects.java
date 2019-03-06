package binarysailor.tetrese.ui.menu;

import android.graphics.Color;
import android.graphics.Paint;

class MenuGraphicalObjects {
    final Paint backgroundPaint;
    final Paint optionFramePaint;
    final Paint optionTextPaint;
    final Paint optionBackgroundPaint;
    final Paint optionBackgroundHoverPaint;

    MenuGraphicalObjects() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        optionFramePaint = new Paint();
        optionFramePaint.setARGB(255, 255, 255, 255);
        optionTextPaint = new Paint();
        optionTextPaint.setARGB(255, 255, 255, 255);
        optionTextPaint.setTextSize(130f);
        optionBackgroundPaint = new Paint();
        optionBackgroundPaint.setARGB(255, 0, 204, 68);
        optionBackgroundHoverPaint = new Paint();
        optionBackgroundHoverPaint.setARGB(255, 130, 255, 255);
    }
}
