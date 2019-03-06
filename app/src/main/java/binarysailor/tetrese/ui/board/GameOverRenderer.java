package binarysailor.tetrese.ui.board;

import android.graphics.Canvas;
import android.graphics.Paint;

class GameOverRenderer {

    private final Paint dimPaint, textPaint;

    GameOverRenderer() {
        dimPaint = new Paint();
        dimPaint.setARGB(210, 0, 0, 0);
        textPaint = new Paint();
        textPaint.setARGB(255, 255, 255, 255);
    }

    void render(Canvas canvas) {
        canvas.drawRect(canvas.getClipBounds(), dimPaint);

        textPaint.setTextSize(140.0f);
        canvas.drawText("GAME OVER", 40, canvas.getHeight() / 2, textPaint);
        textPaint.setTextSize(70.0f);
        canvas.drawText("Touch to restart", 40, canvas.getHeight() / 2 + 100, textPaint);
    }
}
